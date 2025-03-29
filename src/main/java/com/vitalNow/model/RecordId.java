package com.vitalNow.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class RecordId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer patient;

	private String user;

	private String vitalSign;

	private LocalDateTime dateTime;
	
	public RecordId() {
		super();
	}

	public Integer getPatient() {
		return patient;
	}

	public void setPatient(Integer patient) {
		this.patient = patient;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getVitalSign() {
		return vitalSign;
	}

	public void setVitalSign(String vitalSign) {
		this.vitalSign = vitalSign;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
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
		RecordId other = (RecordId) obj;
		return Objects.equals(dateTime, other.dateTime) && Objects.equals(patient, other.patient)
				&& Objects.equals(user, other.user) && Objects.equals(vitalSign, other.vitalSign);
	}

	
	
	

}
