package br.com.example.users_api.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.example.users_api.Service.UserService;
import br.com.example.users_api.exceptions.BadRequestException;
import br.com.example.users_api.model.UserData;
import br.com.example.users_api.repository.UserRepository;
import br.com.example.users_api.requests.UserPostRequestBody;
import br.com.example.users_api.requests.UserPutRequestBody;
import br.com.example.users_api.util.UserCreator;
import br.com.example.users_api.util.UserPostRequestBodyCreator;
import br.com.example.users_api.util.UserPutRequestBodyCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste para o service da aplicação")
public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepositoryMock;
	
	@BeforeEach
	void setUp() {
		PageImpl<UserData> userPage = new PageImpl<>(List.of(UserCreator.createValidUser()));
		BDDMockito.when(this.userRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
			.thenReturn(userPage);
		
		BDDMockito.when(this.userRepositoryMock.findById(ArgumentMatchers.anyLong()))
			.thenReturn(Optional.of(UserCreator.createValidUser()));
		
		BDDMockito.when(this.userRepositoryMock.findByNome(ArgumentMatchers.anyString()))
			.thenReturn(List.of(UserCreator.createValidUser()));
		
		BDDMockito.when(this.userRepositoryMock.save(ArgumentMatchers.any(UserData.class)))
			.thenReturn(UserCreator.createValidUser());
				
		BDDMockito.doNothing().when(this.userRepositoryMock).delete(ArgumentMatchers.any(UserData.class));
	}
	
	@Test
	@DisplayName("ListAll retorna uma lista de usuarios como objeto page")
	void list_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
		UserData expectedUser = UserCreator.createValidUser();
		
		Page<UserData> userPage = this.userService.listAll(PageRequest.of(1, 1));
		
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
	@DisplayName("FindByID retorna um usuario na busca por Id")
	void findByIdOrThrowBadRequestException_ReturnUser_WhenSuccessful() {
		
		UserData expectedUser = UserCreator.createValidUser();
		
		UserData user = this.userService.findByIdOrThrowBadRequest(100L);
		
		Assertions.assertThat(user).isNotNull();
		Assertions.assertThat(user.getId()).isNotNull()
			.isEqualTo(expectedUser.getId());
		Assertions.assertThat(user.getNome()).isEqualTo(expectedUser.getNome());
        Assertions.assertThat(user.getCriado()).isEqualTo(expectedUser.getCriado());
	}
	
	@Test
	@DisplayName("FindByID lança uma exceção quando um usuario na busca por Id não é encontrado")
	void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenUserIsNotFound() {
		BDDMockito.when(this.userRepositoryMock.findById(ArgumentMatchers.anyLong()))
			.thenReturn(Optional.empty());
		
		Assertions.assertThatExceptionOfType(BadRequestException.class)
			.isThrownBy(() -> this.userService.findByIdOrThrowBadRequest(109L));
	}
	
	@Test
	@DisplayName("FindByName retorna uma lista de usuarios na busca por nome")
	void findByName_ReturnListOfUsers_WhenSuccessful() {
		UserData expectedUser = UserCreator.createValidUser();
		
		List<UserData> users = this.userService.findByName(expectedUser.getNome());
		
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
	@DisplayName("FindByName retorna uma lista vazia na busca por nome que não esta na base de dados")
	void findByName_ReturnEmptyListOfUsers_WhenUserIsNotFound() {
		BDDMockito.when(this.userRepositoryMock.findByNome(ArgumentMatchers.anyString()))
			.thenReturn(Collections.emptyList());
				
		List<UserData> users = this.userService.findByName("User");
		
		Assertions.assertThat(users).isNotNull();

        Assertions.assertThat(users)
        		.isNotNull()
                .isEmpty();
	}
	
//	@Test
//	@DisplayName("Save retorna o usuario que foi inserido")
//	void save_ReturnUser_WhenSuccessful() {
//		UserPostRequestBody userPost = UserPostRequestBodyCreator.creteUserPostRequestBody();
//		UserData user = this.userService.save(userPost);
//		
//		Assertions.assertThat(user).isNotNull().isEqualTo(UserCreator.createValidUser());
//	}
//	
//	@Test
//	@DisplayName("Replace altera um usuario que foi passado")
//	void replace_updateUser_WhenSuccessful() {
//		UserPutRequestBody userPut = UserPutRequestBodyCreator.creteUserPostRequestBody();
//		Assertions.assertThatCode(() -> this.userService.replace(userPut))
//			.doesNotThrowAnyException();
//	}
	
	@Test
	@DisplayName("Delete deleta um usuario que foi passado")
	void delete_removeUser_WhenSuccessful() {
		Assertions.assertThatCode(() -> this.userService.delete(1L))
			.doesNotThrowAnyException();
	}
}
