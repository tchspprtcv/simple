package com.simple.auth.controller;

import com.simple.auth.dto.UsuarioRequest;
import com.simple.auth.dto.UsuarioResponse;
import com.simple.auth.service.UsuarioService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                // Optional: Add exception handlers if you have custom ones
                // .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // For LocalDateTime
    }

    @Test
    void getCurrentUser_shouldReturnUsuarioResponse() throws Exception {
        UUID userId = UUID.randomUUID();
        UsuarioResponse mockResponse = UsuarioResponse.builder()
                .id(userId)
                .nome("Test User")
                .email("test@example.com")
                .perfil("USER")
                .ativo(true)
                .ultimoAcesso(LocalDateTime.now())
                .criadoEm(LocalDateTime.now().minusDays(1))
                .build();

        when(usuarioService.getCurrentAuthenticatedUser()).thenReturn(mockResponse);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.nome", is("Test User")));
    }

    @Test
    void updateCurrentUserProfile_shouldReturnUpdatedUsuarioResponse() throws Exception {
        UUID userId = UUID.randomUUID();
        UsuarioRequest updateRequest = new UsuarioRequest("Updated Test User");
        UsuarioResponse mockResponse = UsuarioResponse.builder()
                .id(userId)
                .nome("Updated Test User")
                .email("test@example.com")
                .perfil("USER")
                .ativo(true)
                .build();

        when(usuarioService.updateCurrentAuthenticatedUser(any(UsuarioRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(put("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Updated Test User")));
    }

    @Test
    void getUserById_whenUserExists_shouldReturnUsuarioResponse() throws Exception {
        UUID userId = UUID.randomUUID();
        UsuarioResponse mockResponse = UsuarioResponse.builder()
                .id(userId)
                .nome("Specific User")
                .email("specific@example.com")
                .perfil("ADMIN")
                .ativo(true)
                .build();

        when(usuarioService.findUserById(userId)).thenReturn(mockResponse);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.nome", is("Specific User")));
    }

    @Test
    void getUserById_whenUserNotFound_shouldReturnNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        when(usuarioService.findUserById(userId)).thenThrow(new EntityNotFoundException("Utilizador não encontrado"));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById_whenServiceThrowsUnexpectedException_shouldReturnInternalServerError() throws Exception {
        UUID userId = UUID.randomUUID();
        when(usuarioService.findUserById(userId)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/users/{id}", userId))
            .andExpect(status().isInternalServerError());
    }
}
