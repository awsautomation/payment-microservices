package com.visa.innovation.paymentservice.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.visa.innovation.paymentservice.util.Constants;
import com.visa.innovation.paymentservice.util.CustomLogger;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getDefaultMessage());
		}
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), Constants.INAVLID_INPUT_PARAMETERS_ERROR,
				errors);
		CustomLogger.log(apiError);
		return handleExceptionInternal(ex, apiError, headers, HttpStatus.valueOf(apiError.getStatus()), request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), errors);
		CustomLogger.log(apiError);
		return handleExceptionInternal(ex, apiError, headers, HttpStatus.valueOf(apiError.getStatus()), request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
		ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getLocalizedMessage(),
				builder.toString());
		CustomLogger.log(apiError);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.valueOf(apiError.getStatus()));
	}
	
	@ExceptionHandler({InputParametersInvalidException.class})
	public ResponseEntity<Object> handleInputParametersInvalidException(InputParametersInvalidException ex, WebRequest request){
		
		List<String> errors = ex.getErrors();
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), errors);
		CustomLogger.log(apiError);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ CallbackUrlException.class })
	public ResponseEntity<Object> handleCallbackUrlException(CallbackUrlException ex, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), errors);
		CustomLogger.log(apiError);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ GenericVDPException.class })
	public ResponseEntity<ApiError> handleGenericVDPException(GenericVDPException ex, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getVDPError().getResponseStatusVDP().getMessage());
		ApiError apiError = null;
		if (ex.getVDPError().getResponseStatusVDP().getStatus()
				.equalsIgnoreCase(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
			apiError = new ApiError(HttpStatus.SERVICE_UNAVAILABLE.value(),
					ex.getVDPError().getResponseStatusVDP().getMessage(), errors);
			CustomLogger.log(apiError);
			return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
		} else {
			apiError = new ApiError(Integer.parseInt(ex.getVDPError().getResponseStatusVDP().getStatus()),
					ex.getVDPError().getResponseStatusVDP().getMessage(), errors);
			CustomLogger.log(apiError);
			return new ResponseEntity<ApiError>(apiError, new HttpHeaders(),
					HttpStatus.valueOf(Integer.parseInt(ex.getVDPError().getResponseStatusVDP().getStatus())));
		}

	}
	
	@ExceptionHandler({ GenericServiceException.class })
	public ResponseEntity<ApiError> handleGenericException(GenericServiceException ex, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getError().getMessage());
		ApiError apiError = null;
		if (ex.getError().getStatus()
				.equalsIgnoreCase(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
			apiError = new ApiError(HttpStatus.SERVICE_UNAVAILABLE.value(),
					ex.getError().getMessage(), errors);
			CustomLogger.log(apiError);
			return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
		} else {
			apiError = new ApiError(Integer.parseInt(ex.getError().getStatus()),
					ex.getError().getMessage(), errors);
			CustomLogger.log(apiError);
			return new ResponseEntity<ApiError>(apiError, new HttpHeaders(),
					HttpStatus.valueOf(Integer.parseInt(ex.getError().getStatus())));
		}

	}
	
	@ExceptionHandler({GenericException.class})
	public ResponseEntity<Object> GenericException(GenericException ex, WebRequest request){
		
		List<String> errors = ex.getErrors();
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), errors);
		CustomLogger.log(apiError);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ ForbiddenVDPException.class })
	public ResponseEntity<ApiError> handleForbiddenVDPException(ForbiddenVDPException ex, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ApiError apiError = null;
		
			apiError = new ApiError(HttpStatus.FORBIDDEN.value(),
					ex.getMessage(), errors);
			CustomLogger.log(apiError);
			return new ResponseEntity<ApiError>(apiError, new HttpHeaders(),
					HttpStatus.FORBIDDEN);

	}
	
	@ExceptionHandler({MissingHeadersException.class})
	public ResponseEntity<Object> handleMissingHeadersException(MissingHeadersException ex, WebRequest request){
		
		List<String> errors = ex.getErrors();
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), errors);
		CustomLogger.log(apiError);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler({TransactionNotFoundException.class})
	public ResponseEntity<Object> handleUserNotMatchingException(TransactionNotFoundException ex, WebRequest request){
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), errors);
		CustomLogger.log(apiError);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

}
