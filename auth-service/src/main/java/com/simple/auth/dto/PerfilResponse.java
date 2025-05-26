package com.simple.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponse {
    private Integer id;
    private String nome;
    private String descricao;
    private String permissoes; // JSON string representing permissions
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
