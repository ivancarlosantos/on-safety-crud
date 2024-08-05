package ics.on_safety.desafio.crud.repository;

import ics.on_safety.desafio.crud.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    @Query(value = "SELECT * FROM pessoa u WHERE u.nome LIKE %?1%", nativeQuery = true)
    List<Pessoa> findPessoaByNome(@Param("nome") String nome);

    @Query(value = "SELECT u FROM Pessoa u WHERE u.cpf=:cpf")
    Pessoa findByPessoaPorCPF(@Param("cpf") String cpf);
}
