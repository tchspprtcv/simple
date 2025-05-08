package com.municipio.simple.service;

import com.municipio.simple.dto.CategoriaServicoResponse;
import com.municipio.simple.dto.ConfiguracaoResponse;
import com.municipio.simple.dto.PerfilResponse;
import com.municipio.simple.dto.StatusPedidoResponse;
import com.municipio.simple.entity.CategoriaServico;
import com.municipio.simple.entity.Perfil;
import com.municipio.simple.entity.StatusPedido;
import com.municipio.simple.repository.CategoriaServicoRepository;
import com.municipio.simple.repository.PerfilRepository;
import com.municipio.simple.repository.StatusPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfiguracaoService {

    private final StatusPedidoRepository statusPedidoRepository;
    private final CategoriaServicoRepository categoriaServicoRepository;
    private final PerfilRepository perfilRepository;

    @Cacheable("configuracoes")
    public ConfiguracaoResponse getTodasConfiguracoes() {
        List<StatusPedidoResponse> statusPedidos = statusPedidoRepository.findAll().stream()
                .map(this::convertToStatusPedidoResponse)
                .collect(Collectors.toList());

        List<CategoriaServicoResponse> categoriasServico = categoriaServicoRepository.findAll().stream()
                .map(this::convertToCategoriaServicoResponse)
                .collect(Collectors.toList());

        List<PerfilResponse> perfis = perfilRepository.findAll().stream()
                .map(this::convertToPerfilResponse)
                .collect(Collectors.toList());

        return new ConfiguracaoResponse(statusPedidos, categoriasServico, perfis);
    }

    private StatusPedidoResponse convertToStatusPedidoResponse(StatusPedido statusPedido) {
        return new StatusPedidoResponse(
                statusPedido.getId(),
                statusPedido.getCodigo(),
                statusPedido.getNome(),
                statusPedido.getDescricao(),
                statusPedido.getCor(),
                statusPedido.getIcone(),
                statusPedido.getOrdem(),
                statusPedido.isVisivelPortal(),
                statusPedido.getCriadoEm(),
                statusPedido.getAtualizadoEm()
        );
    }

    private CategoriaServicoResponse convertToCategoriaServicoResponse(CategoriaServico categoriaServico) {
        return new CategoriaServicoResponse(
                categoriaServico.getId(),
                categoriaServico.getNome(),
                categoriaServico.getDescricao(),
                categoriaServico.getIcone(),
                categoriaServico.getCor(),
                categoriaServico.getOrdem(),
                categoriaServico.isAtivo(),
                categoriaServico.getCriadoEm(),
                categoriaServico.getAtualizadoEm()
        );
    }

    private PerfilResponse convertToPerfilResponse(Perfil perfil) {
        return new PerfilResponse(
                perfil.getId(),
                perfil.getNome(),
                perfil.getDescricao(),
                perfil.getPermissoes(),
                perfil.getCriadoEm(),
                perfil.getAtualizadoEm()
        );
    }
}