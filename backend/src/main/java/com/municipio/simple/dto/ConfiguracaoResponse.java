package com.municipio.simple.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoResponse {
    private List<StatusPedidoResponse> statusPedido;
    private List<CategoriaServicoResponse> categoriasServico;
    private List<PerfilResponse> perfis;
}