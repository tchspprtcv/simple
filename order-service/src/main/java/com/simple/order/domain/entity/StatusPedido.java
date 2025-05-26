package com.simple.order.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "status_pedido") // Table name can remain or be prefixed like "order_status_pedido"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String codigo; // e.g., "PENDENTE", "EM_ANALISE", "CONCLUIDO"

    @Column(nullable = false)
    private String nome;

    private String descricao;

    private String cor; // For UI hints

    private String icone; // For UI hints

    @Column(nullable = false)
    private Integer ordem = 0; // To define a sequence or progression

    @Column(name = "visivel_portal", nullable = false)
    private boolean visivelPortal = true; // If status should be visible to the end-user

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
