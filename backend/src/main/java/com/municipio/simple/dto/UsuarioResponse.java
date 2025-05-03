package com.municipio.simple.dto;

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
public class UsuarioResponse {
    private UUID id;
    private String nome;
    private String email;
    private String perfil;
    private boolean ativo;
    private LocalDateTime ultimoAcesso;
    private LocalDateTime criadoEm;
}
