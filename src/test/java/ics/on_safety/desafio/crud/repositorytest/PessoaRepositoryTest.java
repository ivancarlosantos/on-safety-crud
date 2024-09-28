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

import java.text.ParseException;
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
    void save() throws ParseException {
        Pessoa p = created();
        Pessoa save = this.repository.save(p);

        assertThat(save).isNotNull();
        assertThat(save.getId()).isNotNull();
        assertThat(save.getNome()).isEqualTo(p.getNome());
    }

    @Test
    void findPessoaByNome() throws ParseException {
        List<Pessoa> p = repository.findPessoaByNome(created().getNome());

        assertThat(p).isNotNull();
    }

    @Test
    void findByPessoa() throws ParseException {
        List<Pessoa> p = repository.findPessoaByNome(created().getNome());
        assertThat(p).isNotNull();
    }

    private static Pessoa created() throws ParseException {
        return new Pessoa(
                FakeFactory.pessoa().getId(),
                FakeFactory.pessoa().getNome(),
                FakeFactory.pessoa().getCpf(),
                FakeFactory.pessoa().getDataNascimento(),
                FakeFactory.pessoa().getEmail());
    }
}