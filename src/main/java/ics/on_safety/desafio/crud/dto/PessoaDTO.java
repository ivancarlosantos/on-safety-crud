package ics.on_safety.desafio.crud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record PessoaDTO(
        @NotBlank
        @NotNull
        String nome,

        @CPF
        @NotBlank
        @NotNull
        String cpf,

        @NotBlank
        @NotNull
        String dataNascimento,

        @NotBlank
        @NotEmpty
        @Email
        String email){}
