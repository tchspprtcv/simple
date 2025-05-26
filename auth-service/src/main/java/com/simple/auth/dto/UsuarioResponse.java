package com.simple.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse { // This is the UsuarioDTO
    private UUID id;
    private String nome;
    private String email;
    private String perfil; // Name of the Perfil (e.g., "ADMIN", "USER")
    private boolean ativo;
    private LocalDateTime ultimoAcesso;
    private LocalDateTime criadoEm;
    // Note: No password field, which is correct for a response DTO.
}
