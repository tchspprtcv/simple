package com.simple.config.service;

import com.simple.config.dto.CategoriaServicoResponse;
import com.simple.config.dto.ConfiguracaoResponse;
import com.simple.config.dto.TipoServicoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.cache.annotation.Cacheable; // Caching can be added later

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfiguracaoService {

    private final CategoriaServicoService categoriaServicoService;
    private final TipoServicoService tipoServicoService;
    // TODO: Inject Feign clients for StatusPedidoService and PerfilService if needed for a global config view

    // @Cacheable("configuracoesGlobais") // Example of caching
    @Transactional(readOnly = true)
    public ConfiguracaoResponse getTodasConfiguracoesLocais() {
        List<CategoriaServicoResponse> categoriasServico = categoriaServicoService.findAll(true); // Only active ones
        List<TipoServicoResponse> tiposServico = tipoServicoService.findAll(true, null); // Only active, all categories

        // In the future, if this endpoint needs to provide a truly global configuration:
        // List<StatusPedidoResponse> statusPedidos = statusPedidoFeignClient.getAllStatus();
        // List<PerfilResponse> perfis = perfilFeignClient.getAllPerfis();
        // return new ConfiguracaoResponse(statusPedidos, categoriasServico, tiposServico, perfis);

        return ConfiguracaoResponse.builder()
                .categoriasServico(categoriasServico)
                .tiposServico(tiposServico)
                .build();
    }
}
