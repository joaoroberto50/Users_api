package br.com.example.users_api.util;

import br.com.example.users_api.requests.UserPostRequestBody;

public class UserPostRequestBodyCreator {
	public static UserPostRequestBody creteUserPostRequestBody() {
		UserPostRequestBody userPost = new UserPostRequestBody();
		userPost.setNome(UserCreator.createValidUser().getNome());
		return userPost;
	}
}
