package com.municipio.simple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponse {
    private UUID id;
    private String codigoAcompanhamento;
    private String tipoServico;
    private String cidadao;
    private String usuarioCriacao;
    private String usuarioResponsavel;
    private String etapaAtual;
    private String status;
    private LocalDateTime dataInicio;
    private LocalDateTime dataPrevisao;
    private LocalDateTime dataConclusao;
    private String observacoes;
    private BigDecimal valorTotal;
    private String origem;
    private Integer prioridade;
    private LocalDateTime criadoEm;
}
