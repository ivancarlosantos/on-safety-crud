<p align="center">
 <img src="https://img.shields.io/static/v1?label=Dev&message=Ivan Santos&color=8257E5&labelColor=000000" alt="@ivan_santos" />
 <img src="https://img.shields.io/static/v1?label=Tipo&message=Desafio&color=867E12&labelColor=00A654" alt="Desafio" />
 <img src="https://img.shields.io/static/v1?label=Status&message=Em Desenvolvimento&color=&labelColor=90876I" alt="Status" />
</p>

> # Aplicação em Web para Gestão de Pessoas

* **Desafio por** [@OnSafety](https://www.linkedin.com/company/on-safety/posts/?feedView=all)

> ## Descrição do Projeto
O projeto consiste na construção de uma plataforma Web/API para auxiliar no Cadastramento e Gestão de Pessoas

O intuito é, centralizar informações básicas de usuários

Sendo assim, foram criados os serviços:
* Página Web para visualização e cadastramento dos usuários
* Listar usuários cadastrados
* Encontrar pessoa(s) por filtragem (por letra/terminação inicial e/ou final) de seu nome/sobrenome


> ## Tecnologias Utilizadas
As seguintes ferramentas foram utilizadas na construção do projeto
- [Java - v17](https://www.oracle.com/br/java/technologies/downloads/)
- [Maven](https://maven.apache.org/)
- [Spring Boot](https://spring.io/)
- [Thymeleaf](https://www.thymeleaf.org/index.html)
- [MySQL](https://www.mysql.com/)
- [Adminer](https://www.adminer.org/)
- [Docker](https://www.docker.com/get-started/)
- [JUnit](https://junit.org/junit5/)
- [Pitest](https://pitest.org/)
- [Mockito](https://site.mockito.org/)
- [MockMVC](https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-framework.html)
- [Testcontainers](https://testcontainers.com/getting-started/)
- [Swagger Docs](https://swagger.io/)

> ## Práticas adotadas

- Página Web
- API REST
- Containerização
- Injeção de Dependências
- Testes de Unidade, Integração e cobertura
- Testcontainers
- Tratamento de respostas de erro
- Implementação de esteira CI/CD Actions

> ## Instalação
Tenha instalado em seu sistema operacional:
> - [JAVA NA VERSÃO 17 OU SUPERIOR](https://www.oracle.com/br/java/technologies/downloads/#java17)
>
> - [MAVEN](https://maven.apache.org/download.cgi)
>
> - [DOCKER](https://www.docker.com/get-started/)
> 
> - [GIT](https://git-scm.com/downloads)

> ## Como Executar

- Clonar repositório `GITHUB`
```bash
$ git clone https://github.com/ivancarlosantos/on-safety-crud.git
```
- Ir até o diretório/pasta localizando a aplicação
```bash
$ cd [caminho onde realizou o clone]
```
- Construir o projeto

_Caso desejar usar container Docker do MySQL para persistência e o Adminer SGBD, na raíz do projeto, executar:_
```bash
$ docker-compose up -d
```
> ## Adminer
- _Adminer é um serviço de SGBD para acesso e gerência de dados desenvolvido em PHP_

Para acesso aos dados persistidos, via ` Adminer `, acesse:
````bash
$ http://localhost:15432
````
Com o container do _**Adminer SGBD**_ ativo, preencha os seguintes campos:
> [**System**]() - [_**MySQL**_]()
>
> [**Server**]() - [_**db**_]()
>
> [**Username**]() - [_**root**_]()
>
> [**password**]() - [_**root**_]()


Caso haja problema de conexão com o Banco de Dados no Docker, realize o seguinte procedimento:

Abra um terminal e execute os comando abaixo:
```bash
$ docker exec -it crud_db /bin/bash
```

- execute o comando para entrar no mysql
```bash
$ mysql -u root -p
```

- Coloque a senha password
```bash
enter password: password
```

Abrirá o terminal do mysql
```bash
mysql>
```
Agora execute os comando abaixo
```bash
mysql> ALTER USER 'root' IDENTIFIED WITH mysql_native_password BY 'password';
```

Atualize os privilégios de usuário
```bash
mysql> flush privileges;
```

- Selecione a base de dados
```bash
mysql> use crud_db;
````

> ## DEMO

- Caso desejar testar a aplicação em ambiente `dev`, com o Docker ativo, execute:
````bash
$ docker run --name=crud-on-safety -p 8080:8080 -d devmenorzera/crud
````
- Para acessar a aplicação, via [Web](http://localhost:8080)
````bash
$ http://localhost:8080
````
- Para acessar a API, via [Swagger-UI](http://localhost:8080/swagger-ui/index.html#/):
````bash
$ http://localhost:8080/swagger-ui/index.html#/
````

Com a base de dados ativa, inicializar o projeto:

- Na raíz do projeto, executar a aplicação com o ambiente desejado (dev/prod)
```bash
$ mvn spring-boot:run -P prod
```
- ou use o wrapper run
```bash
$ .\mvnw spring-boot:run -P prod
```
_``PS: Em caso de execução da aplicação em ambiente dev, os dados serão salvos em memória (H2)``_

# API Endpoints
A página poderá ser acessada em [http://localhost:8080](http://localhost:8080)

Ou se preferir, você pode acessar a API via [SWAGGER](http://localhost:8080/swagger-ui/index.html) como client HTTP

- Cadastrar uma Pessoa [[POST]]()
```bash
$ http POST :8080/addPessoaForm nome="Nome" cpf="123.456.789-00" dataNascimento="01/01/2000" email="email@email.com"
{
  "nome": "nome",
  "cpf": "123.456.789-00",
  "dataNascimento": "01/01/2000",
  "email": "email@email.com"
}
```

- Listar Pessoas Cadastradas [[GET]]()
```bash
$ http GET :8080/list

[
 {
  "nome": "nome A",
  "cpf": "123.456.789-00",
  "dataNascimento": "01/01/2000",
  "email": "email_a@email.com"
 },
 {
  "nome": "nome B",
  "cpf": "987.654.321-00",
  "dataNascimento": "01/01/2000",
  "email": "email_b@email.com"
 }
]
```
- Encontrar Pessoa pelo nome [[GET]]()

`PS: Você pode encontrar/filtrar por inicio, meio ou final do nome`
```bash
$ http GET :8080/api/find?nome=

[
  {
    "nome": "nome",
    "cpf": "123.456.789-00",
    "dataNascimento": "01/01/2000",
    "email": "email@email.com"
  }
]
```

- Atualizar Cadastro de uma Pessoa [[PUT]]()
```bash
$ http PUT :8080/showUpdateForm?id=

  {
    "nome": "nome update",
    "cpf": "123.456.789-00",
    "dataNascimento": "01/01/2000",
    "email": "email@email.com"
  }
```
> ## Teste de Unidade
_Os teste de unidade podem ser executados para verificação de cobertura da aplicação_

Para execução dos teste de unidade e integração, realize o seguinte comando:
```bash
$ mvn clean test -P test
```

Para execução dos testes para verificação de cobertura, realize o seguinte comando:
```bash
$ mvn test-compile org.pitest:pitest-maven:mutationCoverage -P test
```

O link de cobertura pode ser encontrado no diretório

```bash
$ cd target/pit-reports/index.html
```