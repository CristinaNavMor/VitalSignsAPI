package com.vitalNow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vitalNow.model.Patient;


public interface PatientRepository extends JpaRepository<Patient, Integer> {
	
	List<Patient> findAll();
	
	Optional<Patient> findByMedicalRecordNumber(Integer medicalRecordNumber);


}
