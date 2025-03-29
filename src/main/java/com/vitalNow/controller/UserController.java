package com.vitalNow.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vitalNow.exception.BadRequestException;
import com.vitalNow.exception.ConflictException;
import com.vitalNow.exception.DisabledOrLockedAccountException;
import com.vitalNow.exception.ElementNotFoundException;
import com.vitalNow.exception.InternalServerErrorException;
import com.vitalNow.exception.ServiceUnavailableException;
import com.vitalNow.exception.UnauthorizedException;
import com.vitalNow.model.Credential;
import com.vitalNow.model.User;
import com.vitalNow.model.dto.UserChangePasswordDto;
import com.vitalNow.model.dto.UserDto;
import com.vitalNow.model.dto.UserDtoSave;
import com.vitalNow.model.dto.UserToAddDto;
import com.vitalNow.security.TokenUtils;
import com.vitalNow.services.AuthService;
import com.vitalNow.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Users", description = "Users related operations.")
@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired 
	TokenUtils tokenUtils;
	
	@Autowired
	AuthenticationManager authenticationMaganer;
	
	@Autowired
	AuthService authService;
	
	//getUsers
	@Operation(summary="Get a user by the username", description = "It returns a user by the username.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "User found."),
		@ApiResponse(responseCode = "400", description = "Format error in user's data."),
		@ApiResponse(responseCode = "404", description = "Not found. User was not found by the given username."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@GetMapping("/user/{username}")
	public ResponseEntity<?> getUser(@Parameter(description = "User's username", example = "marlopgon369", required = true)
									@PathVariable String username) {
		if(!this.authService.isSameUserOrAdmin(username)) {
			throw new UnauthorizedException("The user logged in has no permission to see other users information.");
		}
		try {
			return ResponseEntity.ok(this.userService.getUserByUsername(username));
		}catch(ElementNotFoundException enfe) {
			throw enfe;
		}catch(InternalServerErrorException ie) {
			throw ie;
		}
	}
	
	@Operation(summary="Get a list of users", description = "It returns a list with all the users.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Users found."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@GetMapping("/users")
	public ResponseEntity<?> getUserList() {
		try {
			return ResponseEntity.ok(this.userService.getUsers());
		}catch(InternalServerErrorException ie) {
			throw ie;
		}
		
	}
	
	/* AUTHENTICATION CONTROLLERS */
	
	@Operation(summary="Login", description = "Login")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Login successfull."),
		@ApiResponse(responseCode = "401", description = "Unauthorized. Invalid user credentials."),
		@ApiResponse(responseCode = "403", description = "Forbbiden. Account disabled or locked."),
		@ApiResponse(responseCode = "404", description = "Not found. User was not found by the given username."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Credential credential){
		
		Authentication authentication;
		try {
			authentication = this.authenticationMaganer
					.authenticate(new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword()));
			User user = (User) authentication.getPrincipal();
			String token = TokenUtils.generateToken(user.getEmail(), user.getUsername(), user.getRole().toString());
			Map<String, String> response = new HashMap<String, String>();
			response.put("message", "Successfull login.");
			response.put("token", token);
			return ResponseEntity.ok(response);						
		}catch(BadCredentialsException bce) {
			throw new UnauthorizedException("Invalid user credentials.");
		}catch(DisabledException de) {
			throw new DisabledOrLockedAccountException("The account is disabled. Makes no assertion as to whether or not the credentials were valid. ");
		}catch(LockedException le) {
			throw new DisabledOrLockedAccountException("Authentication request is rejected because the account is locked. Makes no assertion as to whether or not the credentials were valid.");
		}catch(Exception e) {
			throw new ServiceUnavailableException("Authentication error: "+e.getMessage());
		}
	}
	
	
	@Operation(summary="Register a new user.", description = "It registered a new user and returns it. Only an admin user can do it.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "User found."),
		@ApiResponse(responseCode = "400", description = "Format error in user's data."),
		@ApiResponse(responseCode = "409", description = "Conflict. Other user is modifying the user's data."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody UserToAddDto userToAdd){
		
		try {
			UserDto userAdded = this.userService.addUser(userToAdd); 
			return ResponseEntity.ok(userAdded);
		}catch(ConflictException ce) {
			throw ce;
		}catch(BadRequestException bre) {
			throw bre;
		}catch(ServiceUnavailableException sue) {
			throw sue;
		}catch(Exception e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
	}
	
	@Operation(summary="Change user's password", description = "It returns a user by the username.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Password changed successfully."),
		@ApiResponse(responseCode = "400", description = "Format error in user's data."),
		@ApiResponse(responseCode = "401", description = "Unauthorized. The user authenticated has no permission to change other user's password."),
		@ApiResponse(responseCode = "404", description = "Not found. User was not found by the given username."),
		@ApiResponse(responseCode = "409", description = "Conflict. Other user is modifying the user's data."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PostMapping("user/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody UserChangePasswordDto userPassword){
		//Ver si permitir que admin cambie contraseñas o sólo lo haga. Hacer un cambiar contraseña url
		if(!this.authService.isSameUserOrAdmin(userPassword.getUsername())) {
			throw new UnauthorizedException("The user logged in has no permission to edit other users information.");
		}
		try {
			Map<String, String> response = this.userService.changePassword(userPassword);
			return ResponseEntity.ok(response);
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(OptimisticLockingFailureException olfe) {
			throw olfe;
		}catch(BadRequestException bre) {
			throw bre;
		}catch(InternalServerErrorException ise) {
			throw ise;
		}
	}
	
	@Operation(summary="Put user", description = "Change user's information. All properties must be sent.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "User found."),
		@ApiResponse(responseCode = "400", description = "Format error in user's data."),
		@ApiResponse(responseCode = "401", description = "Unauthorized. The user authenticated has no permission to change other user's information."),
		@ApiResponse(responseCode = "404", description = "Not found. User was not found by the given username."),
		@ApiResponse(responseCode = "409", description = "Conflict. Other user is modifying the user's data."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PutMapping("user/{username}")
	public ResponseEntity<?> putUser(@Parameter(description = "User's username", example = "marlopgon369", required = true)
									@PathVariable String username, 
									@RequestBody UserDtoSave userEdited){
		if(!this.authService.isSameUserOrAdmin(username)) {
			throw new UnauthorizedException("The user logged in has no permission to edit other users information.");
		}
		
		try {
			return ResponseEntity.ok(this.userService.putUser(username, userEdited, false));
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(OptimisticLockingFailureException olfe) {
			throw olfe;
		}catch(BadRequestException bre) {
			throw bre;
		}catch(InternalServerErrorException ise) {
			throw ise;
		}
	}
	
	@Operation(summary="Patch user", description = "Change user's information. Not all properties must be sent.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "User found."),
		@ApiResponse(responseCode = "400", description = "Format error in user's data."),
		@ApiResponse(responseCode = "401", description = "Unauthorized. The user authenticated has no permission to change other user's information."),
		@ApiResponse(responseCode = "404", description = "Not found. User was not found by the given username."),
		@ApiResponse(responseCode = "409", description = "Conflict. Other user is modifying the user's data."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PatchMapping("user/{username}")
	public ResponseEntity<?> patchUser(@Parameter(description = "User's username", example = "marlopgon369", required = true)
										@PathVariable String username, 
										@RequestBody UserDtoSave userEdited){
		if(!this.authService.isSameUserOrAdmin(username)) {
			throw new UnauthorizedException("The user logged in has no permission to edit other users information.");
		}
		try {
			return ResponseEntity.ok(this.userService.patchUser(username, userEdited, false));
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(OptimisticLockingFailureException olfe) {
			throw olfe;
		}catch(BadRequestException bre) {
			throw bre;
		}catch(InternalServerErrorException ise) {
			throw ise;
		}
	}
	
	@Operation(summary="Put user by an administrator", description = "Change user's information. All properties must be sent. Only users with ADMIN role can access this path.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "User found."),
		@ApiResponse(responseCode = "400", description = "Format error in user's data."),
		@ApiResponse(responseCode = "401", description = "Unauthorized. The user authenticated has no permission to change other user's information."),
		@ApiResponse(responseCode = "404", description = "Not found. User was not found by the given username."),
		@ApiResponse(responseCode = "409", description = "Conflict. Other user is modifying the user's data."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PutMapping("user/admin/{username}")
	public ResponseEntity<?> putUserByAdmin(@PathVariable String username, @RequestBody UserDtoSave userEdited){
		try {
			return ResponseEntity.ok(this.userService.putUser(username, userEdited, true));
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(OptimisticLockingFailureException olfe) {
			throw olfe;
		}catch(BadRequestException bre) {
			throw bre;
		}catch(InternalServerErrorException ise) {
			throw ise;
		}
	}
	
	@Operation(summary="Patch user by an administrator", description = "Change user's information. Not all properties must be sent. Only users with ADMIN role can access this path.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "User found."),
		@ApiResponse(responseCode = "400", description = "Format error in user's data."),
		@ApiResponse(responseCode = "404", description = "Not found. User was not found by the given username."),
		@ApiResponse(responseCode = "409", description = "Conflict. Other user is modifying the user's data."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@PatchMapping("user/admin/{username}")
	public ResponseEntity<?> patchUserByAdmin(@Parameter(description = "User's username", example = "marlopgon369", required = true)
												@PathVariable String username, 
												@RequestBody UserDtoSave userEdited){
		try {
			return ResponseEntity.ok(this.userService.patchUser(username, userEdited, true));
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(OptimisticLockingFailureException olfe) {
			throw olfe;
		}catch(BadRequestException bre) {
			throw bre;
		}catch(InternalServerErrorException ise) {
			throw ise;
		}
	}
	
	@Operation(summary="Delete user", description = "Delete an user. Only users with ADMIN role can access this path.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "User found."),
		@ApiResponse(responseCode = "400", description = "Format error in the given username."),
		@ApiResponse(responseCode = "404", description = "Not found. User was not found by the given username."),
		@ApiResponse(responseCode = "409", description = "Conflict. Other user is modifying the user's data."),
		@ApiResponse(responseCode = "503", description = "Service unavailable.")
	})
	@DeleteMapping("user/admin/{username}")
	public ResponseEntity<?> deleteUser(@Parameter(description = "User's username", example = "marlopgon369", required = true)
										@PathVariable String username){
		try {
			return ResponseEntity.ok(this.userService.deleteUser(username));
		}catch(ElementNotFoundException enf) {
			throw enf;
		}catch(BadRequestException be) {
			throw be;
		}catch(OptimisticLockingFailureException olfe) {
			throw olfe;
		}catch(InternalServerErrorException ise) {
			throw ise;
		}
	}	
	
}
