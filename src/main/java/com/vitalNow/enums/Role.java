package com.vitalNow.enums;

public enum Role {
	ADMIN("ADMIN", "Admin.", "Admin."),
	DOCTOR("DOCTOR", "Dr.", "Dra."), 
	NURSE("NURSE", "Enf.", "Enf.");
	
	private final String role;
	private final String maleTitleES;
	private final String femaleTitleES;
		
	Role (String role, String maleTitleES, String femaleTitleES){
		this.role=role;
		this.maleTitleES = maleTitleES;
		this.femaleTitleES = femaleTitleES;
	}

	public String getRole() {
		return this.role;
	}
	
	public String getMaleTitleES() {
		return this.maleTitleES;
	}
	
	public String getFemaleTitleES() {
		return this.femaleTitleES;
	}
	
	public static Role getRoleFromString(String strRole) {
		Role role = null; 
		if(strRole!=null && strRole.length()>0) {
			for(Role r : Role.values()){
				if(r.toString().equalsIgnoreCase(strRole.strip())) {
					role = r;
				}
			}			
		}
		return role;
	}
	
}
