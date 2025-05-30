package com.simple.config.controller;

import com.simple.config.dto.StatusPedidoResponse;
import com.simple.config.service.StatusPedidoService;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StatusPedidoControllerTest {

    @Mock
    private StatusPedidoService statusPedidoService;

    @InjectMocks
    private StatusPedidoController statusPedidoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private StatusPedidoResponse statusResponse1;
    private StatusPedidoResponse statusResponse2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statusPedidoController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        statusResponse1 = StatusPedidoResponse.builder()
                .id(1)
                .codigo("NOVO")
                .nome("Novo Pedido")
                .cor("#FFFFFF")
                .criadoEm(LocalDateTime.now())
                .build();
        statusResponse2 = StatusPedidoResponse.builder()
                .id(2)
                .codigo("CONCLUIDO")
                .nome("Pedido Concluído")
                .cor("#000000")
                .criadoEm(LocalDateTime.now())
                .build();
    }

    @Test
    void findById_whenStatusExists_shouldReturnStatusResponse() throws Exception {
        when(statusPedidoService.findById(1)).thenReturn(statusResponse1);

        mockMvc.perform(get("/status-pedidos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Novo Pedido")))
                .andExpect(jsonPath("$.codigo", is("NOVO")));
    }

    @Test
    void findById_whenStatusNotFound_shouldReturnNotFound() throws Exception {
        when(statusPedidoService.findById(1)).thenThrow(new EntityNotFoundException("Status não encontrado"));

        mockMvc.perform(get("/status-pedidos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_whenServiceThrowsUnexpectedException_shouldReturnInternalServerError() throws Exception {
        when(statusPedidoService.findById(1)).thenThrow(new RuntimeException("Unexpected internal error"));

        mockMvc.perform(get("/status-pedidos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAll_shouldReturnListOfStatusResponse() throws Exception {
        List<StatusPedidoResponse> listResponse = Arrays.asList(statusResponse1, statusResponse2);
        when(statusPedidoService.findAll()).thenReturn(listResponse);

        mockMvc.perform(get("/status-pedidos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("Novo Pedido")))
                .andExpect(jsonPath("$[1].nome", is("Pedido Concluído")));
    }

    @Test
    void findByCodigo_whenStatusExists_shouldReturnStatusResponse() throws Exception {
        when(statusPedidoService.findByCodigo("NOVO")).thenReturn(statusResponse1);

        mockMvc.perform(get("/status-pedidos/codigo/{codigo}", "NOVO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Novo Pedido")))
                .andExpect(jsonPath("$.codigo", is("NOVO")));
    }

    @Test
    void findByCodigo_whenStatusNotFound_shouldReturnNotFound() throws Exception {
        when(statusPedidoService.findByCodigo("INEXISTENTE")).thenThrow(new EntityNotFoundException("Status não encontrado"));

        mockMvc.perform(get("/status-pedidos/codigo/{codigo}", "INEXISTENTE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
