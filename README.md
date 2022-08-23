## Users_api Spring

API Rest construída com Spring Boot e MySQL para acessar e manipular uma base de usuarios.

### Tecnologias Utilizadas
- Java 11
- Spring Boot 2.7.1
- MySQL
- Docker
- Docker-Compose
- H2

### Metodos e Endpoints da Api
Quando a aplicação está rodando localmente a api fica disponivel em http://localhost:8080/usuarios/
- Get /usuarios/
Retorna um pageable com os usuarios cadastrados na base de dados.
- Get /usuarios/{id}
Retorna um usuario correspondente ao id passado como parametro se ele existir na base de dados, caso contrario retornará uma HTTP BadRequest, code 400.
- Get /usuarios/find?nome=
Retorna uma lista de usuarios correspondentes ao nome passado como parametro, caso não tenha nenhum usuario com o nome correspondente ao parametro, será retornada uma lista vazia.
- Post /usuarios/
Adiciona o usuario passado no banco de dados e retorna o mesmo.
-Put /usuarios/
Modifica um usuario passado e retorna Http statuscode 204.
-Delete /usuarios/{id}
Exclui um usuario da base de dados e retorna Http statuscode 204 quando bem sucedido e 400 quando o usuario não é encontrado.

É possível encontrar mais detalhes da api em http://localhost:8080/swagger-ui-custom.html (Spring-openapi) quando a api estiver rodando.

O projeto ainda conta com Spring-actuator para realizar o monitoramento da aplicação.

Esse projeto é baseado na api desenvolvida no curso Spring Boot 2 Essentials da DevDojo.

Agosto de 2022.
