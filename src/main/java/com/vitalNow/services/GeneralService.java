package com.vitalNow.services;

import java.util.Set;

import org.springframework.stereotype.Service;


import jakarta.validation.ConstraintViolation;

@Service
public class GeneralService {

	public <T> String getErrorMessages(Set<ConstraintViolation<T>> violations) {
		StringBuilder errorMsg = new StringBuilder("");
		for(ConstraintViolation<T> violation : violations) {
			errorMsg.append(violation.getMessage()+"\n");
		}
		return errorMsg.toString();
	}

}
