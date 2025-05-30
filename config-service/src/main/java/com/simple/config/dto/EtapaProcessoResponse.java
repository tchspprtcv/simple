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
public class EtapaProcessoResponse {
    private Integer id;
    private String codigo;
    private String nome;
    private String descricao;
    private Integer ordem;
    private Integer tempoEstimado; // em horas
    private boolean requerDocumento;
    private boolean requerPagamento;
    private boolean requerAprovacao;
    private Integer tipoServicoId; // Foreign key, just the ID
    private Integer perfilResponsavelId; // Foreign key, just the ID
    private String perfilResponsavelNome; // To be enriched from auth-service
    private Integer etapaAnteriorId; // Foreign key, just the ID
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
