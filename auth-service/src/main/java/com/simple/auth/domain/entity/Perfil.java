package com.simple.auth.domain.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type; // Ensure this is the correct Type annotation
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "perfis") // Table name can remain or be prefixed like "auth_perfis"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome; // e.g., "ADMIN", "USER", "CIDADAO"

    private String descricao;

    // Using String for permissions; could be a List<String> or a custom object if parsed
    // For simplicity, keeping as String as in the original monolith's definition
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb") // Removed nullable = false as it's not in the original and might be an issue if it's truly optional
    private String permissoes; // JSON string representing permissions

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
