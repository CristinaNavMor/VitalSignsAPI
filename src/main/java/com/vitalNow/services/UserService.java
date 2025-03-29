package com.vitalNow.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vitalNow.enums.Role;
import com.vitalNow.exception.BadCredentialException;
import com.vitalNow.exception.BadRequestException;
import com.vitalNow.exception.ConflictException;
import com.vitalNow.exception.ElementNotFoundException;
import com.vitalNow.exception.InternalServerErrorException;
import com.vitalNow.exception.ServiceUnavailableException;
import com.vitalNow.exception.UserException;
import com.vitalNow.model.User;
import com.vitalNow.model.dto.UserChangePasswordDto;
import com.vitalNow.model.dto.UserDto;
import com.vitalNow.model.dto.UserDtoSave;
import com.vitalNow.model.dto.UserToAddDto;
import com.vitalNow.repository.UserRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class UserService implements UserDetailsService {

	
	// MÉTODO PARA VALIDAR LA CONTRASEÑA
	
	//Método para generar username aletaorio de usuario.
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private AuthService authService;
	
	/**
	 * This method returns the user found or an ElementNotFoundException if it is not found in the database.
	 * @param
	 * @throws ElementNotFoundException
	 * */
	public UserDto getUserByUsername(String username) throws ElementNotFoundException, InternalServerErrorException {
		try {
			Optional<User> optUser = this.userRepository.findByUsername(username);	
			UserDto user = new UserDto(optUser.orElseThrow());		
			return user;
		}catch(NoSuchElementException nsee) {
			throw new ElementNotFoundException("El nombre de usuario "+username+" no se encuentra en la base de datos.");
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}
	
	public List<UserDto> getUsers() throws ServiceUnavailableException{
		try {
			List<User> usersList = this.userRepository.findAll();	
			
			List<UserDto> usersListToReturn = usersList.stream().map((user)-> new UserDto(user)).toList();
			return usersListToReturn;
			
		}catch(Exception e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
	}
	
	private String getPartialNameForUsername(String partialName) {
		String response = "";
		if(partialName.length()>=3) {
			response = StringUtils.stripAccents(partialName.toLowerCase().substring(0, 3));
		}else {
			response = StringUtils.stripAccents(partialName.toLowerCase().substring(0, partialName.length()));
		}
		return response;
	}
	
	/**
	 * This method generates an username based on the first three chars of the name, the first three chars of the first surname, the first three chars
	 * of the second surname or the role (if the user has no second surname), and three random numbers.
	 * @throws UserException 
	 * */
	private String generateUsername(String name, String firstSurname, String role, String secondSurname) throws BadRequestException {
		if((name==null || name.length()==0) || (firstSurname ==null || firstSurname.length()==0) || role ==null) {
			throw new BadRequestException("Name, first surname or role are null or empty.");
		}
		StringBuilder username = new StringBuilder();
		
		//We add the name and first surname, removing accents.
		username.append(getPartialNameForUsername(name))
				.append(getPartialNameForUsername(firstSurname));
		
		//We add the second surname if exists or the role otherwise
		if(secondSurname==null || secondSurname.length()==0) {
			username.append(role.toLowerCase().substring(0,3));
		}else {
			username.append(getPartialNameForUsername(secondSurname));
		}
		//We add three random numbers. We validate if the username is not already in the database. If it is already registered, we swich the three random numbers 
		username.append(ThreadLocalRandom.current().nextInt(100, 1000));

		while(this.userRepository.existsByUsername(username.toString())) {
			Integer newNumber = ThreadLocalRandom.current().nextInt(100, 1000);
			username.replace(username.length()-3, username.length(), newNumber.toString());
		}
		
		return username.toString();
	}

	
	private String encodePassword(String password) {
		return this.bCryptPasswordEncoder.encode(password);
	}
	
	private String generateNewPassword() {
		StringBuilder sb = new StringBuilder("");
		
		String[] validChars = {"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "abcdefghijklmnopqrstuvwxyz", "0123456789", "@#$%^&*!?"};
		
		sb.append(validChars[0].charAt(ThreadLocalRandom.current().nextInt(0, 27)));
		sb.append(validChars[1].charAt(ThreadLocalRandom.current().nextInt(0, 27)));
		sb.append(validChars[2].charAt(ThreadLocalRandom.current().nextInt(0, 10)));
		sb.append(validChars[3].charAt(ThreadLocalRandom.current().nextInt(0, 10)));

		
		for(int i =0; i<6; i++) {
			int validCharPosition = ThreadLocalRandom.current().nextInt(0, 4);
			sb.append(validChars[validCharPosition].charAt(ThreadLocalRandom.current().nextInt(0, validChars[validCharPosition].length())));
			
		}
		return encodePassword(sb.toString());
	}	
	//La contraseña debe tener entre 8 y 20 caracteres, incluir una mayúscula, una minúscula, un número y un carácter especial.
	
	private boolean validatePassword(String password) {
		if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&*!?])[A-Za-z\\d@#$%^&*!?]{8,20}$")){
			throw new BadRequestException("The password must be between 8 and 20 characters, include an uppercase letter, a lowercase letter, a number, and a special character.");
		}
		return true;
	}
	
	
//	private String validateUserToAdd(UserDtoAdd userToAdd) {
//		String response = "";
//		
//		if(userToAdd.getName().isBlank()) {
//			response="Name is empty or null. \n";
//		}if(userToAdd.getFirstSurname().isBlank()) {
//			response +=" Surname is empty or null. \n";
//		}
//		if(userToAdd.getEmail().isBlank()) {
//			response +=" Email is empty or null. \n";
//		}else if(this.userRepository.existsByEmail(userToAdd.getEmail())) {
//			response +=" Email already registered. \n";
//		}
//		
//		if(userToAdd.getRole().isBlank()) {
//			response +=" Role is empty or null. \n";
//		}else if(Role.getRoleFromString(userToAdd.getRole())==null) {
//			response +=" Invalid role. \n";
//		}
//		return response;
//	}
	
	public String getErrorMessages(Set<ConstraintViolation<User>> violations) {
		String errorMsg = "";
		for(ConstraintViolation<User> violation : violations) {
			errorMsg+=violation.getMessage()+"\n";
		}
		return errorMsg;
	}
	
	
	/**
	 * This method receives a role, an UserDtoSave object and an User. It will set the user title ("Admin.", "Enf.", "Dr." or "Dra."). If it is a Doctor, the title in the UserDto must be present and must be valid ("Dr." or "Dra."). If it is not present, it will throw an
	 * BadRequestException. 
	 * @param
	 * @throws
	 * */
	private User setTitle(Role role, UserDtoSave userDto, User user) throws BadRequestException{
		if(role.equals(Role.ADMIN) || role.equals(Role.NURSE)) {
			user.setTitle(role.getMaleTitleES());
		}else {		
			String userDtoTitle = userDto.getTitle();
			if(userDtoTitle==null || (!userDtoTitle.equalsIgnoreCase(Role.DOCTOR.getFemaleTitleES()) && !userDtoTitle.equalsIgnoreCase(Role.DOCTOR.getMaleTitleES()))) {
				throw new BadRequestException("Doctor title is not correct. Must be ''Dr.'' or ''Dra.''.");
			}
			String title = userDtoTitle.equalsIgnoreCase(Role.DOCTOR.getFemaleTitleES()) ? Role.DOCTOR.getFemaleTitleES() : Role.DOCTOR.getMaleTitleES();
			user.setTitle(title);
		}
		return user;
	}
	
	//ESTA FUNCIÓN SERÁ ELIMINADA cuando se implemente el correo. UserToSaveDto será elimiinado y tanto editar como añadir será con UserDtoSave
	private User setTitle(Role role, UserToAddDto userDto, User user) throws BadRequestException{
		if(role.equals(Role.ADMIN) || role.equals(Role.NURSE)) {
			user.setTitle(role.getMaleTitleES());
		}else {		
			String userDtoTitle = userDto.getTitle();
			if(userDtoTitle==null || (!userDtoTitle.equalsIgnoreCase(Role.DOCTOR.getFemaleTitleES()) && !userDtoTitle.equalsIgnoreCase(Role.DOCTOR.getMaleTitleES()))) {
				throw new BadRequestException("Doctor title is not correct. Must be ''Dr.'' or ''Dra.''.");
			}
			String title = userDtoTitle.equalsIgnoreCase(Role.DOCTOR.getFemaleTitleES()) ? Role.DOCTOR.getFemaleTitleES() : Role.DOCTOR.getMaleTitleES();
			user.setTitle(title);
		}
		return user;
	}
	
	/**
	 * @param
	 * @throws 
	 * */
	public UserDto addUser(UserToAddDto userToAdd) throws ConflictException, BadRequestException, ServiceUnavailableException {
//		try {
//			UserDtoSave.isFormatValid(userToAdd);
//		}catch(BadRequestException bre) {
//			throw bre;
//		}
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		if (userToAdd==null) {
			throw new BadRequestException("The sent user is null.");
		}
		
		if(this.userRepository.existsByEmail(userToAdd.getEmail())) {
			throw new ConflictException("The email is already in use.");
		}
		
		//Agregamos un nuevo usuario
		User newUser = new User();
		
		Role role = Role.getRoleFromString(userToAdd.getRole());
		newUser.setRole(role);
		//Validaciones internas no recogidas en la clase POJO, y susceptibles de lanzar excepciones: 
		try {
			newUser.setUsername(this.generateUsername(userToAdd.getName(), userToAdd.getFirstSurname(), userToAdd.getRole(),userToAdd.getSecondSurname()));
			if(role!=null) this.setTitle(role, userToAdd, newUser);	
			validatePassword(userToAdd.getPassword());
		}catch(BadRequestException bre) {
			throw bre;
		}
		
		newUser.setName(userToAdd.getName());
		newUser.setFirstSurname(userToAdd.getFirstSurname());
		newUser.setSecondSurname(userToAdd.getSecondSurname() != null ? userToAdd.getSecondSurname() : "");
		newUser.setEmail(userToAdd.getEmail());
		//INICIALMENTE DEJAREMOS EL MÉTODO DE LA CONTRASEÑA COMENTADA
		//CUANDO ESTÉ TODO CORRECTO, CONFIGURAR ENVIAR LA CONTRASEÑA AL EMAIL Y DESCOMENTAMOS ESTA PARTE DE INICIAR LA CONTRASEÑA
		//newUser.setPassword(generateNewPassword());
		newUser.setPassword(userToAdd.getPassword());

		//Validamos y mandamos error personalizado según los errores cometidos. 
		Set<ConstraintViolation<User>> violations = validator.validate(newUser);
		if(!violations.isEmpty()) {
			throw new BadRequestException(getErrorMessages(violations));
		}
		//Encriptamos la contraseña
		newUser.setPassword(encodePassword(newUser.getPassword()));
		//Enviar correo.
		
		//Guardamos el usuario
		//No controlamos las excepciones porque si hay llegado aquí se ha validado que no sea nulo, y que el email no existe ya, ni el username, y no se puede estar modificando algo que todavía no se ha creado. 
		try {
			newUser = this.userRepository.save(newUser);			
		}catch(Exception e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		
		return new UserDto(newUser);
	}
	
	public Map<String, String> changePassword(UserChangePasswordDto userPasswordDto) throws ElementNotFoundException, BadCredentialException, InternalServerErrorException {
		try {
			
			User user = this.userRepository.findByUsername(userPasswordDto.getUsername()).orElseThrow();
			if (!bCryptPasswordEncoder.matches(userPasswordDto.getOldPassword(), user.getPassword())) throw new BadCredentialException("Incorrect password");
			user.setPassword(bCryptPasswordEncoder.encode(userPasswordDto.getNewPassword()));
			user.setPasswordChanged(true);
			
			this.userRepository.save(user);
			
			Map<String, String> response = new HashMap<String, String>();
			response.put("message", "Password changed successfully.");
			return response; 
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("No user was found with username ''"+userPasswordDto.getUsername()+"''");
		}catch(OptimisticLockingFailureException olf) {
			throw new OptimisticLockingFailureException("The same user is being modified by other user.");
		}catch(BadCredentialException bce) {
			throw bce;
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage()); 
		}
	}
	
	
	/**
	 * @param
	 * @throws
	 * */
	public UserDto putUser (String username, UserDtoSave userEdited, boolean isAdmin) throws ElementNotFoundException, OptimisticLockingFailureException, InternalServerErrorException, BadRequestException {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		try {
			User userToEdit = this.userRepository.findByUsername(username).orElseThrow();
			userToEdit.setName(userEdited.getName());
			userToEdit.setFirstSurname(userEdited.getFirstSurname());
			userToEdit.setSecondSurname(userEdited.getSecondSurname() != null ? userEdited.getSecondSurname() : "");
			userToEdit.setEmail(userEdited.getEmail());
			
			if(isAdmin) {
				userToEdit.setRole(Role.getRoleFromString(userEdited.getRole()));				
			}
			
			Set<ConstraintViolation<User>> violations = validator.validate(userToEdit);
			if(!violations.isEmpty()) {
				throw new BadRequestException(getErrorMessages(violations));
			}
			return new UserDto(this.userRepository.save(userToEdit));
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("No user was found with username ''"+username+"''");
		}catch(OptimisticLockingFailureException olf) {
			throw new OptimisticLockingFailureException("The same user is being modified by other user.");
		}catch (BadRequestException bre) {
			throw bre;
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}
	
	private boolean updateValue(String previousValue, String newValue) {
		return newValue!=null && !previousValue.equals(newValue);
	}

	
	public UserDto patchUser(String username, UserDtoSave userEdited, boolean isAdmin) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		try {
			User userToEdit = this.userRepository.findByUsername(username).orElseThrow();
			
			if(updateValue(userToEdit.getName(), userEdited.getName())) {
				userToEdit.setName(userEdited.getName());
			}
			if(updateValue(userToEdit.getFirstSurname(), userEdited.getFirstSurname())) {
				userToEdit.setFirstSurname(userEdited.getFirstSurname());
			}
			if(updateValue(userToEdit.getFirstSurname(), userEdited.getSecondSurname())) {
				userToEdit.setSecondSurname(userEdited.getSecondSurname());
			}
			if(updateValue(userToEdit.getEmail(), userEdited.getEmail())) {
				userToEdit.setSecondSurname(userEdited.getEmail());
			}
			if(isAdmin) {
				String strNewRole = userEdited.getRole();
				//Vemos si se ha modificado el valor
				if(updateValue(userToEdit.getRole().toString(), strNewRole)) {
					//Vemos si el valor es válido o, en caso contrario, lanzamos excepción.
					if(Role.getRoleFromString(strNewRole)==null) {
						throw new BadRequestException("Role value not valid. Must be ''NURSE'', ''DOCTOR'' or ''ADMIN''.");
					}
					userToEdit.setRole(Role.getRoleFromString(strNewRole));
				}
			}			
			Set<ConstraintViolation<User>> violations = validator.validate(userToEdit);
			if(!violations.isEmpty()) {
				throw new BadRequestException(getErrorMessages(violations));
			}
			return new UserDto(this.userRepository.save(userToEdit));
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("No user was found with username ''"+username+"''");
		}catch(OptimisticLockingFailureException olf) {
			throw new OptimisticLockingFailureException("The same user is being modified by other user.");
		}catch (BadRequestException bre) {
			throw bre;
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}

	/**
	 * @param
	 * @throws
	 * */
	public UserDto deleteUser(String username) throws ElementNotFoundException, BadRequestException, OptimisticLockingFailureException, InternalServerErrorException {
		//Ver si existe
		try {
			User userToDelete = this.userRepository.findByUsername(username).orElseThrow();
			this.userRepository.delete(userToDelete);
			return new UserDto(userToDelete);
		}catch(NoSuchElementException nse) {
			throw new ElementNotFoundException("No user was found with username ''"+username+"''");
		}catch(IllegalArgumentException iae) {
			throw new BadRequestException("The given username is null.");
		}catch(OptimisticLockingFailureException olfe) {
			throw new OptimisticLockingFailureException("The same user is being modified.");
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}
	

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			User user = this.userRepository.findByEmail(email).orElseThrow();			
			return user;
		}catch(NoSuchElementException nsee) {
			throw new ElementNotFoundException("User not found.");
		}
	}
	

}
