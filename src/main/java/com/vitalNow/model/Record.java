package com.vitalNow.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.vitalNow.model.dto.RecordToAddDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Record")
@IdClass(RecordId.class)
public class Record {

	@Id
	@ManyToOne
	@JoinColumn(name="patient_recordNumber")
	private Patient patient;
	
	@Id
	@ManyToOne
	@JoinColumn(name="username")	
	private User user;
	
	@Id
	@ManyToOne
	@JoinColumn(name="vitalSign")
	private VitalSign vitalSign;
	
	@Id
	@Column(name="dateTime")
	private LocalDateTime dateTime;
	
	@Column(name="value")
	private Float value;
	
	@Column(name="alert")
	private boolean alert;
	
	
	public Record() {
		super();
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public VitalSign getVitalSign() {
		return vitalSign;
	}

	public void setVitalSign(VitalSign vitalSign) {
		this.vitalSign = vitalSign;
	}

	

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateTime, patient, user, vitalSign);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		return Objects.equals(dateTime, other.dateTime) && Objects.equals(patient, other.patient)
				&& Objects.equals(user, other.user) && Objects.equals(vitalSign, other.vitalSign);
	}

	
	
	

}
