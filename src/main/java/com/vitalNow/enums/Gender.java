package com.vitalNow.enums;

public enum Gender {
	MALE("MALE", "Male", "Masculino"), 
	FEMALE("FEMALE", "Female", "Femenino"), 
	NON_BINARY("NON_BINARY", "Non binary", "No binario");
	
	
	private final String gender;
	private final String genderEN;
	private final String genderSP;

	
	Gender (String gender, String genderEN, String genderSP){
		this.gender=gender;
		this.genderEN = genderEN;
		this.genderSP = genderSP;
	}

	public String getGender() {
		return gender;
	}
	
	public String getGenderSP() {
		return genderSP;
	}
	public static Gender getGenderFromString(String strGender) {
		Gender gender = null; 
		if(strGender!=null && strGender.length()>0) {
			for(Gender g : Gender.values()){
				if(g.toString().equalsIgnoreCase(strGender.strip())) {
					gender = g;
				}
			}			
		}
		return gender;
	}
	public static boolean isValidGender(String strGender) {
		return strGender.equalsIgnoreCase(Gender.MALE.toString()) ||
				strGender.equalsIgnoreCase(Gender.FEMALE.toString()) ||
				strGender.equalsIgnoreCase(Gender.NON_BINARY.toString());
	}
}
