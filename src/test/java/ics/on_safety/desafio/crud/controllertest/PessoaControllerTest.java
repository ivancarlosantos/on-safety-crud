package ics.on_safety.desafio.crud.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ics.on_safety.desafio.crud.api.PessoaController;
import ics.on_safety.desafio.crud.dto.PessoaDTO;
import ics.on_safety.desafio.crud.dto.PessoaResponse;
import ics.on_safety.desafio.crud.factory.FakeFactory;
import ics.on_safety.desafio.crud.service.PessoaServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(PessoaController.class)
public class PessoaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PessoaServices service;

    @Autowired
    ObjectMapper mapper;


    @Test
    void persist() throws Exception {

        PessoaDTO dto = new PessoaDTO(FakeFactory.pessoa().getNome(), "877.930.068-52", "01/01/2000", FakeFactory.pessoa().getEmail(), FakeFactory.pessoa().getEndereco().toString());
        PessoaResponse res = new PessoaResponse(FakeFactory.pessoa().getNome(), "877.930.068-52", "01/01/2000", FakeFactory.pessoa().getEmail(), FakeFactory.pessoa().getEndereco());

        when(service.persist(dto)).thenReturn(res);

        String response = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(response))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(res.nome()))
                .andExpect(jsonPath("$.cpf").value(res.cpf()))
                .andExpect(jsonPath("$.dataNascimento").value(res.dataNascimento()))
                .andExpect(jsonPath("$.email").value(res.email()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUpdate() throws Exception {

        PessoaResponse resp = new PessoaResponse(FakeFactory.pessoa().getNome(), "877.930.068-52", "01/01/2000", FakeFactory.pessoa().getEmail(), FakeFactory.pessoa().getEndereco());
        PessoaDTO dto = new PessoaDTO(FakeFactory.pessoa().getNome(), "877.930.068-52", "01/01/2000", FakeFactory.pessoa().getEmail(), FakeFactory.pessoa().getEndereco().getCep());

        when(service.update("1", dto)).thenReturn(resp);

        String response = mapper.writeValueAsString(dto);

        System.out.println(response);

        mockMvc.perform(put("/api/update/" + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(response))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(resp.nome()))
                .andExpect(jsonPath("$.cpf").value(resp.cpf()))
                .andExpect(jsonPath("$.dataNascimento").value(resp.dataNascimento()))
                .andExpect(jsonPath("$.email").value(resp.email()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testList() throws Exception {

        PessoaResponse dto1 = new PessoaResponse(
                FakeFactory.pessoa().getNome(),
                "123.456.789-00",
                "01/01/2000",
                FakeFactory.pessoa().getEmail(),
                FakeFactory.pessoa().getEndereco()
        );

        PessoaResponse dto2 = new PessoaResponse(
                FakeFactory.pessoa().getNome(),
                "123.456.789-00",
                "01/01/2000",
                FakeFactory.pessoa().getEmail(),
                FakeFactory.pessoa().getEndereco()
        );

        when(service.list()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(dto1.nome()))
                .andExpect(jsonPath("$[0].cpf").value(dto1.cpf()))
                .andExpect(jsonPath("$[0].dataNascimento").value(dto1.dataNascimento()))
                .andExpect(jsonPath("$[0].email").value(dto1.email()))

                .andExpect(jsonPath("$[1].nome").value(dto2.nome()))
                .andExpect(jsonPath("$[1].cpf").value(dto2.cpf()))
                .andExpect(jsonPath("$[1].dataNascimento").value(dto2.dataNascimento()))
                .andExpect(jsonPath("$[1].email").value(dto2.email()))

                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shouldGetPessoaByID() throws Exception {

        String id = "1";

        PessoaResponse dto = new PessoaResponse(
                FakeFactory.pessoa().getNome(),
                "877.930.068-52",
                "01/01/2000",
                FakeFactory.pessoa().getEmail(),
                FakeFactory.pessoa().getEndereco()
        );

        when(service.findByID(id)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/search/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.nome").value(dto.nome()))
                .andExpect(jsonPath("$.cpf").value(dto.cpf()))
                .andExpect(jsonPath("$.dataNascimento").value(dto.dataNascimento()))
                .andExpect(jsonPath("$.email").value(dto.email()));
    }

    @Test
    void testFindPessoa() throws Exception {

        PessoaResponse dto = new PessoaResponse(
                FakeFactory.pessoa().getNome(),
                "123.456.789-00",
                "01/01/2000",
                FakeFactory.pessoa().getEmail(),
                FakeFactory.pessoa().getEndereco()
        );

        when(service.findPessoaByNome(dto.nome())).thenReturn(List.of(dto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/find")
                        .param("nome", dto.nome()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$[0].nome", is(dto.nome())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shouldDeletePessoa() throws Exception {

        String id = "1";

        when(service.delete(id)).thenReturn(String.valueOf(""));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }
}
