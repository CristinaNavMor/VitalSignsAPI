package com.vitalNow.model.dto;

import com.vitalNow.model.Record;

public class RecordToAddDto {
			
	private String vitalSign;
	
	private Float value;
	
	public RecordToAddDto() {
		super();
	}
	
	public RecordToAddDto(Record record) {
		this.vitalSign = record.getVitalSign().getName();
		this.value = record.getValue();
	}

	public String getVitalSign() {
		return vitalSign;
	}

	public void setVitalSign(String vitalSign) {
		this.vitalSign = vitalSign;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
}
