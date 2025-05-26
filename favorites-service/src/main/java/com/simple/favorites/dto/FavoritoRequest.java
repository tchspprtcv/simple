package com.simple.favorites.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoRequest {
    @NotNull(message = "ID do Tipo de Serviço é obrigatório")
    private Integer tipoServicoId; // Matches TipoServico.id in config-service
}
