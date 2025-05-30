package com.simple.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusPedidoResponse {
    private Integer id;
    private String codigo;
    private String nome;
    private String descricao;
    private String cor;
    private String icone;
    private Integer ordem;
    private boolean visivelPortal;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
