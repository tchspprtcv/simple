package com.municipio.simple.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoRequest {

    @NotNull(message = "O ID do tipo de serviço não pode ser nulo.")
    private Integer tipoServicoId;

    private Integer ordem;
}