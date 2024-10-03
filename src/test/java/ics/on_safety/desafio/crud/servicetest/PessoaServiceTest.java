package ics.on_safety.desafio.crud.servicetest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ics.on_safety.desafio.crud.dto.PessoaDTO;
import ics.on_safety.desafio.crud.exception.RegraDeNegocioException;
import ics.on_safety.desafio.crud.exception.ValidateParameterException;
import ics.on_safety.desafio.crud.factory.FakeFactory;
import ics.on_safety.desafio.crud.model.Pessoa;
import ics.on_safety.desafio.crud.repository.PessoaRepository;
import ics.on_safety.desafio.crud.service.PessoaServices;
import ics.on_safety.desafio.crud.utils.ValidateParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
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

    @Autowired
    ObjectMapper mapper;

    @Test
    void testDomain() throws ParseException {

        Long id = FakeFactory.pessoa().getId();
        String nome = FakeFactory.pessoa().getNome();
        String cpf = FakeFactory.pessoa().getCpf();
        String nascimento = FakeFactory.pessoa().getDataNascimento();
        String email = FakeFactory.pessoa().getEmail();

        Pessoa pessoa = new Pessoa(id, nome, cpf, nascimento, email);
        PessoaDTO dto = new PessoaDTO(pessoa.getNome(), pessoa.getCpf(), pessoa.getDataNascimento(), pessoa.getEmail());

        repository.save(pessoa);

        assertNotNull(pessoa);
        assertNotNull(dto);
        assertEquals(nome, pessoa.getNome());
        assertEquals(cpf, pessoa.getCpf());
        assertEquals(nascimento, pessoa.getDataNascimento());
        assertEquals(email, pessoa.getEmail());

        verify(repository).save(pessoa);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void savedTest() throws ParseException {

        Pessoa pessoa = new Pessoa(FakeFactory.pessoa().getId(), FakeFactory.pessoa().getNome(), FakeFactory.pessoa().getCpf(), FakeFactory.pessoa().getDataNascimento(), FakeFactory.pessoa().getEmail());
        PessoaDTO dto = new PessoaDTO(pessoa.getNome(), pessoa.getCpf(), pessoa.getDataNascimento(), pessoa.getEmail());
        Pessoa savedPessoa = new Pessoa(1L, dto.nome(), dto.cpf(), dto.dataNascimento(), dto.email());

        when(repository.save(any(Pessoa.class))).thenReturn(savedPessoa);

        PessoaDTO result = service.persist(dto);

        assertNotNull(result);
        assertEquals(savedPessoa.getNome(), result.nome());
        assertEquals(savedPessoa.getCpf(), result.cpf());
        assertEquals(savedPessoa.getDataNascimento(), result.dataNascimento());
        assertEquals(savedPessoa.getEmail(), result.email());

        verify(repository, times(1)).save(any(Pessoa.class));

        verify(repository, times(1)).findByPessoa(pessoa.getCpf());

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testFindByID() throws ParseException {

        Pessoa pessoa = new Pessoa(FakeFactory.pessoa().getId(), FakeFactory.pessoa().getNome(), FakeFactory.pessoa().getCpf(), FakeFactory.pessoa().getDataNascimento(), FakeFactory.pessoa().getEmail());
        PessoaDTO dto = new PessoaDTO(pessoa.getNome(), pessoa.getCpf(), pessoa.getDataNascimento(), pessoa.getEmail());

        when(repository.findById(anyLong())).thenReturn(Optional.of(pessoa));

        PessoaDTO response = service.findByID(FakeFactory.pessoa().getId().toString());

        assertNotNull(response);
        assertEquals(pessoa.getNome(), response.nome());
        assertEquals(pessoa.getCpf(), response.cpf());
        assertEquals(pessoa.getDataNascimento(), response.dataNascimento());
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
    public void testList() throws ParseException {

        Pessoa pessoa = new Pessoa(FakeFactory.pessoa().getId(), FakeFactory.pessoa().getNome(), FakeFactory.pessoa().getCpf(), FakeFactory.pessoa().getDataNascimento(), FakeFactory.pessoa().getEmail());

        when(repository.findAll()).thenReturn(Collections.singletonList(pessoa));

        List<PessoaDTO> result = service.list();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pessoa.getNome(), result.get(0).nome());
        assertEquals(pessoa.getCpf(), result.get(0).cpf());
        assertEquals(pessoa.getDataNascimento(), result.get(0).dataNascimento());
        assertEquals(pessoa.getEmail(), result.get(0).email());

        verify(repository, times(1)).findAll();

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testFindPessoaByNome() throws ParseException {

        Pessoa pessoa = new Pessoa(FakeFactory.pessoa().getId(), FakeFactory.pessoa().getNome(), FakeFactory.pessoa().getCpf(), FakeFactory.pessoa().getDataNascimento(), FakeFactory.pessoa().getEmail());
        PessoaDTO dto = new PessoaDTO(pessoa.getNome(), pessoa.getCpf(), pessoa.getDataNascimento(), pessoa.getEmail());

        when(repository.findPessoaByNome(anyString())).thenReturn(Collections.singletonList(pessoa));

        List<PessoaDTO> result = service.findPessoaByNome(dto.nome());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pessoa.getNome(), result.get(0).nome());
        assertEquals(pessoa.getCpf(), result.get(0).cpf());
        assertEquals(pessoa.getDataNascimento(), result.get(0).dataNascimento());
        assertEquals(pessoa.getEmail(), result.get(0).email());

        assertThat(result.get(0).nome()).isNotNull();
        assertThat(result.get(0).cpf()).isNotNull();
        assertThat(result.get(0).dataNascimento()).isNotNull();
        assertThat(result.get(0).email()).isNotNull();

        verify(repository, times(1)).findPessoaByNome(pessoa.getNome());

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testUpdate() throws ParseException {

        Pessoa pessoa = new Pessoa(FakeFactory.pessoa().getId(), FakeFactory.pessoa().getNome(), FakeFactory.pessoa().getCpf(), FakeFactory.pessoa().getDataNascimento(), FakeFactory.pessoa().getEmail());
        PessoaDTO dto = new PessoaDTO(pessoa.getNome(), pessoa.getCpf(), pessoa.getDataNascimento(), pessoa.getEmail());

        when(repository.findById(pessoa.getId())).thenReturn(Optional.of(pessoa));
        when(repository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaDTO result = service.update("1", dto);

        assertNotNull(result);
        assertEquals(pessoa.getNome(), result.nome());
        assertEquals(pessoa.getCpf(), result.cpf());
        assertEquals(pessoa.getDataNascimento(), result.dataNascimento());
        assertEquals(pessoa.getEmail(), result.email());

        verify(repository, times(1)).save(pessoa);

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testDelete() throws ParseException {

        Pessoa pessoa = new Pessoa(FakeFactory.pessoa().getId(), FakeFactory.pessoa().getNome(), FakeFactory.pessoa().getCpf(), FakeFactory.pessoa().getDataNascimento(), FakeFactory.pessoa().getEmail());

        Long id = ValidateParameter.validate(pessoa.getId().toString());

        when(repository.findById(id)).thenReturn(Optional.of(pessoa));

        String result = service.delete(pessoa.getId().toString());

        assertEquals("Pessoa Removida", result);

        verify(repository, times(1)).deleteById(id);

        verifyNoMoreInteractions(repository);
    }
}
