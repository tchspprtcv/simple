package com.simple.config.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tipos_servicos") // Table name can remain or be prefixed "config_tipos_servicos"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER) // Categoria is local to this service
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaServico categoria;

    @Column(nullable = false, unique = true)
    private String codigo; // A unique code for the service type

    @Column(nullable = false)
    private String nome;

    @Lob // For potentially longer text
    private String descricao;

    @Column(name = "prazo_estimado_dias") // Renamed for clarity (e.g., in days)
    private Integer prazoEstimadoDias;

    @Column(name = "valor_base")
    private BigDecimal valorBase;

    @Column(name = "requer_vistoria", nullable = false)
    private boolean requerVistoria = false;

    @Column(name = "requer_analise_tecnica", nullable = false)
    private boolean requerAnaliseTecnica = false;

    @Column(name = "requer_aprovacao", nullable = false)
    private boolean requerAprovacao = true;

    @Column(name = "disponivel_portal", nullable = false)
    private boolean disponivelPortal = false;

    @Column(nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
