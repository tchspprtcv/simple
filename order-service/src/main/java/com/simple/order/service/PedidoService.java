package com.simple.order.service;

import com.simple.order.dto.PedidoRequest;
import com.simple.order.dto.PedidoResponse;
import com.simple.order.dto.StatusPedidoResponse;
import com.simple.order.domain.entity.Pedido;
import com.simple.order.domain.entity.StatusPedido;
import com.simple.order.repository.PedidoRepository;
import com.simple.order.repository.StatusPedidoRepository;
import com.simple.order.util.CodigoAcompanhamentoUtil;
import com.simple.order.client.AuthServiceClient;
import com.simple.order.client.CitizenServiceClient;
import com.simple.order.client.ConfigServiceClient;
import com.simple.order.client.dto.CidadaoResponse;
import com.simple.order.client.dto.EtapaProcessoResponse;
import com.simple.order.client.dto.TipoServicoResponse;
import com.simple.order.client.dto.UsuarioResponse;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository pedidoRepository;
    private final StatusPedidoRepository statusPedidoRepository;
    private final AuthServiceClient authServiceClient;
    private final CitizenServiceClient citizenServiceClient;
    private final ConfigServiceClient configServiceClient;

    @Transactional(readOnly = true)
    public Page<PedidoResponse> findAll(Pageable pageable) {
        return pedidoRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public PedidoResponse findById(UUID id) {
        return pedidoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public PedidoResponse findByCodigoAcompanhamento(String codigo) {
        return pedidoRepository.findByCodigoAcompanhamento(codigo)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com código: " + codigo));
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponse> findByCidadaoId(UUID cidadaoId, Pageable pageable) {
        // Optionally, call citizen-service to validate/get citizen details first
        return pedidoRepository.findByCidadaoId(cidadaoId, pageable).map(this::mapToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<PedidoResponse> findByUsuarioCriacaoId(UUID usuarioCriacaoId, Pageable pageable) {
        // This method is for "meus-pedidos" if the user ID is passed directly
        return pedidoRepository.findByUsuarioCriacaoId(usuarioCriacaoId, pageable).map(this::mapToResponse);
    }

    @Transactional
    public PedidoResponse create(PedidoRequest request, UUID usuarioCriacaoId) {
        // Call citizen-service to validate request.getCidadaoId()
        // Call config-service to validate request.getTipoServicoId() and get prazoEstimado

        StatusPedido statusInicial = statusPedidoRepository.findByCodigo("NOVO") // Assuming "NOVO" is a valid initial status code
                .orElseThrow(() -> new EntityNotFoundException("Status inicial 'NOVO' não configurado."));
        
        Pedido pedido = new Pedido();
        pedido.setCodigoAcompanhamento(CodigoAcompanhamentoUtil.gerarCodigo());
        pedido.setCidadaoId(request.getCidadaoId());
        pedido.setTipoServicoId(request.getTipoServicoId());
        pedido.setStatus(statusInicial);
        pedido.setUsuarioCriacaoId(usuarioCriacaoId); // Set from parameter (e.g., from JWT)
        // pedido.setUsuarioResponsavelId(usuarioCriacaoId); // Or based on some logic
        pedido.setDataInicio(LocalDateTime.now());
        pedido.setOrigem(request.getOrigem());
        pedido.setPrioridade(request.getPrioridade());
        pedido.setObservacoes(request.getObservacoes());
        
        // Set dataPrevisao based on prazoEstimado from TipoServico (fetched from config-service)
        // Ex: if (prazoEstimadoDias != null) {
        //         pedido.setDataPrevisao(LocalDateTime.now().plusDays(prazoEstimadoDias));
        //      }

        Pedido savedPedido = pedidoRepository.save(pedido);
        return mapToResponse(savedPedido);
    }

    @Transactional
    public PedidoResponse updateStatus(UUID id, Integer statusId) { // Changed statusId to Integer
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
        
        StatusPedido novoStatus = statusPedidoRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundException("Status não encontrado com ID: " + statusId));
        
        pedido.setStatus(novoStatus);
        
        if ("CONCLUIDO".equals(novoStatus.getCodigo())) { // Assuming "CONCLUIDO" is the code for a final status
            pedido.setDataConclusao(LocalDateTime.now());
        }
        
        Pedido updatedPedido = pedidoRepository.save(pedido);
        return mapToResponse(updatedPedido);
    }
    
    @Transactional
    public PedidoResponse assignResponsavel(UUID pedidoId, UUID usuarioResponsavelId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + pedidoId));
        // Call auth-service to validate usuarioResponsavelId
        pedido.setUsuarioResponsavelId(usuarioResponsavelId);
        Pedido updatedPedido = pedidoRepository.save(pedido);
        return mapToResponse(updatedPedido);
    }


    private PedidoResponse mapToResponse(Pedido pedido) {
        PedidoResponse response = PedidoResponse.builder()
                .id(pedido.getId())
                .codigoAcompanhamento(pedido.getCodigoAcompanhamento())
                .tipoServicoId(pedido.getTipoServicoId())
                .cidadaoId(pedido.getCidadaoId())
                .usuarioCriacaoId(pedido.getUsuarioCriacaoId())
                .usuarioResponsavelId(pedido.getUsuarioResponsavelId())
                .etapaAtualId(pedido.getEtapaAtualId())
                .status(mapStatusToResponse(pedido.getStatus()))
                .dataInicio(pedido.getDataInicio())
                .dataPrevisao(pedido.getDataPrevisao())
                .dataConclusao(pedido.getDataConclusao())
                .observacoes(pedido.getObservacoes())
                .valorTotal(pedido.getValorTotal())
                .origem(pedido.getOrigem())
                .prioridade(pedido.getPrioridade())
                .criadoEm(pedido.getCriadoEm())
                .atualizadoEm(pedido.getAtualizadoEm())
                .build();

        try {
            response.setCidadaoNome(citizenServiceClient.getCidadaoById(pedido.getCidadaoId()).getNome());
        } catch (FeignException e) {
            logger.error("Error fetching nome for Cidadao ID: {} from citizen-service. Error: {}", pedido.getCidadaoId(), e.getMessage());
        }

        try {
            response.setTipoServicoNome(configServiceClient.getTipoServicoById(pedido.getTipoServicoId()).getNome());
        } catch (FeignException e) {
            logger.error("Error fetching nome for TipoServico ID: {} from config-service. Error: {}", pedido.getTipoServicoId(), e.getMessage());
        }

        try {
            response.setUsuarioCriacaoNome(authServiceClient.getUsuarioById(pedido.getUsuarioCriacaoId()).getNome());
        } catch (FeignException e) {
            logger.error("Error fetching nome for UsuarioCriacao ID: {} from auth-service. Error: {}", pedido.getUsuarioCriacaoId(), e.getMessage());
        }

        if (pedido.getUsuarioResponsavelId() != null) {
            try {
                response.setUsuarioResponsavelNome(authServiceClient.getUsuarioById(pedido.getUsuarioResponsavelId()).getNome());
            } catch (FeignException e) {
                logger.error("Error fetching nome for UsuarioResponsavel ID: {} from auth-service. Error: {}", pedido.getUsuarioResponsavelId(), e.getMessage());
            }
        }
        if (pedido.getEtapaAtualId() != null) {
            try {
                response.setEtapaAtualNome(configServiceClient.getEtapaProcessoById(pedido.getEtapaAtualId()).getNome());
            } catch (FeignException e) {
                logger.error("Error fetching nome for EtapaProcesso ID: {} from config-service. Error: {}", pedido.getEtapaAtualId(), e.getMessage());
            }
        }

        return response;
    }

    private StatusPedidoResponse mapStatusToResponse(StatusPedido statusPedido) {
        if (statusPedido == null) return null;
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
}
