package com.simple.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoServicoResponse {
    private Integer id;
    private Integer categoriaId;
    private String categoriaNome; // To be populated by the service layer
    private String codigo;
    private String nome;
    private String descricao;
    private Integer prazoEstimadoDias; // Renamed from prazoEstimado
    private BigDecimal valorBase;
    private boolean requerVistoria;
    private boolean requerAnaliseTecnica;
    private boolean requerAprovacao;
    private boolean disponivelPortal;
    private boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
