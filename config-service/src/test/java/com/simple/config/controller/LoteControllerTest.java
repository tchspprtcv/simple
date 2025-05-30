package com.simple.config.controller;

import com.simple.config.dto.LoteResponse;
import com.simple.config.service.LoteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoteControllerTest {

    @Mock
    private LoteService loteService;

    @InjectMocks
    private LoteController loteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private LoteResponse loteResponse1;
    private LoteResponse loteResponse2;
    private UUID lote1Id;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loteController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        lote1Id = UUID.randomUUID();
        loteResponse1 = LoteResponse.builder()
                .id(lote1Id)
                .codigo("L001")
                .quadra("Q1")
                .numero("N1")
                .area(new BigDecimal("360.50"))
                .valorBase(new BigDecimal("100000.00"))
                .criadoEm(LocalDateTime.now())
                .build();
        loteResponse2 = LoteResponse.builder()
                .id(UUID.randomUUID())
                .codigo("L002")
                .quadra("Q2")
                .numero("N2")
                .area(new BigDecimal("450.00"))
                .valorBase(new BigDecimal("120000.00"))
                .criadoEm(LocalDateTime.now())
                .build();
    }

    @Test
    void findById_whenLoteExists_shouldReturnLoteResponse() throws Exception {
        when(loteService.findById(lote1Id)).thenReturn(loteResponse1);

        mockMvc.perform(get("/lotes/{id}", lote1Id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lote1Id.toString())))
                .andExpect(jsonPath("$.codigo", is("L001")))
                .andExpect(jsonPath("$.area", is(360.50)));
    }

    @Test
    void findById_whenLoteNotFound_shouldReturnNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(loteService.findById(nonExistentId)).thenThrow(new EntityNotFoundException("Lote não encontrado"));

        mockMvc.perform(get("/lotes/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_whenServiceThrowsUnexpectedException_shouldReturnInternalServerError() throws Exception {
        UUID id = UUID.randomUUID();
        when(loteService.findById(id)).thenThrow(new RuntimeException("Unexpected internal error"));

        mockMvc.perform(get("/lotes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAll_paginated_shouldReturnPageOfLoteResponse() throws Exception {
        List<LoteResponse> lotesList = Arrays.asList(loteResponse1, loteResponse2);
        Page<LoteResponse> lotePage = new PageImpl<>(lotesList, PageRequest.of(0, 20), lotesList.size());
        when(loteService.findAll(any(Pageable.class))).thenReturn(lotePage);

        mockMvc.perform(get("/lotes")
                        .param("page", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].codigo", is("L001")))
                .andExpect(jsonPath("$.content[1].codigo", is("L002")))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    void findAll_whenServiceFails_shouldReturnInternalServerError() throws Exception {
        when(loteService.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Service failure"));
         mockMvc.perform(get("/lotes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findByCodigo_whenLoteExists_shouldReturnLoteResponse() throws Exception {
        when(loteService.findByCodigo("L001")).thenReturn(loteResponse1);

        mockMvc.perform(get("/lotes/codigo/{codigo}", "L001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lote1Id.toString())))
                .andExpect(jsonPath("$.codigo", is("L001")));
    }

    @Test
    void findByCodigo_whenLoteNotFound_shouldReturnNotFound() throws Exception {
        when(loteService.findByCodigo("INEXISTENTE")).thenThrow(new EntityNotFoundException("Lote não encontrado"));

        mockMvc.perform(get("/lotes/codigo/{codigo}", "INEXISTENTE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
