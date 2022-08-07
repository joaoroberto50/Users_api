package br.com.example.users_api.util;

import br.com.example.users_api.requests.UserPutRequestBody;

public class UserPutRequestBodyCreator {
	
	public static UserPutRequestBody creteUserPostRequestBody() {
		return UserPutRequestBody.builder()
				.Id(UserCreator.createValidUpdatedUser().getId())
				.nome(UserCreator.createValidUpdatedUser().getNome()).build();
	}
	
	public static UserPutRequestBody creteUserPostRequestBodyWithInvalidId() {
		return UserPutRequestBody.builder().Id(109L).nome("Teste 04").build();
	}
}
