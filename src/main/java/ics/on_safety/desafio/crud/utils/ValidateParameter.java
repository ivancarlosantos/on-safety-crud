package ics.on_safety.desafio.crud.utils;

public class ValidateParameter {

    private ValidateParameter() {}

    public static Long validate(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Parâmetro Inválido");
        }
    }
}
