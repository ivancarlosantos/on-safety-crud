package ics.on_safety.desafio.crud.dto;

import ics.on_safety.desafio.crud.model.Endereco;

public record PessoaResponse(

        String nome,

        String cpf,

        String dataNascimento,

        String email,

        Endereco endereco){}
