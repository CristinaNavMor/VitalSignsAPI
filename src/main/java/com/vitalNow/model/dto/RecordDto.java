package com.vitalNow.model.dto;

import java.time.LocalDateTime;
import com.vitalNow.model.Record;

public class RecordDto {
			
	private String username;
	
	private String userNameWithTitle;
	
	private String vitalSign;
	
	private LocalDateTime dateTime;
	
	private Float value;
	
	private boolean alert;
	
	
	public RecordDto() {
		super();
	}
	
	public RecordDto(Record record) {
		this.username=record.getUser().getUsername();
		this.userNameWithTitle = record.getUser().getTitle() + " " + record.getUser().getFirstSurname() + " " + record.getUser().getSecondSurname();
		this.vitalSign = record.getVitalSign().getName();
		this.dateTime = record.getDateTime();
		this.value = record.getValue();
		this.alert = record.isAlert();
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserNameWithTitle() {
		return userNameWithTitle;
	}

	public void setUserNameWithTitle(String userNameWithTitle) {
		this.userNameWithTitle = userNameWithTitle;
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

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	

}
