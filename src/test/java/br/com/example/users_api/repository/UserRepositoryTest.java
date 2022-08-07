package br.com.example.users_api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.example.users_api.model.UserData;
import br.com.example.users_api.util.DateUtil;
import br.com.example.users_api.util.UserCreator;

@DataJpaTest
@DisplayName("Teste para o repositorio da aplicação")
public class UserRepositoryTest {
	private final DateUtil dateUtil = new DateUtil();
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@DisplayName("Salvar um novo usuario")
	void save_PersistUser_WhenSuccessfull() {
		UserData userToBeSaved = UserCreator.createUserToBeSaved();
		UserData userSaved = this.userRepository.save(userToBeSaved);
		
		Assertions.assertThat(userSaved).isNotNull();
		Assertions.assertThat(userSaved.getId()).isNotNull();
		Assertions.assertThat(userSaved.getNome()).isEqualTo(userToBeSaved.getNome());
		Assertions.assertThat(userSaved.isEditado()).isFalse();
		// A data de criação e a data de modificação é a mesma para usuarios recem-criados
		Assertions.assertThat(userSaved.getCriado()).isEqualTo(userSaved.getModificado());
	}
	
	@Test
	@DisplayName("Atualizar um usuario")
	void save_UpdateUser_WhenSuccessfull() {
		UserData userToBeSaved = UserCreator.createUserToBeSaved();
		UserData userSaved = this.userRepository.save(userToBeSaved);
		
		userSaved.setNome("Test 02");
		userSaved.setEditado(true);
		userSaved.setModificado(dateUtil.formatLocalDateBaseStyle(LocalDateTime.now()));
		
		UserData userUpdated = this.userRepository.save(userSaved);
		
		Assertions.assertThat(userUpdated).isNotNull();
		Assertions.assertThat(userUpdated.getId()).isNotNull();
		Assertions.assertThat(userUpdated.getNome()).isEqualTo(userUpdated.getNome());
		Assertions.assertThat(userUpdated.isEditado()).isTrue();
	}
	
	@Test
	@DisplayName("Deleta um usuario do repositorio")
	void delete_RemoveUser_WhenSuccessfull() {
		UserData userToBeSaved = UserCreator.createUserToBeSaved();
		UserData userSaved = this.userRepository.save(userToBeSaved);
		
		this.userRepository.delete(userSaved);
		
		Optional<UserData> userOptional = this.userRepository.findById(userSaved.getId());
		
		Assertions.assertThat(userOptional).isEmpty();
	}
	
	@Test
	@DisplayName("Retorna uma lista de usuarios com o nome igual ao pesquisado")
	void findByName_ReturnListOfUsers_WhenSuccessfull() {
		UserData userToBeSaved = UserCreator.createUserToBeSaved();
		UserData userSaved = this.userRepository.save(userToBeSaved);
		
		List<UserData> users = this.userRepository.findByNome(userSaved.getNome());
		
		Assertions.assertThat(users).isNotEmpty();
		Assertions.assertThat(users).contains(userSaved);
	}
	
	@Test
	@DisplayName("Retorna uma lista de usuarios vazia quando nehum usuario com o nome igual ao pesquisado é encontrado")
	void findByName_ReturnEmptyList_WhenUserIsNotFound() {
		List<UserData> users = this.userRepository.findByNome("Teste 03");
		
		Assertions.assertThat(users).isEmpty();
	}
	
	@Test
	@DisplayName("Lança a exceção ConstraintViolationException quando o nome é vazio")
	void save_ThrowsConstraintViolationException_WhenNomeIsEmpty() {
		UserData user = new UserData();
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.userRepository.save(user))
			.withMessageContaining("Nome não pode ser vazio");
	}
	
	@Test
	@DisplayName("Lança a exceção ConstraintViolationException quando o nome é nulo")
	void save_ThrowsConstraintViolationException_WhenNomeIsNull() {
		UserData user = UserData.builder().nome(null).build();
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.userRepository.save(user))
			.withMessageContaining("Nome não pode ser nulo");
	}

}
