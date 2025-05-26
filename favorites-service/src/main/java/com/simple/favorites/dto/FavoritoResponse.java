package com.simple.favorites.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoResponse {
    private UUID id; // ID of the Favorito entity
    private UUID usuarioId;
    private Integer tipoServicoId; // ID of the favorited TipoServico
    private LocalDateTime criadoEm;
    private TipoServicoDTO tipoServicoDetails; // Enriched details of the service type
}
