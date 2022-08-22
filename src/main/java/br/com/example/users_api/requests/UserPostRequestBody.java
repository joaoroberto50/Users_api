package br.com.example.users_api.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserPostRequestBody {
	@NotEmpty(message = "Nome não pode ser vazio")
	@NotNull(message = "Nome não pode ser nulo")
	@Schema(description = "Nome do usuario", example = "Maria Olga", required = true)
	private String nome;
}
