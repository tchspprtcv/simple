package com.simple.order.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedidos") // Table name can remain or be prefixed like "order_pedidos"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "codigo_acompanhamento", nullable = false, unique = true)
    private String codigoAcompanhamento;

    @Column(name = "tipo_servico_id", nullable = false)
    private UUID tipoServicoId; // Changed from TipoServico entity

    @Column(name = "cidadao_id", nullable = false)
    private UUID cidadaoId; // Changed from Cidadao entity

    @Column(name = "usuario_criacao_id", nullable = false)
    private UUID usuarioCriacaoId; // Changed from Usuario entity

    @Column(name = "usuario_responsavel_id")
    private UUID usuarioResponsavelId; // Changed from Usuario entity

    @Column(name = "etapa_atual_id") // Assuming EtapaProcesso is part of Config service or simplified
    private UUID etapaAtualId; // Changed from EtapaProcesso entity

    @ManyToOne(fetch = FetchType.EAGER) // StatusPedido is local to this service
    @JoinColumn(name = "status_id", nullable = false)
    private StatusPedido status;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_previsao")
    private LocalDateTime dataPrevisao;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Lob // For potentially longer text
    private String observacoes;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private String origem; // e.g., "PORTAL", "APP", "INTERNO"

    @Column(nullable = false)
    private Integer prioridade = 0; // 0 = Normal, higher values = higher priority

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
