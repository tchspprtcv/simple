package com.municipio.simple.service;

import com.municipio.simple.dto.PedidoRequest;
import com.municipio.simple.dto.PedidoResponse;
import com.municipio.simple.entity.*;
import com.municipio.simple.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CidadaoRepository cidadaoRepository;
    private final TipoServicoRepository tipoServicoRepository;
    private final StatusPedidoRepository statusPedidoRepository;
    private final UsuarioRepository usuarioRepository;

    public Page<PedidoResponse> findAll(Pageable pageable) {
        return pedidoRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public PedidoResponse findById(UUID id) {
        return pedidoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }

    public PedidoResponse findByCodigoAcompanhamento(String codigo) {
        return pedidoRepository.findByCodigoAcompanhamento(codigo)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }

    public Page<PedidoResponse> findByCidadao(UUID cidadaoId, Pageable pageable) {
        Cidadao cidadao = cidadaoRepository.findById(cidadaoId)
                .orElseThrow(() -> new EntityNotFoundException("Utente não encontrado"));
        
        return pedidoRepository.findByCidadao(cidadao, pageable)
                .map(this::mapToResponse);
    }

    public Page<PedidoResponse> findByUsuarioLogado(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        return pedidoRepository.findByUsuarioCriacao(usuarioLogado, pageable)
                .map(this::mapToResponse);
    }

    public PedidoResponse create(PedidoRequest request) {
        Cidadao cidadao = cidadaoRepository.findById(request.getCidadaoId())
                .orElseThrow(() -> new EntityNotFoundException("Utente não encontrado"));
        
        TipoServico tipoServico = tipoServicoRepository.findById(request.getTipoServicoId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de serviço não encontrado"));
        
        StatusPedido statusInicial = statusPedidoRepository.findByCodigo("NOVO")
                .orElseThrow(() -> new EntityNotFoundException("Status inicial não encontrado"));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        
        Pedido pedido = new Pedido();
        pedido.setCidadao(cidadao);
        pedido.setTipoServico(tipoServico);
        pedido.setStatus(statusInicial);
        pedido.setUsuarioCriacao(usuarioLogado);
        pedido.setUsuarioResponsavel(usuarioLogado);
        pedido.setDataInicio(LocalDateTime.now());
        pedido.setOrigem(request.getOrigem());
        pedido.setPrioridade(request.getPrioridade());
        pedido.setObservacoes(request.getObservacoes());
        
        // Calcular data de previsão baseada no prazo estimado do tipo de serviço
        if (tipoServico.getPrazoEstimado() != null) {
            pedido.setDataPrevisao(LocalDateTime.now().plusDays(tipoServico.getPrazoEstimado()));
        }
        
        pedidoRepository.save(pedido);
        
        return mapToResponse(pedido);
    }

    public PedidoResponse updateStatus(UUID id, Long statusId) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        
        StatusPedido novoStatus = statusPedidoRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundException("Status não encontrado"));
        
        pedido.setStatus(novoStatus);
        
        // Se o status for "CONCLUIDO", atualizar a data de conclusão
        if (novoStatus.getCodigo().equals("CONCLUIDO")) {
            pedido.setDataConclusao(LocalDateTime.now());
        }
        
        pedidoRepository.save(pedido);
        
        return mapToResponse(pedido);
    }

    private PedidoResponse mapToResponse(Pedido pedido) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .codigoAcompanhamento(pedido.getCodigoAcompanhamento())
                .tipoServico(pedido.getTipoServico().getNome())
                .cidadao(pedido.getCidadao().getNome())
                .usuarioCriacao(pedido.getUsuarioCriacao().getNome())
                .usuarioResponsavel(pedido.getUsuarioResponsavel() != null ? pedido.getUsuarioResponsavel().getNome() : null)
                .etapaAtual(pedido.getEtapaAtual() != null ? pedido.getEtapaAtual().getNome() : null)
                .status(pedido.getStatus().getNome())
                .dataInicio(pedido.getDataInicio())
                .dataPrevisao(pedido.getDataPrevisao())
                .dataConclusao(pedido.getDataConclusao())
                .observacoes(pedido.getObservacoes())
                .valorTotal(pedido.getValorTotal())
                .origem(pedido.getOrigem())
                .prioridade(pedido.getPrioridade())
                .criadoEm(pedido.getCriadoEm())
                .build();
    }
}
