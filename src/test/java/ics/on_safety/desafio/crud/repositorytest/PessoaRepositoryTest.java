package ics.on_safety.desafio.crud.repositorytest;

import ics.on_safety.desafio.crud.factory.FakeFactory;
import ics.on_safety.desafio.crud.model.Pessoa;
import ics.on_safety.desafio.crud.repository.PessoaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class PessoaRepositoryTest {

    @Autowired
    private PessoaRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void save(){
        Pessoa p = created();
        Pessoa save = this.repository.save(p);

        assertThat(save).isNotNull();
        assertThat(save.getId()).isNotNull();
        assertThat(save.getNome()).isEqualTo(p.getNome());
    }

    @Test
    void findPessoaByNome() {
        List<Pessoa> p = repository.findPessoaByNome(created().getNome());

        assertThat(p).isNotNull();
    }

    @Test
    void findByPessoa() {
        List<Pessoa> p = repository.findPessoaByNome(created().getNome());
        assertThat(p).isNotNull();
    }

    private static Pessoa created() {
        return Pessoa
                .builder()
                .nome(FakeFactory.pessoa().getNome())
                .cpf(FakeFactory.pessoa().getCpf())
                .dataNascimento(FakeFactory.pessoa().getDataNascimento())
                .email(FakeFactory.pessoa().getEmail())
                .endereco(FakeFactory.pessoa().getEndereco())
                .build();
    }
}