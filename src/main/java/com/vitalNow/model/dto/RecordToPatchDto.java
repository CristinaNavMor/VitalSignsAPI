package com.vitalNow.model.dto;

import java.time.LocalDateTime;


public class RecordToPatchDto {
				
	private String username;
		
	private String vitalSign;
	
	private LocalDateTime dateTime;
	
	private Float value;
	
	public RecordToPatchDto() {
		super();
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVitalSign() {
		return vitalSign;
	}

	public void setVitalSign(String vitalSign) {
		this.vitalSign = vitalSign;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}



	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
}
