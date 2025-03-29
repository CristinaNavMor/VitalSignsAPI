package com.vitalNow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vitalNow.enums.Role;
import com.vitalNow.model.User;
import com.vitalNow.repository.UserRepository;


@Service
public class AuthService {
	
	@Autowired
	UserRepository userRepository;

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	private boolean roleMatches(Role role) {
		Authentication authentication = getAuthentication();
		return authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(role.toString())); 
	}
	
	public String getUsernameFromToken() {
		Authentication authentication = getAuthentication();
		return (String) authentication.getDetails(); 
	}
	
	public User getUserLogged() {
		return this.userRepository.findByUsername(getUsernameFromToken()).orElse(null);
	}
	
    public boolean isAdmin() {
    	return this.roleMatches(Role.ADMIN);    	
    }
    
    public boolean isNurse() {
    	return this.roleMatches(Role.NURSE);  
    }
    
    public boolean isDoctor() {
    	return this.roleMatches(Role.DOCTOR);
    }
    
    public boolean isDoctorOrNurse() {
		Authentication authentication = getAuthentication();
    	return authentication.getAuthorities().stream().anyMatch(authority -> {
    		String auth = authority.getAuthority();
    		return auth.equals(Role.DOCTOR.toString()) || auth.equals(Role.NURSE.toString());
    	});
    }

	public boolean isSameUserOrAdmin(String username) {
		return this.isAdmin() || this.isSameUser(username);
	}
	
	public boolean isSameUser(String username) {
		Authentication authentication = getAuthentication();
		boolean isAuthorized = false;
		if(username!= null) {
			if(username.equals(authentication.getDetails())) {
				isAuthorized = true;
			}
		}
		return isAuthorized;
	}
//	public ResponseEntity<?> seeAuth(){
//		SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().forEach(authority -> System.out.println(authority.getAuthority()));
//		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
//		return ResponseEntity.ok(new HashMap<>());
//	}
}
