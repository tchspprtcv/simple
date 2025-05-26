package com.simple.order.dto;

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

    // IDs for entities from other services
    private UUID tipoServicoId;
    private UUID cidadaoId;
    private UUID usuarioCriacaoId;
    private UUID usuarioResponsavelId;
    private UUID etapaAtualId; // Assuming EtapaProcesso is external for now

    // Enriched data (will be populated by service layer, potentially via inter-service calls)
    private String tipoServicoNome; // TODO: Populate from Config Service
    private String cidadaoNome; // TODO: Populate from Citizen Service
    private String usuarioCriacaoNome; // TODO: Populate from Auth Service
    private String usuarioResponsavelNome; // TODO: Populate from Auth Service
    private String etapaAtualNome; // TODO: Populate from Config Service (or similar)

    private StatusPedidoResponse status; // Status is local to order-service

    private LocalDateTime dataInicio;
    private LocalDateTime dataPrevisao;
    private LocalDateTime dataConclusao;
    private String observacoes;
    private BigDecimal valorTotal;
    private String origem;
    private Integer prioridade;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm; // Added this field
}
