package ics.on_safety.desafio.crud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;

@Slf4j
@SpringBootApplication
public class CrudOnSafetyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudOnSafetyApplication.class, args);
        log.info("Application UP {}", HttpStatus.OK);
    }
}
