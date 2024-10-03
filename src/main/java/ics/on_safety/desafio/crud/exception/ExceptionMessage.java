package ics.on_safety.desafio.crud.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

public class ExceptionMessage {

    private Integer statusNumber;
    private HttpStatus errorStatus;
    private String timestamp;
    private List<String> messages;

    public ExceptionMessage() {}

    public ExceptionMessage(Integer statusNumber, HttpStatus errorStatus, Date timeError, List<String> messages) {
        this.statusNumber = statusNumber;
        this.errorStatus = errorStatus;
        this.timestamp = timeError.toString();
        this.messages = messages;
    }

    public Integer getStatusNumber() {
        return statusNumber;
    }

    public HttpStatus getErrorStatus() {
        return errorStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<String> getMessages() {
        return messages;
    }
}
