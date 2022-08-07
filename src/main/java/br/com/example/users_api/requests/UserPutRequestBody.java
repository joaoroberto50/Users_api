package br.com.example.users_api.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPutRequestBody {
	private Long Id;
	private String nome;
}
