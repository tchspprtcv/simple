package com.simple.order.service;

import com.simple.order.dto.PedidoRequest;
import com.simple.order.dto.PedidoResponse;
import com.simple.order.dto.StatusPedidoResponse;
import com.simple.order.domain.entity.Pedido;
import com.simple.order.domain.entity.StatusPedido;
import com.simple.order.repository.PedidoRepository;
import com.simple.order.repository.StatusPedidoRepository;
import com.simple.order.util.CodigoAcompanhamentoUtil;
// TODO: Import Feign clients for auth, citizen, config services when created
// import com.simple.order.client.AuthServiceClient;
// import com.simple.order.client.CitizenServiceClient;
// import com.simple.order.client.ConfigServiceClient;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final StatusPedidoRepository statusPedidoRepository;
    // TODO: Inject Feign clients
    // private final AuthServiceClient authServiceClient;
    // private final CitizenServiceClient citizenServiceClient;
    // private final ConfigServiceClient configServiceClient;

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
        // TODO: Optionally, call citizen-service to validate/get citizen details first
        return pedidoRepository.findByCidadaoId(cidadaoId, pageable).map(this::mapToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<PedidoResponse> findByUsuarioCriacaoId(UUID usuarioCriacaoId, Pageable pageable) {
        // This method is for "meus-pedidos" if the user ID is passed directly
        return pedidoRepository.findByUsuarioCriacaoId(usuarioCriacaoId, pageable).map(this::mapToResponse);
    }

    @Transactional
    public PedidoResponse create(PedidoRequest request, UUID usuarioCriacaoId) {
        // TODO: Call citizen-service to validate request.getCidadaoId()
        // TODO: Call config-service to validate request.getTipoServicoId() and get prazoEstimado

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
        
        // TODO: Set dataPrevisao based on prazoEstimado from TipoServico (fetched from config-service)
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
        // TODO: Call auth-service to validate usuarioResponsavelId
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

        // TODO: Enrich with details from other services
        // response.setCidadaoNome(citizenServiceClient.getCidadaoById(pedido.getCidadaoId()).getNome());
        // response.setTipoServicoNome(configServiceClient.getTipoServicoById(pedido.getTipoServicoId()).getNome());
        // response.setUsuarioCriacaoNome(authServiceClient.getUsuarioById(pedido.getUsuarioCriacaoId()).getNome());
        // if (pedido.getUsuarioResponsavelId() != null) {
        //     response.setUsuarioResponsavelNome(authServiceClient.getUsuarioById(pedido.getUsuarioResponsavelId()).getNome());
        // }
        // if (pedido.getEtapaAtualId() != null) {
        //     response.setEtapaAtualNome(configServiceClient.getEtapaProcessoById(pedido.getEtapaAtualId()).getNome());
        // }

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
