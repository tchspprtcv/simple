package com.simple.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoResponse {
    // This DTO will now only contain configurations managed by this service.
    // If truly global configurations are needed, Feign clients would be used
    // to fetch data from other services (e.g., StatusPedido from order-service,
    // Perfil from auth-service).
    
    private List<CategoriaServicoResponse> categoriasServico;
    private List<TipoServicoResponse> tiposServico;
    
    // Placeholder for other general configurations that might be managed by this service in the future
    // private Map<String, String> generalSettings; 
}
