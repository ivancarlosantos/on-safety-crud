package ics.on_safety.desafio.crud.service;

import ics.on_safety.desafio.crud.dto.PessoaDTO;
import ics.on_safety.desafio.crud.exception.DataViolationException;
import ics.on_safety.desafio.crud.exception.RegraDeNegocioException;
import ics.on_safety.desafio.crud.model.Pessoa;
import ics.on_safety.desafio.crud.repository.PessoaRepository;
import ics.on_safety.desafio.crud.utils.ValidateParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PessoaServices {

    private final PessoaRepository repository;

    public PessoaDTO persist(PessoaDTO dto) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ld = LocalDate.parse(dto.dataNascimento(), dtf);

        Pessoa p = Pessoa.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .dataNascimento(ld)
                .email(dto.email())
                .build();

        if (findByPessoaPorCPF(dto) != null) {
            throw new DataViolationException("[PESSOA/CPF JÁ CADASTRADO]");
        }

        repository.save(p);

        return new PessoaDTO(p.getNome(), p.getCpf(), p.getDataNascimento().toString(), p.getEmail());
    }

    public PessoaDTO findByID(String value) {
        Long id = ValidateParameter.validate(value);
        return repository
                .findById(id)
                .map(p -> new PessoaDTO(p.getNome(), p.getCpf(), p.getDataNascimento().toString(), p.getEmail()))
                .orElseThrow(() -> new RegraDeNegocioException("Pessoa Não Encontrada"));
    }

    public List<PessoaDTO> list() {
        return repository
                .findAll()
                .stream()
                .map(p -> new PessoaDTO(p.getNome(), p.getCpf(), p.getDataNascimento().toString(), p.getEmail()))
                .toList();
    }

    public List<PessoaDTO> findPessoaByNome(String nome) {

        return repository
                .findPessoaByNome(nome)
                .stream()
                .map(p -> new PessoaDTO(p.getNome(), p.getCpf(), p.getDataNascimento().toString(), p.getEmail()))
                .toList();
    }

    public PessoaDTO update(String id, PessoaDTO dto) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ld = LocalDate.parse(dto.dataNascimento(), dtf);

        Pessoa pessoa = findID(id);
        pessoa.setNome(dto.nome());
        pessoa.setCpf(dto.cpf());
        pessoa.setDataNascimento(ld);
        pessoa.setEmail(dto.email());

        repository.save(pessoa);

        return new PessoaDTO(pessoa.getNome(), pessoa.getCpf(), pessoa.getDataNascimento().toString(), pessoa.getEmail());
    }

    public String delete(String value) {

        Long id = ValidateParameter.validate(value);
        Pessoa pessoa = repository
                .findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Pessoa Não Encontrada"));

        repository.deleteById(pessoa.getId());

        return "Pessoa Removida";
    }

    private Pessoa findID(String value) {

        Long id = ValidateParameter.validate(value);
        Optional<Pessoa> findID = Optional
                .ofNullable(repository
                        .findById(id)
                        .orElseThrow(() -> new RegraDeNegocioException("Pessoa Não Encontrada")));

        Pessoa model = null;

        model = findID.get();

        return model;
    }

    private PessoaDTO findByPessoaPorCPF(PessoaDTO dto) {
        Pessoa p = repository.findByPessoaPorCPF(dto.cpf());
        if (p != null) {
            return new PessoaDTO(p.getNome(), p.getCpf(), p.getDataNascimento().toString(), p.getEmail());
        }
        return null;
    }
}