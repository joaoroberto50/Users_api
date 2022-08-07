package br.com.example.users_api.Service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.example.users_api.exceptions.BadRequestException;
import br.com.example.users_api.model.UserData;
import br.com.example.users_api.repository.UserRepository;
import br.com.example.users_api.requests.UserPostRequestBody;
import br.com.example.users_api.requests.UserPutRequestBody;
import br.com.example.users_api.util.DateUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	@Autowired
	private final DateUtil dateUtil;
	private final UserRepository userRepository;

	public Page<UserData> listAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return userRepository.findAll(pageable);
	}
	
	public UserData findByIdOrThrowBadRequest(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("User not Found"));
	}
	
	public List<UserData> findByName(String nome){
		return (List<UserData>) userRepository.findByNome(nome);
	}
	
	@Transactional
	public UserData save(UserPostRequestBody userPost) {
		// TODO Auto-generated method stub
		return userRepository.save(UserData.builder().nome(userPost.getNome()).editado(false)
				.criado(dateUtil.formatLocalDateBaseStyle(LocalDateTime.now()))
				.modificado(dateUtil.formatLocalDateBaseStyle(LocalDateTime.now())).build());
	}

	public void delete(long id) {
		// TODO Auto-generated method stub
		userRepository.delete(this.findByIdOrThrowBadRequest(id));
	}

	public void replace(UserPutRequestBody userPut) {
		// TODO Auto-generated method stub
		UserData saveUser = this.findByIdOrThrowBadRequest(userPut.getId());
		UserData user = UserData.builder()
				.id(saveUser.getId())
				.nome(userPut.getNome()).editado(true)
				.criado(saveUser.getCriado())
				.modificado(dateUtil.formatLocalDateBaseStyle(LocalDateTime.now())).build();
		userRepository.save(user);
	}
	
}
