package com.vitalNow.model.dto;

import java.time.LocalDateTime;

import com.vitalNow.model.Record;

public class RecordToDelDto {
				
	private String username;
		
	private String vitalSign;
	
	private LocalDateTime dateTime;
	
	public RecordToDelDto() {
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
}
