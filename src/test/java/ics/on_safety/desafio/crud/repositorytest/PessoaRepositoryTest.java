package ics.on_safety.desafio.crud.repositorytest;

import ics.on_safety.desafio.crud.factory.FakeFactory;
import ics.on_safety.desafio.crud.model.Pessoa;
import ics.on_safety.desafio.crud.repository.PessoaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

    @Test
    void save() throws ParseException {
        Pessoa p = created();
        Pessoa save = this.repository.save(p);

        assertThat(save).isNotNull();
        assertThat(save.getId()).isNotNull();
        assertThat(save.getNome()).isEqualTo(p.getNome());
    }

    @Test
    void listAllTest() throws ParseException {
        Pessoa p1 = created();
        Pessoa p2 = created();

        System.out.println(p1);
        System.out.println(p2);

        List<Pessoa> saveAll = this.repository.saveAll(List.of(p1, p2));

        assertThat(saveAll).isNotNull();
        Assertions.assertEquals(2, saveAll.size());
    }

    @Test
    void findPessoaByNome() throws ParseException {
        List<Pessoa> p = repository.findPessoaByNome(created().getNome());

        assertThat(p).isNotNull();
    }

    @Test
    void findByPessoa() throws ParseException {
        Pessoa p = created();
        Pessoa save = this.repository.save(p);
        Pessoa exist = this.repository.findByPessoa(save.getCpf());
        assertThat(exist.getCpf()).isNotNull();
        assertThat(p.getCpf()).isEqualTo(exist.getCpf());
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