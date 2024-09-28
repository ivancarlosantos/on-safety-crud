package ics.on_safety.desafio.crud.factory;

import com.github.javafaker.Faker;
import ics.on_safety.desafio.crud.model.Pessoa;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FakeFactory {

    private static final Faker faker = new Faker();

    public static Pessoa pessoa() throws ParseException {

        return new Pessoa(
                1L,
                faker.dragonBall().character(),
                "877.930.068-52",
                "01/01/2000",
                faker.internet().emailAddress());
    }
}
