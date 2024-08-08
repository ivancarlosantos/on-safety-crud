package ics.on_safety.desafio.crud.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiErrors {

    private List<String> errors;

    public ApiErrors(String message) {
        this.errors = Arrays.asList(message);
    }
}
