package ics.on_safety.desafio.crud.factory;

import com.github.javafaker.Faker;
import ics.on_safety.desafio.crud.model.Pessoa;

import java.time.LocalDate;

public class FakeFactory {

    private static final Faker faker = new Faker();

    public static Pessoa pessoa(){

        return new Pessoa(
                faker.number().randomNumber(),
                faker.gameOfThrones().character(),
                "459.827.228-71",
                LocalDate.of(2000, 1, 1),
                faker.internet().emailAddress());
    }
}
