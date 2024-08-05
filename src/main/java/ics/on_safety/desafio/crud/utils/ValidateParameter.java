package ics.on_safety.desafio.crud.utils;

import ics.on_safety.desafio.crud.exception.ValidateParameterException;

public class ValidateParameter {

    public static Long validate(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new ValidateParameterException("Parâmetro Inválido " + ex.getMessage());
        }
    }
}
