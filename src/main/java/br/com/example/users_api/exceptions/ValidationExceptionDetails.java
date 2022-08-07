package br.com.example.users_api.exceptions;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails {
	private final String fields;
	private final String fieldMessage;
}
