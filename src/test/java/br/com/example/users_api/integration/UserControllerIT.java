package br.com.example.users_api.integration;

import org.junit.jupiter.api.Test;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;

import br.com.example.users_api.model.UserData;
import br.com.example.users_api.repository.UserRepository;
import br.com.example.users_api.requests.UserPostRequestBody;
import br.com.example.users_api.requests.UserPutRequestBody;
import br.com.example.users_api.util.UserCreator;
import br.com.example.users_api.util.UserPostRequestBodyCreator;
import br.com.example.users_api.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DisplayName("Teste de Integração para o controller da aplicação")
public class UserControllerIT {
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@DisplayName("Retorna uma lista com um usuario dentro")
	void list_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
		UserData expectedUser = this.userRepository.save(UserCreator.createUserToBeSaved());
		
		PageableResponse<UserData> userPage = testRestTemplate.exchange("/usuarios", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageableResponse<UserData>>() {}).getBody();
		
		Assertions.assertThat(userPage).isNotNull();

        Assertions.assertThat(userPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(userPage.toList().get(0).getNome()).isEqualTo(expectedUser.getNome());
        Assertions.assertThat(userPage.toList().get(0).getCriado()).isEqualTo(expectedUser.getCriado());
        Assertions.assertThat(userPage.toList().get(0).getModificado()).isEqualTo(expectedUser.getModificado());
	}
	
	@Test
	@DisplayName("Retorna um usuario na busca por Id")
	void findById_ReturnUser_WhenSuccessful() {
		UserData savedUser = this.userRepository.save(UserCreator.createUserToBeSaved());
		
		UserData user = this.testRestTemplate.getForObject("/usuarios/{id}", UserData.class, savedUser.getId());
		
		Assertions.assertThat(user).isNotNull();
		Assertions.assertThat(user.getId()).isNotNull()
			.isEqualTo(savedUser.getId());
		Assertions.assertThat(user.getNome()).isEqualTo(savedUser.getNome());
        Assertions.assertThat(user.getCriado()).isEqualTo(savedUser.getCriado());
	}
	
	@Test
	@DisplayName("Retorna uma lista de usuarios na busca por nome")
	void findByName_ReturnListOfUsers_WhenSuccessful() {
		UserData savedUser = this.userRepository.save(UserCreator.createUserToBeSaved());
		
		String url = String.format("/usuarios/find?nome=%s", savedUser.getNome());
		
		List<UserData> users = this.testRestTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<UserData>>() {}).getBody();
		
		Assertions.assertThat(users).isNotNull();

        Assertions.assertThat(users)
        		.isNotNull()
                .isNotEmpty()
                .hasSize(2);
        Assertions.assertThat(users.get(1).getNome()).isEqualTo(savedUser.getNome());
        Assertions.assertThat(users.get(1).getCriado()).isEqualTo(savedUser.getCriado());
        Assertions.assertThat(users.get(1).getId()).isEqualTo(savedUser.getId());
	}
	
	@Test
	@DisplayName("Retorna uma lista vazia na busca por nome que não esta na base de dados")
	void findByName_ReturnEmptyListOfUsers_WhenUserIsNotFound() {
		List<UserData> users = this.testRestTemplate.exchange("/usuarios/find?nome=Ivalid User", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<UserData>>() {}).getBody();
				
		Assertions.assertThat(users)
        		.isNotNull()
                .isEmpty();
	}
	
	@Test
	@DisplayName("Retorna o usuario que foi inserido")
	void save_ReturnUser_WhenSuccessful() {
		UserPostRequestBody userPost = UserPostRequestBodyCreator.creteUserPostRequestBody();
		
		ResponseEntity<UserData> userResponseEntity = this.testRestTemplate.postForEntity("/usuarios", userPost, UserData.class);
		
		Assertions.assertThat(userResponseEntity).isNotNull();
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(userResponseEntity.getBody()).isNotNull();
		Assertions.assertThat(userResponseEntity.getBody().getId()).isNotNull();
		Assertions.assertThat(userResponseEntity.getBody().getNome()).isEqualTo(userPost.getNome());
		Assertions.assertThat(userResponseEntity.getBody().getCriado()).isEqualTo(userResponseEntity.getBody().getModificado());
	}
	
	@Test
	@DisplayName("Altera um usuario que foi passado")
	void replace_updateUser_WhenSuccessful() {
		UserData savedUser = this.userRepository.save(UserCreator.createUserToBeSaved());
		
		UserPutRequestBody userPut = UserPutRequestBody.builder().Id(savedUser.getId()).nome("Teste 296").build();
		
		ResponseEntity<Void> userResponseEntity = this.testRestTemplate.exchange("/usuarios", HttpMethod.PUT, 
				new HttpEntity<>(userPut), Void.class);
		
		Assertions.assertThat(userResponseEntity).isNotNull();

        Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Deleta um usuario que foi passado")
	void delete_removeUser_WhenSuccessful() {
		UserData savedUser = this.userRepository.save(UserCreator.createUserToBeSaved());
		
		ResponseEntity<Void> userResponseEntity = this.testRestTemplate.exchange("/usuarios/{id}", HttpMethod.DELETE, 
				null, Void.class, savedUser.getId());
				
		Assertions.assertThat(userResponseEntity).isNotNull();
		
		Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
}
