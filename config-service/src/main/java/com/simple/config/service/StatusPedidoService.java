package com.simple.config.service;

import com.simple.config.domain.entity.StatusPedido;
import com.simple.config.dto.StatusPedidoResponse;
import com.simple.config.repository.StatusPedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusPedidoService {

    private static final Logger logger = LoggerFactory.getLogger(StatusPedidoService.class);
    private final StatusPedidoRepository statusPedidoRepository;

    @Transactional(readOnly = true)
    public StatusPedidoResponse findById(Integer id) {
        logger.info("Attempting to find StatusPedido with ID: {}", id);
        StatusPedido statusPedido = statusPedidoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("StatusPedido not found with ID: {}", id);
                    return new EntityNotFoundException("StatusPedido não encontrado com ID: " + id);
                });
        logger.info("StatusPedido found with ID: {}", id);
        return mapToResponse(statusPedido);
    }

    @Transactional(readOnly = true)
    public List<StatusPedidoResponse> findAll() {
        logger.info("Attempting to find all StatusPedido, ordered by 'ordem'");
        List<StatusPedido> statusPedidos = statusPedidoRepository.findAllByOrderByOrdemAsc();
        return statusPedidos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public StatusPedidoResponse findByCodigo(String codigo) {
        logger.info("Attempting to find StatusPedido with codigo: {}", codigo);
        StatusPedido statusPedido = statusPedidoRepository.findByCodigo(codigo)
                .orElseThrow(() -> {
                    logger.warn("StatusPedido not found with codigo: {}", codigo);
                    return new EntityNotFoundException("StatusPedido não encontrado com código: " + codigo);
                });
        logger.info("StatusPedido found with codigo: {}", codigo);
        return mapToResponse(statusPedido);
    }


    private StatusPedidoResponse mapToResponse(StatusPedido statusPedido) {
        return StatusPedidoResponse.builder()
                .id(statusPedido.getId())
                .codigo(statusPedido.getCodigo())
                .nome(statusPedido.getNome())
                .descricao(statusPedido.getDescricao())
                .cor(statusPedido.getCor())
                .icone(statusPedido.getIcone())
                .ordem(statusPedido.getOrdem())
                .visivelPortal(statusPedido.isVisivelPortal())
                .criadoEm(statusPedido.getCriadoEm())
                .atualizadoEm(statusPedido.getAtualizadoEm())
                .build();
    }

    // Basic CRUD methods (create, update, delete) can be added here.
    // For create/update, a StatusPedidoRequest DTO would be needed.
}
