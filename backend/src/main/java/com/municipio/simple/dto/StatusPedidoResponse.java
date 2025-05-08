package com.municipio.simple.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
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