package com.vitalNow.model.dto;

import java.util.HashSet;
import java.util.Set;

public class PatientRecordDto {
	
	private Integer medicalRecordNumber;
	
	private Set<RecordDto> records;

	public PatientRecordDto() {
		super();
		this.records = new HashSet<RecordDto>();
	}

	public Integer getMedicalRecordNumber() {
		return medicalRecordNumber;
	}

	public void setMedicalRecordNumber(Integer medicalRecordNumber) {
		this.medicalRecordNumber = medicalRecordNumber;
	}

	public Set<RecordDto> getRecords() {
		return records;
	}

	public void setRecords(Set<RecordDto> records) {
		this.records = records;
	}
	

}
