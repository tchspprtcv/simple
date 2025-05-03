package com.municipio.simple.entity;

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
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "codigo_acompanhamento", nullable = false, unique = true)
    private String codigoAcompanhamento;

    @ManyToOne
    @JoinColumn(name = "tipo_servico_id", nullable = false)
    private TipoServico tipoServico;

    @ManyToOne
    @JoinColumn(name = "cidadao_id", nullable = false)
    private Cidadao cidadao;

    @ManyToOne
    @JoinColumn(name = "usuario_criacao_id", nullable = false)
    private Usuario usuarioCriacao;

    @ManyToOne
    @JoinColumn(name = "usuario_responsavel_id")
    private Usuario usuarioResponsavel;

    @ManyToOne
    @JoinColumn(name = "etapa_atual_id")
    private EtapaProcesso etapaAtual;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusPedido status;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_previsao")
    private LocalDateTime dataPrevisao;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    private String observacoes;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private String origem;

    @Column(nullable = false)
    private Integer prioridade = 0;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
