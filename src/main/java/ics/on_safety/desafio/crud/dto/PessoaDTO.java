package ics.on_safety.desafio.crud.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record PessoaDTO(
        @NotBlank
        @NotNull
        String nome,

        @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}")
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
