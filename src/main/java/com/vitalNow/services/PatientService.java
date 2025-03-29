package com.vitalNow.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.vitalNow.enums.Gender;
import com.vitalNow.enums.VitalSignType;
import com.vitalNow.exception.BadRequestException;
import com.vitalNow.exception.ElementNotFoundException;
import com.vitalNow.exception.InternalServerErrorException;
import com.vitalNow.exception.ServiceUnavailableException;
import com.vitalNow.model.Patient;
import com.vitalNow.model.Record;
import com.vitalNow.model.User;
import com.vitalNow.model.VitalSign;
import com.vitalNow.model.dto.PatientDto;
import com.vitalNow.model.dto.PatientToAddDto;
import com.vitalNow.model.dto.RecordDto;
import com.vitalNow.model.dto.UserDto;
import com.vitalNow.repository.PatientRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class PatientService {

	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	RecordService recordService;
	
	@Autowired
	GeneralService generalService;
	
//	public Map<String, Object> getPatientsWithRecords() {		
//		try {
//			List<Patient> patientsList = this.patientRepository.findAll();	
//			
//			List<PatientWithRecordsDto> patientsListToReturn = patientsList.stream().map((patient)-> new PatientWithRecordsDto(patient)).toList();
//			return patientsListToReturn;
//			
//		}catch(Exception e) {
//			throw new ServiceUnavailableException(e.getMessage());
//		}
//	}
	
	
	public List<PatientDto> getPatients() {		
		try {
			List<Patient> patientsList = this.patientRepository.findAll();	
			
			List<PatientDto> patientsListToReturn = patientsList.stream().map((patient)-> new PatientDto(patient)).toList();
			return patientsListToReturn;
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new ServiceUnavailableException(e.getMessage());
		}
	}
	
	public PatientDto getPatient(String medicalRecordNumber) {
		//Comprobar que sea número
		try {
			Patient patient = this.patientRepository.findByMedicalRecordNumber(Integer.parseInt(medicalRecordNumber)).orElseThrow();
			return new PatientDto(patient);
		}catch(NumberFormatException nfe) {
			throw new BadRequestException("Invalid format for medical record number: Not a number.");
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("There could not be found a patient with medical record number "+medicalRecordNumber);
		}	
	}
	
	public PatientDto addPatient(PatientToAddDto patientToAdd) {
		
		if(!Gender.isValidGender(patientToAdd.getGender())) throw new BadRequestException("Gender format is not valid. Must be ''MALE'', ''FEMALE'' or ''NON_BINARY''"); 
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Patient newPatient = new Patient(patientToAdd);
		
		Set<ConstraintViolation<Patient>> violations = validator.validate(newPatient);
		
		if(!violations.isEmpty()) {
			throw new BadRequestException(generalService.getErrorMessages(violations));
		}
		try {
			return new PatientDto(this.patientRepository.save(newPatient));
		}catch(IllegalArgumentException iae) {
			throw new BadRequestException("The given patient is null.");
		}catch(Exception e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
	}
	
	public PatientDto putPatient(String medicalRecordNumber, PatientToAddDto patientToEdit) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		try {
			Patient patient = this.patientRepository.findByMedicalRecordNumber(Integer.parseInt(medicalRecordNumber)).orElseThrow();
			
			patient.setName(patientToEdit.getName());
			patient.setSurname(patientToEdit.getSurname());
			patient.setGender(Gender.getGenderFromString(patientToEdit.getGender()));
			patient.setBirthday(patientToEdit.getBirthday());
			
			Set<ConstraintViolation<Patient>> violations = validator.validate(patient);
			
			if(!violations.isEmpty()) {
				throw new BadRequestException(generalService.getErrorMessages(violations));
			}
			
			return new PatientDto(this.patientRepository.save(patient));
		}catch(NumberFormatException nfe) {
			throw new BadRequestException("Invalid format for medical record number: Not a number.");
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("No patient was found with the medical record number ''"+medicalRecordNumber+"''");
		}catch(IllegalArgumentException iae) {
			throw new BadRequestException("The given medical record number is null.");
		}catch(OptimisticLockingFailureException olfe) {
			throw new OptimisticLockingFailureException("The same patient is being modified.");
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}
	
	public PatientDto dischargePatient(String medicalRecordNumber) {
		try {
			Patient patientToDelete = this.patientRepository.findByMedicalRecordNumber(Integer.parseInt(medicalRecordNumber)).orElseThrow();
			this.patientRepository.delete(patientToDelete);
			return new PatientDto(patientToDelete);
		}catch(NumberFormatException nfe) {
			throw new BadRequestException("Invalid format for medical record number: Not a number.");
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("No patient was found with the medical record number ''"+medicalRecordNumber+"''");
		}catch(IllegalArgumentException iae) {
			throw new BadRequestException("The given medical record number is null.");
		}catch(OptimisticLockingFailureException olfe) {
			throw new OptimisticLockingFailureException("The same patient is being modified.");
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}


	//Cambiar el método en el futuro para que se muestre como registro principal uno que tenga el paciente, y no se devuelva un array vacío. 
	public Map<String, Object> getPatientWithRecords(String medicalRecordNumber){
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		try {
			Patient patient = this.patientRepository.findByMedicalRecordNumber(Integer.parseInt(medicalRecordNumber)).orElseThrow();
			response.put("patient", new PatientDto(patient));
			
			//Cogermos el compoleto 
			List<Record> records = this.recordService.getLastWeekRecords(medicalRecordNumber, VitalSignType.HEART_RATE.toString());
			List<RecordDto> recordsToResponse = records.stream().map((record) -> new RecordDto(record)).toList();
			response.put("mainRecord", recordsToResponse);
			
			response.put("secondaryRecords", new TreeMap<String, RecordDto>());
			
			String[] vitalSigns = {VitalSignType.BLOOD_PRESSURE.toString(), 
			                       VitalSignType.BREATHING_RATE.toString(), 
			                       VitalSignType.OXYGEN_SATURATION.toString(),
			                       VitalSignType.BODY_TEMPERATURE.toString()};
			
			for(String vitalSign : vitalSigns) {
				Record record = this.recordService.getLastRecord(medicalRecordNumber, vitalSign).orElse(null);
				if(record!=null) {
					TreeMap<String, RecordDto> secondaryRecords = (TreeMap<String, RecordDto>) response.get("secondaryRecords");
					secondaryRecords.put(vitalSign, new RecordDto(record));
					response.put("secondaryRecords", secondaryRecords);
				}
			}

		}catch(NumberFormatException nfe) {
			throw new BadRequestException("Invalid format for medical record number: Not a number.");
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("There could not be found a patient with medical record number "+medicalRecordNumber);
		}	
		
		return response;
	}

}
