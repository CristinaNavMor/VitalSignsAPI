package com.vitalNow.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description= "User schema to register a new user.")
public class UserToAddDto {
	
	@Schema(description="User's name", example="Manuel", requiredMode = RequiredMode.REQUIRED)
	private String name;
	
	@Schema(description="User's first surname", example="Rodríguez", requiredMode = RequiredMode.REQUIRED)
	private String firstSurname;
	
	@Schema(description="User's second surname", example="Pérez")
	private String secondSurname;
	
	@Schema(description="User's email", example="manuel@hotmail.com", requiredMode = RequiredMode.REQUIRED)
	private String email;
	
	@Schema(description="User's role", example="DOCTOR", allowableValues = {"DOCTOR, NURSE, ADMIN"}, requiredMode = RequiredMode.REQUIRED)
	private String role;
	
	@Schema(description="User's profesional title. It is required if the role is ''DOCTOR''.", example="Dr.", allowableValues = {"Dr.", "Dra."})
	private String title;
	
	@Schema(description="User's password. The password must be between 8 and 20 characters, include an uppercase letter, a lowercase letter, a number, and a special character.", example="AveRapaz9?", requiredMode = RequiredMode.REQUIRED)
	private String password;

	
	public UserToAddDto() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstSurname() {
		return firstSurname;
	}

	public void setFirstSurname(String firstSurname) {
		this.firstSurname = firstSurname;
	}

	public String getSecondSurname() {
		return secondSurname;
	}

	public void setSecondSurname(String secondSurname) {
		this.secondSurname = secondSurname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
