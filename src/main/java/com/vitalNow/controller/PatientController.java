package com.vitalNow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vitalNow.exception.BadRequestException;
import com.vitalNow.exception.ElementNotFoundException;
import com.vitalNow.exception.InternalServerErrorException;
import com.vitalNow.exception.ServiceUnavailableException;
import com.vitalNow.model.dto.PatientToAddDto;
import com.vitalNow.model.dto.RecordToAddDto;
import com.vitalNow.services.PatientService;
import com.vitalNow.services.RecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Patients", description = "Patients related operations.")
@RestController
public class PatientController {

	@Autowired
	PatientService patientService;
	
	@Operation(summary="Patients list", description = "It can be obtained a patients' list without the records.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Request correct. Patients list sended. Can be empty."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@GetMapping("patients")
	public ResponseEntity<?> getPatients(){
		try {
			return ResponseEntity.ok(this.patientService.getPatients());
		}catch(ServiceUnavailableException e) {
			throw e;
		}
	}
	
	@Operation(summary="Get a patient by medical record number", description = "It returns a patient by the medical record number.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Patient found."),
		@ApiResponse(responseCode = "400", description = "Format error in patient's data."),
		@ApiResponse(responseCode = "404", description = "Not found. Patient was not found by the given medical record number."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@GetMapping("patient/{medicalRecordNumber}")
	public ResponseEntity<?> getPatient(@Parameter(description = "Patient medical record number", example = "1", required = true)
										@PathVariable String medicalRecordNumber){
		try {
			return ResponseEntity.ok(this.patientService.getPatient(medicalRecordNumber));
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(BadRequestException be) {
			throw be;
		}catch(ServiceUnavailableException e) {
			throw e;
		}
	}
	
	@Operation(summary="Save a new patient", description = "To register a new patient. It returns the new patient.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Patient regitered successfully."),
		@ApiResponse(responseCode = "400", description = "Format error in patient's data."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PostMapping("patient/register")
	public ResponseEntity<?> createPatient(@RequestBody PatientToAddDto patientDto){
		try {
			return ResponseEntity.ok(this.patientService.addPatient(patientDto));
		}catch(BadRequestException bre) {
			throw bre;
		}catch(ServiceUnavailableException sue) {
			throw sue;
		}
	}	
	
	@Operation(summary="Discharge a patient", description = "Discharge a patient. It returns the discharge patient.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Patient discharged successfully."),
		@ApiResponse(responseCode = "400", description = "Format error in patient's data."),
		@ApiResponse(responseCode = "404", description = "Not found. Patient was not found by the given medical record number."),
		@ApiResponse(responseCode = "409", description = "Conflict. Other user is modifying the patient's data."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@DeleteMapping("patient/{medicalRecordNumber}/discharge")
	public ResponseEntity<?> deletePatient(@Parameter(description = "Patient medical record number", example = "1", required = true)
											@PathVariable String medicalRecordNumber){
		try {
			return ResponseEntity.ok(this.patientService.dischargePatient(medicalRecordNumber));
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(BadRequestException be) {
			throw be;
		}catch(OptimisticLockingFailureException olfe) {
			throw olfe;
		}catch(InternalServerErrorException ise) {
			throw ise;
		}
	}
	
	@Operation(summary="Put patient", description = "Change user's information. All properties must be sent.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Patient saved successfully."),
		@ApiResponse(responseCode = "400", description = "Format error in patient's data."),
		@ApiResponse(responseCode = "401", description = "Unauthorized. The user authenticated has no permission to change patients information."),
		@ApiResponse(responseCode = "404", description = "Not found. Patient was not found by the given medical record number."),
		@ApiResponse(responseCode = "409", description = "Conflict. Other user is modifying the patient's data."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PutMapping("patient/{medicalRecordNumber}")
	public ResponseEntity<?> putPatient(@Parameter(description = "Patient medical record number", example = "1", required = true)
										@PathVariable String medicalRecordNumber, @RequestBody PatientToAddDto patientToEdit){
		try {
			return ResponseEntity.ok(this.patientService.putPatient(medicalRecordNumber, patientToEdit));
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(OptimisticLockingFailureException olfe) {
			throw olfe;
		}catch(BadRequestException bre) {
			throw bre;
		}catch(InternalServerErrorException ise) {
			throw ise;
		}
	}
	
	

	
}
