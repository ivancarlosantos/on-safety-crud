package ics.on_safety.desafio.crud.exception;


public class DataViolationException extends RuntimeException {

    public DataViolationException(String message) {
        super(message);
    }
}
