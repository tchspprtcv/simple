package com.municipio.simple.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "etapas_processo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtapaProcesso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_servico_id", nullable = false)
    private TipoServico tipoServico;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private Integer ordem;

    @Column(name = "tempo_estimado")
    private Integer tempoEstimado;

    @Column(name = "requer_documento", nullable = false)
    private boolean requerDocumento = false;

    @Column(name = "requer_pagamento", nullable = false)
    private boolean requerPagamento = false;

    @Column(name = "requer_aprovacao", nullable = false)
    private boolean requerAprovacao = false;

    @ManyToOne
    @JoinColumn(name = "perfil_responsavel_id")
    private Perfil perfilResponsavel;

    @ManyToOne
    @JoinColumn(name = "etapa_anterior_id")
    private EtapaProcesso etapaAnterior;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
