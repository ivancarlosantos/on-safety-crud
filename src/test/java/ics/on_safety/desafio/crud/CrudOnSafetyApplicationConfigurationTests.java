package ics.on_safety.desafio.crud;

import ics.on_safety.desafio.crud.dto.PessoaDTO;
import ics.on_safety.desafio.crud.factory.FakeFactory;
import ics.on_safety.desafio.crud.model.Pessoa;
import ics.on_safety.desafio.crud.repository.PessoaRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.PostgreSQLContainer;

import java.text.ParseException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrudOnSafetyApplicationConfigurationTests {

    @LocalServerPort
    Integer port;

    @Autowired
    PessoaRepository repository;

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15"));

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);

        System.out.println("url: " + container.getJdbcUrl());
        System.out.println("username: " + container.getUsername());
        System.out.println("password: " + container.getPassword());
        System.out.println("spring.datasource.driver-class-name: " + container.getJdbcDriverInstance());
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        repository.deleteAll();
    }

    @Test
    void connectionEstablished() {
        assertThat(container.isCreated()).isTrue();
        assertThat(container.isRunning()).isTrue();
    }

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    void findPersonTest() throws ParseException {

        Pessoa pessoa = new Pessoa(1L, FakeFactory.pessoa().getNome(), "840.131.618-93", FakeFactory.pessoa().getDataNascimento(), FakeFactory.pessoa().getEmail());

        Pessoa saved = repository.save(pessoa);

        PessoaDTO dto = new PessoaDTO(saved.getNome(), saved.getCpf(), saved.getDataNascimento(), saved.getEmail());

        given()
                .contentType(ContentType.JSON)
                .queryParam("nome", dto.nome())
                .when()
                .get("/api/find")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FOUND.value())
                .body("[0].nome", equalTo(dto.nome()));
    }

    @Test
    void listAllTest() throws ParseException {

        Pessoa pessoa = new Pessoa(FakeFactory.pessoa().getId(), FakeFactory.pessoa().getNome(), FakeFactory.pessoa().getCpf(), FakeFactory.pessoa().getDataNascimento(), FakeFactory.pessoa().getEmail());

        PessoaDTO dto = new PessoaDTO(pessoa.getNome(), pessoa.getCpf(), pessoa.getDataNascimento(), pessoa.getEmail());

        Pessoa savedPessoa1 = new Pessoa(1L, dto.nome(), dto.cpf(), dto.dataNascimento(), dto.email());
        Pessoa savedPessoa2 = new Pessoa(2L, dto.nome(), "806.668.518-16", dto.dataNascimento(), dto.email());

        List<Pessoa> pessoaList = List.of(savedPessoa1, savedPessoa2);

        repository.saveAll(pessoaList);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/list")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

}
