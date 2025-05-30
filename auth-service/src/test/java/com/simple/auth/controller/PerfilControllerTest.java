package com.simple.auth.controller;

import com.simple.auth.dto.PerfilResponse;
import com.simple.auth.service.PerfilService;
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

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class PerfilControllerTest {

    @Mock
    private PerfilService perfilService;

    @InjectMocks
    private PerfilController perfilController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(perfilController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // For LocalDateTime
    }

    @Test
    void getPerfilById_whenPerfilExists_shouldReturnPerfilResponse() throws Exception {
        Integer perfilId = 1;
        PerfilResponse mockResponse = PerfilResponse.builder()
                .id(perfilId)
                .nome("ADMIN")
                .descricao("Administrator role")
                .permissoes("{\"users\":\"all\"}")
                .criadoEm(LocalDateTime.now().minusDays(1))
                .atualizadoEm(LocalDateTime.now())
                .build();

        when(perfilService.findPerfilById(perfilId)).thenReturn(mockResponse);

        mockMvc.perform(get("/perfis/{id}", perfilId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(perfilId)))
                .andExpect(jsonPath("$.nome", is("ADMIN")))
                .andExpect(jsonPath("$.descricao", is("Administrator role")));
    }

    @Test
    void getPerfilById_whenPerfilNotFound_shouldReturnNotFound() throws Exception {
        Integer perfilId = 2;

        when(perfilService.findPerfilById(perfilId)).thenThrow(new EntityNotFoundException("Perfil não encontrado"));

        mockMvc.perform(get("/perfis/{id}", perfilId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPerfilById_whenServiceThrowsUnexpectedException_shouldReturnInternalServerError() throws Exception {
        Integer perfilId = 3;
        when(perfilService.findPerfilById(perfilId)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/perfis/{id}", perfilId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
