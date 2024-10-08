package ics.on_safety.desafio.crud.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class PessoaExceptionHandler {

    @ExceptionHandler(value = RegraDeNegocioException.class)
    public ResponseEntity<ExceptionMessage> handlerExceptionBadRequest(RuntimeException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND,
                new Date(),
                List.of(ex.getMessage()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ExceptionMessage> handlerExceptionConflict(IllegalArgumentException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                new Date(),
                List.of(ex.getMessage()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ValidateParameterException.class})
    public ResponseEntity<ExceptionMessage> handlerExceptionValidationParameter(ValidateParameterException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                new Date(),
                List.of(ex.getMessage()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DataViolationException.class})
    public ResponseEntity<ExceptionMessage> handlerExceptionExistData(DataViolationException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT,
                new Date(),
                List.of(ex.getMessage()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ExceptionMessage> handlerExceptionFormatNumber(MethodArgumentTypeMismatchException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                new Date(),
                List.of(ex.getLocalizedMessage()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}