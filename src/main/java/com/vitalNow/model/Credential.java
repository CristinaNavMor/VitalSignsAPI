package com.vitalNow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description= "User's credential to do login.")
public class Credential {
	@Schema(description="User's email", example="manuel@hotmail.com", requiredMode = RequiredMode.REQUIRED)
	private String email;
	
	@Schema(description="User's password.", example="AveRapaz9?", requiredMode = RequiredMode.REQUIRED)
	private String password;
	
	
	public Credential() {
		super();
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
