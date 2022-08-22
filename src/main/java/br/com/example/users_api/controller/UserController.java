package br.com.example.users_api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/usuarios")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping
	@Operation(summary = "Lista todos os usuarios de paginada", 
		description = "Retorna todos os usuarios de maneira paginada (Pageableo) com tamanho padrão de 20 usuarios por pagina, "
				+ "a quantidade de usuarios retornados pode ser alterada pelo paramtro 'size'", tags = {"usuario"})
	public ResponseEntity<Page<UserData>> list(@ParameterObject Pageable pageable){
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
	@ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operação bem sucedida."),
            @ApiResponse(responseCode = "400", description = "O id não corresponde a nenhum usuario na base de dados.")
    })
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
