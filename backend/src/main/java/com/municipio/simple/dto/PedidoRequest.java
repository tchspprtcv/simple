package com.municipio.simple.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequest {
    @NotNull(message = "Tipo de serviço é obrigatório")
    private Long tipoServicoId;
    
    @NotNull(message = "Cidadão é obrigatório")
    private UUID cidadaoId;
    
    @NotBlank(message = "Origem é obrigatória")
    private String origem;
    
    private Integer prioridade = 0;
    private String observacoes;
}
