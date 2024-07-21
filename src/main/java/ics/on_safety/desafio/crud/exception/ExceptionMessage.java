package ics.on_safety.desafio.crud.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class ExceptionMessage {

    private Integer statusNumber;
    private HttpStatus errorStatus;
    private String timestamp;
    private List<String> messages;

    public ExceptionMessage(Integer statusNumber, HttpStatus errorStatus, Date timeError, List<String> messages) {
        this.statusNumber = statusNumber;
        this.errorStatus = errorStatus;
        this.timestamp = timeError.toString();
        this.messages = messages;
    }
}
