package com.simple.config.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "etapas_processo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtapaProcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Integer ordem;

    @Column(name = "tempo_estimado_horas")
    private Integer tempoEstimado; // em horas

    @Column(name = "requer_documento", nullable = false)
    private boolean requerDocumento = false;

    @Column(name = "requer_pagamento", nullable = false)
    private boolean requerPagamento = false;

    @Column(name = "requer_aprovacao", nullable = false)
    private boolean requerAprovacao = false;

    @Column(name = "tipo_servico_id", nullable = false)
    private Integer tipoServicoId; // Foreign key to TipoServico

    @Column(name = "perfil_responsavel_id")
    private Integer perfilResponsavelId; // Foreign key to Perfil in auth-service

    @Column(name = "etapa_anterior_id")
    private Integer etapaAnteriorId; // Self-referential foreign key

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    // Relationships (optional for this task, but good for completeness)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "tipo_servico_id", insertable = false, updatable = false)
    // private TipoServico tipoServico;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "etapa_anterior_id", insertable = false, updatable = false)
    // private EtapaProcesso etapaAnterior;
}
