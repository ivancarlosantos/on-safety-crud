package ics.on_safety.desafio.crud.api;

import ics.on_safety.desafio.crud.dto.PessoaDTO;
import ics.on_safety.desafio.crud.service.PessoaServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class PessoaController {

    private final PessoaServices services;

    @PostMapping(path = "/save")
    public ResponseEntity<PessoaDTO> persist(@RequestBody @Valid PessoaDTO dto) throws ParseException {
        return ResponseEntity.status(HttpStatus.CREATED).body(services.persist(dto));
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<PessoaDTO> update(@PathVariable String id, @Valid @RequestBody PessoaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(services.update(id, dto));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<PessoaDTO>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(services.list());
    }

    @GetMapping(path = "/search/{id}")
    public ResponseEntity<PessoaDTO> findPessoaByID(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(services.findByID(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<PessoaDTO>> findPessoa(@RequestParam(value = "nome") String nome) {
        return ResponseEntity.status(HttpStatus.FOUND).body(services.findPessoaByNome(nome));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(services.delete(id));
    }
}
