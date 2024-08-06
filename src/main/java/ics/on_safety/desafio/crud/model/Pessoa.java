package ics.on_safety.desafio.crud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "O nome não deve ser null e/ou vazio")
    @NotNull(message = "O nome não deve ser null e/ou vazio")
    private String nome;

    @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}")
    @CPF
    @NotBlank(message = "O CPF não deve ser null e/ou vazio")
    @NotNull(message = "O CPF não deve ser null e/ou vazio")
    private String cpf;

    @NotNull(message = "Inserir sua data de nascimento")
    private LocalDate dataNascimento;

    @NotBlank(message = "O e-mail não deve ser null e/ou vazio")
    @NotEmpty
    @Email
    private String email;

    @Embedded
    private Endereco endereco;
}
