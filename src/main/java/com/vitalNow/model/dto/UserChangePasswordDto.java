package com.vitalNow.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description= "User schema to change the user's password.")
public class UserChangePasswordDto {
	
	@Schema(description="User's username", example="manrodper567", requiredMode = RequiredMode.REQUIRED)
	private String username;
	
	@Schema(description="User's old password.", example="AveRapaz9?", requiredMode = RequiredMode.REQUIRED)
	private String oldPassword;
	
	@Schema(description="User's new password. The password must be between 8 and 20 characters, include an uppercase letter, a lowercase letter, a number, and a special character.", example="rApazAve4_", requiredMode = RequiredMode.REQUIRED)
	private String newPassword;

	
	public UserChangePasswordDto() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getOldPassword() {
		return oldPassword;
	}


	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}


	public String getNewPassword() {
		return newPassword;
	}


	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


}
