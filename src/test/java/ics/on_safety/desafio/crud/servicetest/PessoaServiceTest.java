package ics.on_safety.desafio.crud.servicetest;

import ics.on_safety.desafio.crud.dto.PessoaDTO;
import ics.on_safety.desafio.crud.exception.RegraDeNegocioException;
import ics.on_safety.desafio.crud.exception.ValidateParameterException;
import ics.on_safety.desafio.crud.factory.FakeFactory;
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

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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
    private PessoaRepository repository;

    private Pessoa pessoa;

    private PessoaDTO pessoaDTO;

    @BeforeEach
    public void setUp() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ld = LocalDate.parse("01/01/2000", dtf);

        pessoa = Pessoa.builder()
                .id(1L)
                .nome("Nome Teste")
                .cpf("012.696.448-31")
                .dataNascimento(ld)
                .email("email@email.com")
                .build();

        pessoaDTO = new PessoaDTO(
                "Nome Teste",
                "459.827.228-71",
                "01/01/2000",
                "email@email.com"
        );
    }

    @Test
    void testDomain() {

        String nome = FakeFactory.pessoa().getNome();
        String cpf = FakeFactory.pessoa().getCpf();
        LocalDate nasc = FakeFactory.pessoa().getDataNascimento();
        String email = FakeFactory.pessoa().getEmail();

        Pessoa pessoa = new Pessoa(null, nome, cpf, nasc, email);

        repository.save(pessoa);

        assertNotNull(pessoa);
        assertEquals(nome, pessoa.getNome());
        assertEquals(cpf, pessoa.getCpf());
        assertEquals(nasc, pessoa.getDataNascimento());
        assertEquals(email, pessoa.getEmail());
    }

    @Test
    public void testPersist() throws ParseException {

        when(repository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaDTO dto = service.persist(pessoaDTO);

        assertNotNull(dto);
        assertEquals(pessoaDTO.nome(), dto.nome());
        assertEquals(pessoaDTO.cpf(), dto.cpf());
        assertEquals("2000-01-01", dto.dataNascimento());
        assertEquals(pessoaDTO.email(), dto.email());

        assertThat(dto.nome()).isNotNull();
        assertThat(dto.cpf()).isNotNull();
        assertThat(dto.dataNascimento()).isNotNull();
        assertThat(dto.email()).isNotNull();

        verify(repository, times(1)).findByPessoa(dto.cpf());

        verify(repository, times(1)).save(any(Pessoa.class));

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testFindByID() {

        when(repository.findById(anyLong())).thenReturn(Optional.of(pessoa));

        PessoaDTO dto = service.findByID(FakeFactory.pessoa().getId().toString());

        assertNotNull(dto);
        assertEquals(pessoa.getNome(), dto.nome());
        assertEquals(pessoa.getCpf(), dto.cpf());
        assertEquals(pessoa.getDataNascimento().toString(), dto.dataNascimento());
        assertEquals(pessoa.getEmail(), dto.email());

        assertThat(dto.nome()).isNotNull();
        assertThat(dto.cpf()).isNotNull();
        assertThat(dto.dataNascimento()).isNotNull();
        assertThat(dto.email()).isNotNull();

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
    public void testList() {
        when(repository.findAll()).thenReturn(Collections.singletonList(pessoa));

        List<PessoaDTO> result = service.list();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pessoa.getNome(), result.get(0).nome());
        assertEquals(pessoa.getCpf(), result.get(0).cpf());
        assertEquals(pessoa.getDataNascimento().toString(), result.get(0).dataNascimento());
        assertEquals(pessoa.getEmail(), result.get(0).email());

        verify(repository, times(1)).findAll();

        verifyNoMoreInteractions(repository);
    }

    @Test
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
    }

    @Test
    public void testUpdate() {

        when(repository.findById(pessoa.getId())).thenReturn(Optional.of(pessoa));
        when(repository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaDTO result = service.update("1", pessoaDTO);

        assertNotNull(result);
        assertEquals(pessoa.getNome(), result.nome());
        assertEquals(pessoa.getCpf(), result.cpf());
        assertEquals(pessoa.getDataNascimento().toString(), result.dataNascimento());
        assertEquals(pessoa.getEmail(), result.email());

        verify(repository, times(1)).save(pessoa);

        verifyNoMoreInteractions(repository);
    }

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
