package ics.on_safety.desafio.crud.factory;

import com.github.javafaker.Faker;
import ics.on_safety.desafio.crud.model.Endereco;
import ics.on_safety.desafio.crud.model.Pessoa;

import java.time.LocalDate;
import java.util.Date;

public class FakeFactory {

    private static final Endereco endereco = new Endereco("13063240","Rua João Rodrigues Serra","Jardim Eulina","Campinas","SP","19");
    private static final Faker faker = new Faker();

    public static Pessoa pessoa(){

        return new Pessoa(
                faker.number().randomNumber(),
                faker.dragonBall().character(),
                "459.827.228-71",
                new Date("01/01/2000"),
                faker.internet().emailAddress(),
                endereco);
    }
}
