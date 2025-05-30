package com.simple.config.controller;

import com.simple.config.dto.EtapaProcessoResponse;
import com.simple.config.service.EtapaProcessoService;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class EtapaProcessoControllerTest {

    @Mock
    private EtapaProcessoService etapaProcessoService;

    @InjectMocks
    private EtapaProcessoController etapaProcessoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private EtapaProcessoResponse etapaResponse1;
    private EtapaProcessoResponse etapaResponse2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(etapaProcessoController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Important for LocalDateTime serialization

        etapaResponse1 = EtapaProcessoResponse.builder()
                .id(1)
                .codigo("ETP001")
                .nome("Etapa Inicial")
                .perfilResponsavelNome("Analista")
                .criadoEm(LocalDateTime.now())
                .build();
        etapaResponse2 = EtapaProcessoResponse.builder()
                .id(2)
                .codigo("ETP002")
                .nome("Etapa Intermediária")
                .criadoEm(LocalDateTime.now())
                .build();

    }

    @Test
    void findById_whenEtapaExists_shouldReturnEtapaResponse() throws Exception {
        when(etapaProcessoService.findById(1)).thenReturn(etapaResponse1);

        mockMvc.perform(get("/etapas-processo/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Etapa Inicial")))
                .andExpect(jsonPath("$.perfilResponsavelNome", is("Analista")));
    }

    @Test
    void findById_whenEtapaNotFound_shouldReturnNotFound() throws Exception {
        when(etapaProcessoService.findById(1)).thenThrow(new EntityNotFoundException("Etapa não encontrada"));

        mockMvc.perform(get("/etapas-processo/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_whenServiceThrowsUnexpectedException_shouldReturnInternalServerError() throws Exception {
        when(etapaProcessoService.findById(1)).thenThrow(new RuntimeException("Unexpected internal error"));

        mockMvc.perform(get("/etapas-processo/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAll_shouldReturnListOfEtapaResponse() throws Exception {
        List<EtapaProcessoResponse> listResponse = Arrays.asList(etapaResponse1, etapaResponse2);
        when(etapaProcessoService.findAll()).thenReturn(listResponse);

        mockMvc.perform(get("/etapas-processo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("Etapa Inicial")))
                .andExpect(jsonPath("$[1].nome", is("Etapa Intermediária")));
    }

    @Test
    void findByTipoServicoId_shouldReturnListOfEtapaResponse() throws Exception {
        Integer tipoServicoId = 10;
        List<EtapaProcessoResponse> listResponse = Arrays.asList(etapaResponse1); // Assume etapaResponse1 is for tipoServicoId 10
        etapaResponse1.setTipoServicoId(tipoServicoId);

        when(etapaProcessoService.findByTipoServicoId(tipoServicoId)).thenReturn(listResponse);

        mockMvc.perform(get("/etapas-processo/tipo-servico/{tipoServicoId}", tipoServicoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(etapaResponse1.getId())))
                .andExpect(jsonPath("$[0].nome", is(etapaResponse1.getNome())));
    }

    @Test
    void findByTipoServicoId_whenNoEtapasFound_shouldReturnOkWithEmptyList() throws Exception {
        Integer tipoServicoId = 99; // Assuming no etapas for this ID
        when(etapaProcessoService.findByTipoServicoId(tipoServicoId)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/etapas-processo/tipo-servico/{tipoServicoId}", tipoServicoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
