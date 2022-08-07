package br.com.example.users_api.controller;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.example.users_api.Service.UserService;
import br.com.example.users_api.model.UserData;
import br.com.example.users_api.requests.UserPostRequestBody;
import br.com.example.users_api.requests.UserPutRequestBody;
import br.com.example.users_api.util.UserCreator;
import br.com.example.users_api.util.UserPostRequestBodyCreator;
import br.com.example.users_api.util.UserPutRequestBodyCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste para o controller da aplicação")
public class UserControllerTest {

	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserService userServiceMock;
	
	@BeforeEach
	void setUp() {
		PageImpl<UserData> userPage = new PageImpl<>(List.of(UserCreator.createValidUser()));
		BDDMockito.when(this.userServiceMock.listAll(ArgumentMatchers.any()))
			.thenReturn(userPage);
		
		BDDMockito.when(this.userServiceMock.findByIdOrThrowBadRequest(ArgumentMatchers.anyLong()))
			.thenReturn(UserCreator.createValidUser());
		
		BDDMockito.when(this.userServiceMock.findByName(ArgumentMatchers.anyString()))
			.thenReturn(List.of(UserCreator.createValidUser()));
		
		BDDMockito.when(this.userServiceMock.save(ArgumentMatchers.any(UserPostRequestBody.class)))
			.thenReturn(UserCreator.createValidUser());
		
		BDDMockito.doNothing().when(this.userServiceMock).replace(ArgumentMatchers.any(UserPutRequestBody.class));
		
		BDDMockito.doNothing().when(this.userServiceMock).delete(ArgumentMatchers.anyLong());
	}
	
	@Test
	@DisplayName("Retorna uma lista de usuarios como objeto page")
	void list_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
		UserData expectedUser = UserCreator.createValidUser();
		
		Page<UserData> userPage = this.userController.list(null).getBody();
		
		Assertions.assertThat(userPage).isNotNull();

        Assertions.assertThat(userPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(userPage.toList().get(0).getNome()).isEqualTo(expectedUser.getNome());
        Assertions.assertThat(userPage.toList().get(0).getCriado()).isEqualTo(expectedUser.getCriado());
        Assertions.assertThat(userPage.toList().get(0).getModificado()).isEqualTo(expectedUser.getModificado());
        Assertions.assertThat(userPage.toList().get(0).getId()).isEqualTo(expectedUser.getId());
	}
	
	@Test
	@DisplayName("Retorna um usuario na busca por Id")
	void findById_ReturnUser_WhenSuccessful() {
		UserData expectedUser = UserCreator.createValidUser();
		
		UserData user = this.userController.findById(100L).getBody();
		
		Assertions.assertThat(user).isNotNull();
		Assertions.assertThat(user.getId()).isNotNull()
			.isEqualTo(expectedUser.getId());
		Assertions.assertThat(user.getNome()).isEqualTo(expectedUser.getNome());
        Assertions.assertThat(user.getCriado()).isEqualTo(expectedUser.getCriado());
	}
	
	@Test
	@DisplayName("Retorna uma lista de usuarios na busca por nome")
	void findByName_ReturnListOfUsers_WhenSuccessful() {
		UserData expectedUser = UserCreator.createValidUser();
		
		List<UserData> users = this.userController.findByName(expectedUser.getNome()).getBody();
		
		Assertions.assertThat(users).isNotNull();

        Assertions.assertThat(users)
        		.isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(users.get(0).getNome()).isEqualTo(expectedUser.getNome());
        Assertions.assertThat(users.get(0).getCriado()).isEqualTo(expectedUser.getCriado());
        Assertions.assertThat(users.get(0).getId()).isEqualTo(expectedUser.getId());
	}
	
	@Test
	@DisplayName("Retorna uma lista vazia na busca por nome que não esta na base de dados")
	void findByName_ReturnEmptyListOfUsers_WhenUserIsNotFound() {
		BDDMockito.when(this.userServiceMock.findByName(ArgumentMatchers.anyString()))
			.thenReturn(Collections.emptyList());
				
		List<UserData> users = this.userController.findByName("User").getBody();
		
		Assertions.assertThat(users).isNotNull();

        Assertions.assertThat(users)
        		.isNotNull()
                .isEmpty();
	}
	
	@Test
	@DisplayName("Retorna o usuario que foi inserido")
	void save_ReturnUser_WhenSuccessful() {
		UserData user = this.userController.save(UserPostRequestBodyCreator.creteUserPostRequestBody()).getBody();
		
		Assertions.assertThat(user).isNotNull().isEqualTo(UserCreator.createValidUser());
	}
	
	@Test
	@DisplayName("Altera um usuario que foi passado")
	void replace_updateUser_WhenSuccessful() {
		Assertions.assertThatCode(() -> this.userController.replace(UserPutRequestBodyCreator.creteUserPostRequestBody()))
			.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = this.userController.replace(UserPutRequestBodyCreator.creteUserPostRequestBody());
		
		Assertions.assertThat(entity).isNotNull();
		
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Deleta um usuario que foi passado")
	void delete_removeUser_WhenSuccessful() {
		Assertions.assertThatCode(() -> this.userController.delete(1L))
			.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = this.userController.delete(1L);
		
		Assertions.assertThat(entity).isNotNull();
		
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
}
