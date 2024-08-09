package ics.on_safety.desafio.crud.servicetest;

import ics.on_safety.desafio.crud.dto.PessoaDTO;
import ics.on_safety.desafio.crud.dto.PessoaResponse;
import ics.on_safety.desafio.crud.exception.RegraDeNegocioException;
import ics.on_safety.desafio.crud.exception.ValidateParameterException;
import ics.on_safety.desafio.crud.factory.FakeFactory;
import ics.on_safety.desafio.crud.model.Endereco;
import ics.on_safety.desafio.crud.model.Pessoa;
import ics.on_safety.desafio.crud.repository.PessoaRepository;
import ics.on_safety.desafio.crud.service.PessoaServices;
import ics.on_safety.desafio.crud.utils.ValidateParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

    @InjectMocks
    private PessoaServices service;

    @Mock
    private RestClient restClient;

    @Mock
    private PessoaRepository repository;

    private Endereco endereco;

    private Pessoa pessoa;

    private PessoaDTO pessoaDTO;

    @BeforeEach
    public void setUp() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date nascimento = sdf.parse("01/01/2000");
        this.restClient = RestClient.builder().build();

        endereco = new Endereco("13063580","Rua Martín Luther King Junior", "Jardim Eulina", "Campinas", "SP", "19");

        pessoa = Pessoa.builder()
                .id(1L)
                .nome("Nome Teste")
                .cpf("012.696.448-31")
                .dataNascimento(nascimento)
                .email("email@email.com")
                .endereco(endereco)
                .build();

        pessoaDTO = new PessoaDTO(
                "Nome Teste",
                "459.827.228-71",
                "01/01/2000",
                "email@email.com",
                "13063580"
        );
    }

    @Test
    void testDomain() {

        String nome = FakeFactory.pessoa().getNome();
        String cpf = FakeFactory.pessoa().getCpf();
        Date nascimento = FakeFactory.pessoa().getDataNascimento();
        String email = FakeFactory.pessoa().getEmail();
        String cep = "13063580";

        endereco = restClient
                .get()
                .uri("https://viacep.com.br/ws/{cep}", cep + "/json/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Endereco.class);

        Pessoa pessoa = new Pessoa(null, nome, cpf, nascimento, email, endereco);

        repository.save(pessoa);

        assertNotNull(pessoa);
        assertEquals(nome, pessoa.getNome());
        assertEquals(cpf, pessoa.getCpf());
        assertEquals(nascimento, pessoa.getDataNascimento());
        assertEquals(email, pessoa.getEmail());
        assertEquals("13063-580", pessoa.getEndereco().getCep());
    }

    /*@Test
    public void testPersist() {

        when(repository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaResponse response = service.persist(pessoaDTO);

        assertNotNull(response);
        assertEquals(pessoaDTO.nome(), response.nome());
        assertEquals(pessoaDTO.cpf(), response.cpf());
        assertEquals("2000-01-01", response.dataNascimento());
        assertEquals(pessoaDTO.email(), response.email());
        assertEquals(pessoaDTO.cep(), response.endereco().getCep());

        assertThat(response.nome()).isNotNull();
        assertThat(response.cpf()).isNotNull();
        assertThat(response.dataNascimento()).isNotNull();
        assertThat(response.email()).isNotNull();
        assertThat(response.endereco().getCep()).isNotNull();

        verify(repository, times(1)).findByPessoa(response.cpf());

        verify(repository, times(1)).save(any(Pessoa.class));

        verifyNoMoreInteractions(repository);
    }*/

    @Test
    public void testFindByID() {

        when(repository.findById(anyLong())).thenReturn(Optional.of(pessoa));

        PessoaResponse response = service.findByID(FakeFactory.pessoa().getId().toString());

        assertNotNull(response);
        assertEquals(pessoa.getNome(), response.nome());
        assertEquals(pessoa.getCpf(), response.cpf());
        assertEquals(pessoa.getDataNascimento().toString(), response.dataNascimento());
        assertEquals(pessoa.getEmail(), response.email());

        assertThat(response.nome()).isNotNull();
        assertThat(response.cpf()).isNotNull();
        assertThat(response.dataNascimento()).isNotNull();
        assertThat(response.email()).isNotNull();

        verify(repository, times(1)).findById(anyLong());

        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindWithParameterIDError() {
        String id = "A";
        assertThrows(ValidateParameterException.class, () -> service.findByID(id));
    }

    @Test
    void testPessoaNotFound() {
        String id = "100";
        assertThrows(RegraDeNegocioException.class, () -> service.findByID(id));
    }

    @Test
    void testDeleteThrowsRegraDeNegocioException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class, () -> service.delete("1"));
    }

    @Test
    public void testList() {
        when(repository.findAll()).thenReturn(Collections.singletonList(pessoa));

        List<PessoaResponse> result = service.list();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pessoa.getNome(), result.get(0).nome());
        assertEquals(pessoa.getCpf(), result.get(0).cpf());
        assertEquals(pessoa.getDataNascimento().toString(), result.get(0).dataNascimento());
        assertEquals(pessoa.getEmail(), result.get(0).email());

        verify(repository, times(1)).findAll();

        verifyNoMoreInteractions(repository);
    }

   /* @Test
    public void testFindPessoaByNome() {
        when(repository.findPessoaByNome(anyString())).thenReturn(Collections.singletonList(pessoa));

        List<PessoaDTO> result = service.findPessoaByNome(pessoaDTO.nome());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pessoa.getNome(), result.get(0).nome());
        assertEquals(pessoa.getCpf(), result.get(0).cpf());
        assertEquals(pessoa.getDataNascimento().toString(), result.get(0).dataNascimento());
        assertEquals(pessoa.getEmail(), result.get(0).email());

        assertThat(result.get(0).nome()).isNotNull();
        assertThat(result.get(0).cpf()).isNotNull();
        assertThat(result.get(0).dataNascimento()).isNotNull();
        assertThat(result.get(0).email()).isNotNull();

        verify(repository, times(1)).findPessoaByNome(pessoa.getNome());

        verifyNoMoreInteractions(repository);
    }*/

   /* @Test
    public void testUpdate() {

        when(repository.findById(pessoa.getId())).thenReturn(Optional.of(pessoa));
        when(repository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaDTO result = service.update("1", pessoaDTO);

        assertNotNull(result);
        assertEquals(pessoa.getNome(), result.nome());
        assertEquals(pessoa.getCpf(), result.cpf());
        assertEquals(pessoa.getDataNascimento().toString(), result.dataNascimento());
        assertEquals(pessoa.getEmail(), result.email());
        assertEquals(pessoa.getEndereco().getCep(), result.cep());

        verify(repository, times(1)).save(pessoa);

        verifyNoMoreInteractions(repository);
    }*/

    @Test
    public void testDelete() {

        Long id = ValidateParameter.validate(pessoa.getId().toString());

        when(repository.findById(id)).thenReturn(Optional.of(pessoa));

        String result = service.delete(pessoa.getId().toString());

        assertEquals("Pessoa Removida", result);

        verify(repository, times(1)).deleteById(id);

        verifyNoMoreInteractions(repository);
    }

}
