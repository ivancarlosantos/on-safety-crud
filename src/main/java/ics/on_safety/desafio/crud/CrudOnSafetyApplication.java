package ics.on_safety.desafio.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;


@SpringBootApplication
public class CrudOnSafetyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudOnSafetyApplication.class, args);
        System.out.println("Application UP [" + HttpStatus.OK + "]");
    }
}
