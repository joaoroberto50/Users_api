package br.com.example.users_api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.example.users_api.Service.UserService;
import br.com.example.users_api.model.UserData;
import br.com.example.users_api.requests.UserPostRequestBody;
import br.com.example.users_api.requests.UserPutRequestBody;

@RestController
@RequestMapping("/usuarios")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<Page<UserData>> list(Pageable pageable){
		return ResponseEntity.ok(userService.listAll(pageable));
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<UserData> findById(@PathVariable Long id){
		return ResponseEntity.ok(userService.findByIdOrThrowBadRequest(id));
	}
	
	@GetMapping(path = "/find")
	public ResponseEntity<List<UserData>> findByName(@RequestParam(required = false) String nome){
		return ResponseEntity.ok(userService.findByName(nome));
	}
	
	@PostMapping
	public ResponseEntity<UserData> save(@RequestBody @Valid UserPostRequestBody userPost){
		return new ResponseEntity<>(userService.save(userPost), HttpStatus.CREATED);
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id){
		userService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping
	public ResponseEntity<Void> replace(@RequestBody UserPutRequestBody userPut){
		userService.replace(userPut);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
