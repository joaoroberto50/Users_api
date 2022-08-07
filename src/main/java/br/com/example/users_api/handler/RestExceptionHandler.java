package br.com.example.users_api.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.example.users_api.exceptions.BadRequestException;
import br.com.example.users_api.exceptions.BadRequestExceptionDetails;
import br.com.example.users_api.exceptions.ValidationExceptionDetails;

@ControllerAdvice
public class RestExceptionHandler {
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre){
		return new ResponseEntity<>(
				BadRequestExceptionDetails.builder()
					.timestamp(LocalDateTime.now())
					.status(HttpStatus.BAD_REQUEST.value())
					.title("Bad Request Exception, Check the Documentation.")
					.details(bre.getMessage())
					.developerMessage(bre.getMessage())
					.build(), HttpStatus.BAD_REQUEST
		);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(
			MethodArgumentNotValidException manve){
		List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
		String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
		return new ResponseEntity<>(
				ValidationExceptionDetails.builder()
					.timestamp(LocalDateTime.now())
					.status(HttpStatus.BAD_REQUEST.value())
					.title("Bad Request Exception, Check the Documentation.")
					.details("Field Error. Please chck the fields")
					.developerMessage(manve.getMessage())
					.fields(fields)
					.fieldMessage(fieldsMessage)
					.build(), HttpStatus.BAD_REQUEST
		);
	}
}
