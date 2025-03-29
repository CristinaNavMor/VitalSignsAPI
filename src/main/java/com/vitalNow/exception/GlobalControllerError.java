package com.vitalNow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vitalNow.model.ApiError;

@RestControllerAdvice
public class GlobalControllerError {

	@ExceptionHandler(ElementNotFoundException.class)
	public ResponseEntity<ApiError> handleErrorNotFoundException(ElementNotFoundException e){
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiError> handleBadRequestExceptoin(BadRequestException e){
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<ApiError> handleServiceUnavailableException(ServiceUnavailableException e){
		ApiError apiError = new ApiError(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(apiError);
	}
	
	@ExceptionHandler(OptimisticLockingFailureException.class)
	public ResponseEntity<ApiError> handleOptimisticLockingFailureExceptionn(OptimisticLockingFailureException e){
		ApiError apiError = new ApiError(HttpStatus.CONFLICT, e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}
	@ExceptionHandler(InternalServerErrorException.class)
	public ResponseEntity<ApiError> handleInternalServerErrorException(InternalServerErrorException e){
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}
	
	@ExceptionHandler(BadCredentialException.class)
	public ResponseEntity<ApiError> handleBadCredentialException(BadCredentialException e){
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler(BadTokenException.class)
	public ResponseEntity<ApiError> handleBadTokenException(BadTokenException e){
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiError> handleUnauthorizedException(UnauthorizedException e){
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
	}
	
	@ExceptionHandler(DisabledOrLockedAccountException.class)
	public ResponseEntity<ApiError> handleDisabledOrLockedAccountException(DisabledOrLockedAccountException e){
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}
	
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ApiError> handleForbiddenException(ForbiddenException e){
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}
	
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ApiError> handleConflictException(ConflictException e){
		ApiError apiError = new ApiError(HttpStatus.CONFLICT, e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}
	
	

}
