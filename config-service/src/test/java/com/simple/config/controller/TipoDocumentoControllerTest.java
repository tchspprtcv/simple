package com.simple.config.controller;

import com.simple.config.dto.TipoDocumentoResponse;
import com.simple.config.service.TipoDocumentoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TipoDocumentoControllerTest {

    @Mock
    private TipoDocumentoService tipoDocumentoService;

    @InjectMocks
    private TipoDocumentoController tipoDocumentoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private TipoDocumentoResponse docResponse1;
    private TipoDocumentoResponse docResponse2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tipoDocumentoController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        docResponse1 = TipoDocumentoResponse.builder()
                .id(1)
                .codigo("RG")
                .nome("Registro Geral")
                .ativo(true)
                .obrigatorio(true)
                .criadoEm(LocalDateTime.now())
                .build();
        docResponse2 = TipoDocumentoResponse.builder()
                .id(2)
                .codigo("CPF")
                .nome("Cadastro de Pessoa Física")
                .ativo(true)
                .obrigatorio(true)
                .criadoEm(LocalDateTime.now())
                .build();
    }

    @Test
    void findById_whenDocumentoExists_shouldReturnDocumentoResponse() throws Exception {
        when(tipoDocumentoService.findById(1)).thenReturn(docResponse1);

        mockMvc.perform(get("/tipos-documento/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Registro Geral")))
                .andExpect(jsonPath("$.codigo", is("RG")));
    }

    @Test
    void findById_whenDocumentoNotFound_shouldReturnNotFound() throws Exception {
        when(tipoDocumentoService.findById(1)).thenThrow(new EntityNotFoundException("Documento não encontrado"));

        mockMvc.perform(get("/tipos-documento/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_whenServiceThrowsUnexpectedException_shouldReturnInternalServerError() throws Exception {
        when(tipoDocumentoService.findById(1)).thenThrow(new RuntimeException("Unexpected internal error"));

        mockMvc.perform(get("/tipos-documento/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAll_whenNoFilter_shouldReturnAllDocumentos() throws Exception {
        List<TipoDocumentoResponse> listResponse = Arrays.asList(docResponse1, docResponse2);
        when(tipoDocumentoService.findAll(isNull())).thenReturn(listResponse);

        mockMvc.perform(get("/tipos-documento")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("Registro Geral")))
                .andExpect(jsonPath("$[1].nome", is("Cadastro de Pessoa Física")));
    }

    @Test
    void findAll_whenFilterAtivoIsTrue_shouldReturnAtivos() throws Exception {
        docResponse2.setAtivo(false); // Make one inactive for this test
        List<TipoDocumentoResponse> listResponse = Arrays.asList(docResponse1);
        when(tipoDocumentoService.findAll(true)).thenReturn(listResponse);

        mockMvc.perform(get("/tipos-documento")
                        .param("ativo", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Registro Geral")))
                .andExpect(jsonPath("$[0].ativo", is(true)));
    }

    @Test
    void findAll_whenFilterAtivoIsFalse_shouldReturnInativos() throws Exception {
        TipoDocumentoResponse docResponseInactive = TipoDocumentoResponse.builder().id(3).nome("Certidão Obito").ativo(false).build();
        List<TipoDocumentoResponse> listResponse = Arrays.asList(docResponseInactive);
        when(tipoDocumentoService.findAll(false)).thenReturn(listResponse);

        mockMvc.perform(get("/tipos-documento")
                        .param("ativo", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Certidão Obito")))
                .andExpect(jsonPath("$[0].ativo", is(false)));
    }

    @Test
    void findAll_whenServiceFails_shouldReturnInternalServerError() throws Exception {
        when(tipoDocumentoService.findAll(anyBoolean())).thenThrow(new RuntimeException("Service failure"));
         mockMvc.perform(get("/tipos-documento")
                        .param("ativo", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void findByCodigo_whenDocumentoExists_shouldReturnDocumentoResponse() throws Exception {
        when(tipoDocumentoService.findByCodigo("RG")).thenReturn(docResponse1);

        mockMvc.perform(get("/tipos-documento/codigo/{codigo}", "RG")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Registro Geral")))
                .andExpect(jsonPath("$.codigo", is("RG")));
    }

    @Test
    void findByCodigo_whenDocumentoNotFound_shouldReturnNotFound() throws Exception {
        when(tipoDocumentoService.findByCodigo("INEXISTENTE")).thenThrow(new EntityNotFoundException("Documento não encontrado"));

        mockMvc.perform(get("/tipos-documento/codigo/{codigo}", "INEXISTENTE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
