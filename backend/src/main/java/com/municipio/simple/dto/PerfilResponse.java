package com.municipio.simple.dto;

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
    private String permissoes; // Mantendo como String para refletir o JSON
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}