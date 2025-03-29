package com.vitalNow.model.dto;

import com.vitalNow.enums.Role;
import com.vitalNow.model.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description= "User general schema.")
public class UserDto {

	@Schema(description="User's username", example="manrodper567")
	private String username;
	
	@Schema(description="User's name", example="Manuel")
	private String name;
	
	@Schema(description="User's first surname", example="Rodríguez")
	private String firstSurname;
	
	@Schema(description="User's second surname", example="Pérez")
	private String secondSurname;
	
	@Schema(description="User's profesional title", example="Dr.")
	private String title;
	
	@Schema(description="User's email", example="manuel@hotmail.com")
	private String email;

	@Schema(description="User's role", example="DOCTOR")
	private Role role;
	
	public UserDto() {
		super();
	}
	
	public UserDto(User user) {
		this();
		this.username = user.getUsername();
		this.name = user.getName();
		this.firstSurname = user.getFirstSurname();
		this.secondSurname = user.getSecondSurname();
		this.email = user.getEmail();
		this.role = user.getRole();
		this.title = user.getTitle();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	

}
