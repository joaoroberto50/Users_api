package br.com.example.users_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.example.users_api.model.UserData;

public interface UserRepository extends JpaRepository<UserData, Long> {
	List<UserData> findByNome(String nome);
}
