package com.vitalNow.model.dto;

import java.time.LocalDate;

import com.vitalNow.model.Patient;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description= "Patient schema to add a new patient.")
public class PatientToAddDto {
	
	@Schema(description="Patient's name", example="Carolina", requiredMode = RequiredMode.REQUIRED)
	private String name;
	
	@Schema(description="Patient's surname", example="López Márquez", requiredMode = RequiredMode.REQUIRED)
	private String surname;
	
	@Schema(description="Patient's birthday", example="1995-03-09", requiredMode = RequiredMode.REQUIRED)
	private LocalDate birthday;
	
	@Schema(description="Patient's gender", example="FEMALE", requiredMode = RequiredMode.REQUIRED)
	private String gender;
	
	//Aquí se añadirá el diagnóstico, mientras que PatientDto no devolverá el diagnóstico por defecto. 
		
	public PatientToAddDto() {
		super();
	}
	
	public PatientToAddDto(Patient patient) {
		this();
		this.name = patient.getName();
		this.surname = patient.getSurname();
		this.birthday = patient.getBirthday();
		this.gender = patient.getGender().getGenderSP();
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
	
}
