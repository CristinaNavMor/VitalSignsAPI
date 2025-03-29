package com.vitalNow.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vitalNow.exception.BadRequestException;
import com.vitalNow.exception.ElementNotFoundException;
import com.vitalNow.exception.ServiceUnavailableException;
import com.vitalNow.exception.UnauthorizedException;
import com.vitalNow.model.dto.RecordToAddDto;
import com.vitalNow.model.dto.RecordToDelDto;
import com.vitalNow.model.dto.RecordToPatchDto;
import com.vitalNow.services.AuthService;
import com.vitalNow.services.PatientService;
import com.vitalNow.services.RecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Records", description = "Patient's records related operations.")
@RestController
public class RecordController {

	@Autowired
	PatientService patientService;
	
	@Autowired
	RecordService recordService;
	
	@Autowired
	AuthService authService;
	
	//Paciente con registros -> registro de corazón con los datos de la última semana -> registros de otros con el último dato. 
	//Devuelve los registros del paciente, priorizando el heart rate y, si no, como principal, el que tenga el usuario.
	@Operation(summary="Get patient with records", description = "It return the pacient information, with the heart rate records of the week, and the last records registered of blood pressure, body temperature and oxygen saturation, if existed.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Response obtained successfully."),
		@ApiResponse(responseCode = "400", description = "Format error in patient's data."),
		@ApiResponse(responseCode = "404", description = "Not found. Patient was not found by the given medical record number."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@GetMapping("patient/{medicalRecordNumber}/records")
	public ResponseEntity<?> getPatientRecords(@Parameter(description = "Patient medical record number", example = "1", required = true)
												@PathVariable String medicalRecordNumber){
		try {
			return ResponseEntity.ok(this.patientService.getPatientWithRecords(medicalRecordNumber));
		}catch(BadRequestException br) {
			throw br;
		}catch(ElementNotFoundException en) {
			throw en;
		}catch(ServiceUnavailableException e) {
			throw e;
		}
	}
	
	
//	//Devolvemos exclusivamente el dato que se nos ha pedido al completo
//	@GetMapping("patient/{medicalRecordNumber}/records/{vitalSign}/{periodTime}")
//	public ResponseEntity<?> getPatientRecord(@PathVariable String medicalRecordNumber,
//												@PathVariable String vitalSign,
//												@PathVariable String periodTime){
//		
//	}

	
	//DUDA: ELIMINAR DEL TODO O DEJARLOS EN LA BBDD PERO AÑADIR UN ATRIBUTO QUE SEA "DE ALTA" O ALGO ASÍ? 
	//y SEGUIR DANDO LA OPCIÓN A ELIMINAR EL TODO PERO QUE SEA TIPO POR PROTECCIÓN DE DATOS? 
	@Operation(summary="Add a patient record", description = "Add a patient record. Only nurse or doctor roles can.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Response obtained successfully."),
		@ApiResponse(responseCode = "400", description = "Format error in patient's data."),
		@ApiResponse(responseCode = "404", description = "Not found. Patient was not found by the given medical record number."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PostMapping("patient/{medicalRecordNumber}/record/add")
	public ResponseEntity<?> addRecord(@Parameter(description = "Patient medical record number", example = "1", required = true)
										@PathVariable String medicalRecordNumber, 
										@RequestBody RecordToAddDto recordToAdd){
		
		try {
			return ResponseEntity.ok(this.recordService.addRecord(medicalRecordNumber, recordToAdd));
		}catch(BadRequestException bre) {
			throw bre;
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(ServiceUnavailableException e) {
			throw e;
		}
	}
	
	
	//Aquí no tendría sentido dejar que otro usuario modifique la información.
	//Tampoco tendría sentido que se modifique quién tomó la medida o se modifique la fecha o el tipo.
	//Sólo le veo sentido a que cambie el record. 
//	@PutMapping("patient/{medicalRecordNumber}/record/edit/{username}/{vitalSign}/{dateTime}")
//	public ResponseEntity<?> putRecord(@PathVariable String medicalRecordNumber,
//										@PathVariable String username,
//										@PathVariable String vitalSign,
//										@PathVariable String dateTime){
//		if(!this.authService.isSameUser(username)) {
//			throw new UnauthorizedException("The user logged is not the same user that create the record.");
//		}
//		try {
//			return ResponseEntity.ok(this.patientService.getPatientWithRecords(medicalRecordNumber));
//		}catch(ServiceUnavailableException e) {
//			throw e;
//		}
//	}
	
	@Operation(summary="Change a patient's record value.", description = "Change the record value. Only the same doctor or nurse that registered the record can change it.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Response obtained successfully."),
		@ApiResponse(responseCode = "400", description = "Format error in record's data."),
		@ApiResponse(responseCode = "404", description = "Not found. Record was not found by the given medical record number, username, vital sign type and date time."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PatchMapping("patient/{medicalRecordNumber}/record/edit")
	public ResponseEntity<?> patchRecord(@PathVariable String medicalRecordNumber,
										@RequestBody RecordToPatchDto record){
		if(!this.authService.isSameUser(record.getUsername())) {
			throw new UnauthorizedException("The user logged is not the same user that create the record.");
		}
		try {
			return ResponseEntity.ok(this.recordService.patchRecord(medicalRecordNumber, record));
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(BadRequestException bre) {
			throw bre;
		}catch(ServiceUnavailableException e) {
			throw e;
		}
	}
	
	//Ver si más que eliminar un registro, mandarlo a una tabla nueva de "antiguos registros". Igual con los cambios de valores.
	
	@Operation(summary="Delete a record.", description = "Delete a record. Only the same doctor or nurse that registered the record can change it.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Response obtained successfully."),
		@ApiResponse(responseCode = "400", description = "Format error in record's data."),
		@ApiResponse(responseCode = "404", description = "Not found. Record was not found by the given medical record number, username, vital sign type and date time."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@DeleteMapping("patient/{medicalRecordNumber}/record/delete")
	public ResponseEntity<?> deleteRecord(@PathVariable String medicalRecordNumber,
										@RequestBody RecordToDelDto record){
		if(!this.authService.isSameUser(record.getUsername())) {
			throw new UnauthorizedException("The user logged is not the same user that create the record.");
		}
		try {
			return ResponseEntity.ok(this.recordService.deleteRecord(medicalRecordNumber, record));
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(BadRequestException bre) {
			throw bre;
		}catch(ServiceUnavailableException e) {
			throw e;
		}
	}
	
}
