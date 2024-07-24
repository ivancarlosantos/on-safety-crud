package ics.on_safety.desafio.crud.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PessoaDTO(

        @NotBlank(message = "O nome não deve ser null e/ou vazio")
        @NotNull
        String nome,

        @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}")
        @CPF
        @NotBlank(message = "O CPF não deve ser null e/ou vazio")
        @NotNull
        String cpf,

        @NotNull(message = "Inserir sua data de nascimento")
        String dataNascimento,

        @NotBlank(message = "O e-mail não deve ser null e/ou vazio")
        @NotEmpty
        @Email
        String email) {
}
