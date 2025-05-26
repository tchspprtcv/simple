package com.simple.favorites.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// This DTO represents the data we expect to receive from the config-service for a TipoServico
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoServicoDTO {
    private Integer id; // Matches TipoServico.id in config-service
    private String nome;
    private String descricao;
    private String categoriaNome; // Example of an enriched field from config-service's TipoServicoResponse
    private BigDecimal valorBase; // Example field
    // Add other fields as needed from config-service's TipoServicoResponse
}
