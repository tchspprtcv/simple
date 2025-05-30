package com.simple.config.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lotes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Or GenerationType.UUID
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(length = 50)
    private String quadra;

    @Column(length = 50)
    private String numero;

    @Column(precision = 10, scale = 2) // Example precision for area
    private BigDecimal area; // em m²

    @Column(length = 255)
    private String endereco;

    @Column(length = 100)
    private String bairro;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2) // UF
    private String estado;

    @Column(length = 9) // CEP format XXXXX-XXX
    private String cep;

    @Column(precision = 10, scale = 7) // Example precision for latitude
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7) // Example precision for longitude
    private BigDecimal longitude;

    @Column(length = 50) // e.g., DISPONIVEL, VENDIDO, RESERVADO
    private String situacao;

    @Column(name = "valor_base", precision = 12, scale = 2) // Example precision for currency
    private BigDecimal valorBase;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
