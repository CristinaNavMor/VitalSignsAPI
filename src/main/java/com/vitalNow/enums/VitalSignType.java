package com.vitalNow.enums;

public enum VitalSignType {
	
	//PONER AQUÍ EL NOMBRE, EL MÍNIMO Y EL MÁXIMO.
	
	BODY_TEMPERATURE("BODY_TEMPERATURE"), 
	BLOOD_PRESSURE("BLOOD_PRESSURE"), 
	HEART_RATE("HEART_RATE"), 
	OXYGEN_SATURATION("OXYGEN_SATURATION"), 
	BREATHING_RATE("RESPIRATORY_RATE");
	
	private final String vitalSign;
	
	VitalSignType(String vitalSign){
		this.vitalSign = vitalSign;
	}
	
	public String getVitalSign() {
		return this.vitalSign;
	}
	
	public static VitalSignType getVitalSignFromString(String strVitalSign) {
		VitalSignType vitalSign = null; 
		if(strVitalSign!=null && strVitalSign.length()>0) {
			for(VitalSignType v : VitalSignType.values()){
				if(v.toString().equalsIgnoreCase(strVitalSign.strip())) {
					vitalSign = v;
				}
			}			
		}
		return vitalSign;
	}
}


