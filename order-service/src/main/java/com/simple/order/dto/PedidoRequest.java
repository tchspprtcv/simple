package com.simple.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private UUID tipoServicoId; // Changed from Integer to UUID
    
    @NotNull(message = "Cidadão é obrigatório")
    private UUID cidadaoId;
    
    // usuarioCriacaoId will be set based on the authenticated user from JWT/SecurityContext
    // usuarioResponsavelId can be set by specific logic, perhaps admin/internal users
    // statusId will be set internally, usually to an initial status

    @NotBlank(message = "Origem é obrigatória")
    @Size(max = 50, message = "Origem não pode exceder 50 caracteres")
    private String origem; // e.g., "PORTAL", "APP", "INTERNO"
    
    private Integer prioridade = 0; // Default to 0 (Normal)
    
    @Size(max = 2000, message = "Observações não podem exceder 2000 caracteres")
    private String observacoes;
}
