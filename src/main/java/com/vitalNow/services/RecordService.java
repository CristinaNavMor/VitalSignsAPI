package com.vitalNow.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.vitalNow.enums.VitalSignType;
import com.vitalNow.exception.BadRequestException;
import com.vitalNow.exception.ElementNotFoundException;
import com.vitalNow.exception.InternalServerErrorException;
import com.vitalNow.exception.ServiceUnavailableException;
import com.vitalNow.model.Patient;
import com.vitalNow.model.Record;
import com.vitalNow.model.VitalSign;
import com.vitalNow.model.dto.RecordDto;
import com.vitalNow.model.dto.RecordToAddDto;
import com.vitalNow.model.dto.RecordToDelDto;
import com.vitalNow.model.dto.RecordToPatchDto;
import com.vitalNow.repository.PatientRepository;
import com.vitalNow.repository.RecordRepository;
import com.vitalNow.repository.VitalSignRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class RecordService {

	@Autowired
	RecordRepository recordRepository;
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	VitalSignRepository vitalSignRepository;
	
	@Autowired
	GeneralService generalService;
	
	@Autowired
	AuthService authService;
	
	public List<Record> getLastWeekRecords(String medicalRecordNumber, String vitalSignName){
		return this.recordRepository.getLastRecordsInDays(medicalRecordNumber, vitalSignName, 7);
	}
	
	public List<Record> getLastMonthRecords(String medicalRecordNumber, String vitalSignName){
		return this.recordRepository.getLastRecordsInDays(medicalRecordNumber, vitalSignName, 30);
	}
	
	public List<Record> getLast24hRecords(String medicalRecordNumber, String vitalSignName){
		return this.recordRepository.getLast24hRecords(medicalRecordNumber, vitalSignName);
	}
	public Optional<Record> getLastRecord(String medicalRecordNumber, String vitalSignName){
		return this.recordRepository.getLastRecord(medicalRecordNumber, vitalSignName);
	}
	
	private boolean isAlert(VitalSign vs, Float value) {
		return value<vs.getMin() || value>vs.getMax();
	}
	
	private void validateRecord(Record record) throws BadRequestException {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Record>> violations = validator.validate(record);
		if(!violations.isEmpty()) {
			throw new BadRequestException(generalService.getErrorMessages(violations));
		}
	}
	
	//Inacabado
	public RecordDto addRecord(String medicalRecordNumber, RecordToAddDto recordToAdd) {
		
		VitalSignType vitalSigntype = VitalSignType.getVitalSignFromString(recordToAdd.getVitalSign());
		if (vitalSigntype==null) {
			throw new BadRequestException("Vital sign format null or not valid. Must be "+VitalSignType.BLOOD_PRESSURE.toString() +" "+
																				VitalSignType.BODY_TEMPERATURE.toString()+" "+
																				VitalSignType.BREATHING_RATE.toString()+ " "+
																				VitalSignType.HEART_RATE.toString()+ " or "+
																				VitalSignType.OXYGEN_SATURATION.toString());
		}
		try {
			Patient patient = this.patientRepository.findByMedicalRecordNumber(Integer.parseInt(medicalRecordNumber)).orElseThrow();
			
			
			Record record = new Record();
			
			record.setPatient(patient);
			record.setUser(this.authService.getUserLogged());
			VitalSign vs = this.vitalSignRepository.findByName(vitalSigntype.toString()).orElse(null);
			Float value = recordToAdd.getValue();
			if(value==null) throw new BadRequestException("Value is null."); 
			record.setVitalSign(vs);
			record.setAlert(isAlert(vs, value));
			record.setDateTime(LocalDateTime.now());
			record.setPatient(patient);
			record.setValue(value);
			
			validateRecord(record);
			
			return new RecordDto(this.recordRepository.save(record));
		}catch(BadRequestException bre) {
			throw bre;
		}catch(NumberFormatException nfe) {
			throw new BadRequestException("Invalid format for medical record number: Not a number.");
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("There could not be found a patient with medical record number "+medicalRecordNumber);
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}
//	public RecordDto getRecord(String medicalRecordNumber, String username, String vitalSign,String dateTime) {
//		
//	}
	
	private LocalDateTime isValidLocalDateTimeFormat(String time) {
		try {
			return LocalDateTime.parse(time);			
		}catch(DateTimeParseException dtp) {
			throw new BadRequestException("DateTime format is not valid. It must have a format like ''2007-12-03T10:15:30''.");
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}
	//Ver si el record existe
	//Cambiar el dato. 
	//
	
	private Record getRecord(String medicalRecordNumber, String username, String vitalSign, LocalDateTime dateTime) {
		try {
//			LocalDateTime dateAndTime = isValidLocalDateTimeFormat(dateTime);
			return this.recordRepository.getRecord(medicalRecordNumber, vitalSign, username, dateTime).orElseThrow();
		}catch(BadRequestException bre) {
			throw bre;
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("There could not be found a record with medical record number "+medicalRecordNumber+
												" and username "+username + " and vital sign type "+vitalSign+" and date and time "+dateTime);
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}
	

	
	public RecordDto patchRecord(String medicalRecordNumber, RecordToPatchDto record) {
	
		try {
			Record recordToEdit = getRecord(medicalRecordNumber, record.getUsername(), record.getVitalSign(), record.getDateTime());
			recordToEdit.setValue(record.getValue());
			validateRecord(recordToEdit);
			return new RecordDto(this.recordRepository.save(recordToEdit));
		}catch(BadRequestException bre) {
			throw bre;
		}catch(IllegalArgumentException iae) {
			throw new BadRequestException("The given record is null.");
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("The record was not found by the given medical record number, vital sign type, username and date and time.");
		}catch(Exception e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
	}
	
	public RecordDto deleteRecord(String medicalRecordNumber, RecordToDelDto record) {
		
		try {
			Record recordToDelete = getRecord(medicalRecordNumber, record.getUsername(), record.getVitalSign(), record.getDateTime());
			this.recordRepository.delete(recordToDelete);
			return new RecordDto(recordToDelete);
		}catch(BadRequestException bre) {
			throw bre;
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("The record was not found by the given medical record number, vital sign type, username and date and time.");
		}catch(IllegalArgumentException iae) {
			throw new BadRequestException("The given record is null.");
		}catch(Exception e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
	}
	
}
