package com.vitalNow.model.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import com.vitalNow.enums.Gender;
import com.vitalNow.model.Patient;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description= "Patient general schema.")
public class PatientDto {
	@Schema(description="Patient's medical record number", example="1234")
	private Integer medicalRecordNumber;
	
	@Schema(description="Patient's name", example="Carolina")
	private String name;
	
	@Schema(description="Patient's surname", example="López Márquez")
	private String surname;
	
	@Schema(description="Patient's birthday", example="1995-03-09")
	private LocalDate birthday;
	
	@Schema(description="Patient's age", example="30")
	private Integer age;
	
	@Schema(description="Patient's gender", example="FEMALE")
	private String gender;
		
	public PatientDto() {
		super();
	}
	
	public PatientDto(Patient patient) {
		this();
		this.medicalRecordNumber = patient.getMedicalRecordNumber();
		this.name = patient.getName();
		this.surname = patient.getSurname();
		this.birthday = patient.getBirthday();
		this.gender = patient.getGender().getGenderSP();
		this.age = this.birthday.until(LocalDate.now()).getYears();
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}	
	
}
