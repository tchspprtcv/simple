package com.simple.citizen.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cidadaos") // Table name can remain the same or be prefixed like "citizen_cidadaos"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento; // e.g., "CPF", "RG"

    @Column(name = "numero_documento", nullable = false, unique = true) // Assuming numero_documento should be unique
    private String numeroDocumento;

    @Column(unique = true) // Assuming email should be unique if provided
    private String email;

    private String telefone;

    private String endereco; // Could be a simple string or a more complex @Embedded type if needed

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    // Consider adding a field like `private UUID userId;` if citizens are linked to users in auth-service.
    // For now, keeping it decoupled as per instructions.
}
