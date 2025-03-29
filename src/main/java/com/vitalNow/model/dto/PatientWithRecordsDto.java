package com.vitalNow.model.dto;

import com.vitalNow.model.Record;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import com.vitalNow.enums.Gender;
import com.vitalNow.model.Patient;

public class PatientWithRecordsDto {

	private Integer medicalRecordNumber;
	
	private String name;
	
	private String surname;
	
	private LocalDate birthday;
	
	private Integer age;
	
	private Gender gender;
	
	private Set<RecordDto> records;
	
	public PatientWithRecordsDto() {
		super();
		this.age = LocalDate.now().until(this.birthday).getYears();
		this.records = new TreeSet<RecordDto>();
	}
	
	public PatientWithRecordsDto(Patient patient) {
		this();
		this.medicalRecordNumber = patient.getMedicalRecordNumber();
		this.name = patient.getName();
		this.surname = patient.getSurname();
		this.birthday = patient.getBirthday();
		this.gender = patient.getGender();
//		this.records = getRecordDto(patient.getRecords());
	}
	
	private Set<RecordDto> getRecordDto(Set<Record> recordList) {
		Set<RecordDto> set = new TreeSet<RecordDto>();
		set.addAll(recordList.stream().map((record) -> new RecordDto(record)).toList());
		return set;
	}

	public Integer getMedicalRecordNumber() {
		return medicalRecordNumber;
	}

	public void setMedicalRecordNumber(Integer medicalRecordNumber) {
		this.medicalRecordNumber = medicalRecordNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Set<RecordDto> getRecords() {
		return records;
	}

	public void setRecords(Set<RecordDto> records) {
		this.records = records;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}	
	
}
