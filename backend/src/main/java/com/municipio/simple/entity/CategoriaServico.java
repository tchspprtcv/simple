package com.municipio.simple.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "categorias_servicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Changed from Long to Integer

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    private String icone;

    private String cor;

    @Column(nullable = false)
    private Integer ordem = 0;

    @Column(nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
