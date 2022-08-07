package br.com.example.users_api.util;

import br.com.example.users_api.model.UserData;

public class UserCreator {
	
	public static UserData createUserToBeSaved() {
		// TODO Auto-generated method stub
		return UserData.builder().nome("Teste 01")
				.editado(false)
				.criado("2022-07-30 20:19:18")
				.modificado("2022-07-30 20:19:18")
				.build();
	}
	
	public static UserData createValidUser() {
		return UserData.builder()
				.id(1L)
				.nome("Teste 01")
				.editado(false)
				.criado("2022-07-30 20:19:18")
				.modificado("2022-07-30 20:19:18")
				.build();
	}
	
	public static UserData createValidUpdatedUser() {
		return UserData.builder()
				.id(1L)
				.nome("Teste 02")
				.editado(true)
				.criado("2022-07-30 20:19:18")
				.modificado("2022-08-01 18:19:20")
				.build();
	}
}
