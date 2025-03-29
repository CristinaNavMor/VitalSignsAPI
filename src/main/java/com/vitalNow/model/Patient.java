package com.vitalNow.model;

import java.time.LocalDate;
import java.util.Objects;

import com.vitalNow.enums.Gender;
import com.vitalNow.model.dto.PatientToAddDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="Patient")
public class Patient {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "medical_record_number")
	private Integer medicalRecordNumber;
	
	@Column(name="name")
	@NotBlank
	@Size(min=3, message="Name can not be inferior to 3 characters.")
	private String name;
	
	@Column(name="surname")
	@NotBlank
	@Size(min=3, message="Surname can not be inferior to 3 characters.")
	private String surname;
	
	@Column(name="birthday")
	@NotNull
	private LocalDate birthday;
	
	@Column(name="gender")
	@Enumerated(value=EnumType.STRING)
	@NotNull(message="Role null or not valid. It must be 'FEMALE', 'MALE' or 'NON_BINARY'.")
	private Gender gender;
	
	
	public Patient() {
		super();
	}
	
	public Patient(PatientToAddDto patientToAdd) {
		this.name=patientToAdd.getName();
		this.surname = patientToAdd.getSurname();
		this.birthday = patientToAdd.getBirthday();
		this.gender = Gender.getGenderFromString(patientToAdd.getGender());
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

	@Override
	public int hashCode() {
		return Objects.hash(medicalRecordNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		return Objects.equals(medicalRecordNumber, other.medicalRecordNumber);
	}	
	
}
