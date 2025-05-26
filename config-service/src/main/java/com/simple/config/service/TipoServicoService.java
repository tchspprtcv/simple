package com.simple.config.service;

import com.simple.config.domain.entity.CategoriaServico;
import com.simple.config.domain.entity.TipoServico;
import com.simple.config.dto.TipoServicoRequest;
import com.simple.config.dto.TipoServicoResponse;
import com.simple.config.repository.CategoriaServicoRepository;
import com.simple.config.repository.TipoServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoServicoService {

    private final TipoServicoRepository tipoServicoRepository;
    private final CategoriaServicoRepository categoriaServicoRepository; // To fetch CategoriaServico

    @Transactional(readOnly = true)
    public List<TipoServicoResponse> findAll(boolean apenasAtivos, Integer categoriaId) {
        List<TipoServico> tiposServico;
        Sort sort = Sort.by(Sort.Order.asc("categoria.ordem"), Sort.Order.asc("nome"));

        if (categoriaId != null) {
            if (apenasAtivos) {
                tiposServico = tipoServicoRepository.findByCategoria_IdAndAtivoTrueOrderByNome(categoriaId);
            } else {
                // Manual filter after fetching if no direct repository method for categoriaId and !ativo
                // Or create a more specific repository method. For now, simple approach:
                CategoriaServico categoria = categoriaServicoRepository.findById(categoriaId)
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + categoriaId));
                tiposServico = tipoServicoRepository.findAll(sort).stream()
                                .filter(ts -> ts.getCategoria().getId().equals(categoriaId))
                                .collect(Collectors.toList());
            }
        } else {
            if (apenasAtivos) {
                tiposServico = tipoServicoRepository.findByAtivoTrueOrderByNome();
            } else {
                tiposServico = tipoServicoRepository.findAll(sort);
            }
        }
        return tiposServico.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoServicoResponse findById(Integer id) {
        return tipoServicoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Serviço não encontrado com ID: " + id));
    }
    
    @Transactional(readOnly = true)
    public TipoServicoResponse findByCodigo(String codigo) {
        return tipoServicoRepository.findByCodigo(codigo)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Serviço não encontrado com código: " + codigo));
    }

    @Transactional
    public TipoServicoResponse create(TipoServicoRequest request) {
        tipoServicoRepository.findByCodigo(request.getCodigo()).ifPresent(ts -> {
            throw new IllegalArgumentException("Tipo de Serviço com o código '" + request.getCodigo() + "' já existe.");
        });
        tipoServicoRepository.findByNome(request.getNome()).ifPresent(ts -> {
            throw new IllegalArgumentException("Tipo de Serviço com o nome '" + request.getNome() + "' já existe.");
        });

        CategoriaServico categoria = categoriaServicoRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + request.getCategoriaId()));

        TipoServico tipoServico = new TipoServico();
        mapRequestToEntity(request, tipoServico, categoria);

        TipoServico savedTipoServico = tipoServicoRepository.save(tipoServico);
        return mapToResponse(savedTipoServico);
    }

    @Transactional
    public TipoServicoResponse update(Integer id, TipoServicoRequest request) {
        TipoServico tipoServico = tipoServicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Serviço não encontrado com ID: " + id));

        tipoServicoRepository.findByCodigo(request.getCodigo()).ifPresent(ts -> {
            if (!ts.getId().equals(id)) {
                throw new IllegalArgumentException("Outro Tipo de Serviço com o código '" + request.getCodigo() + "' já existe.");
            }
        });
        tipoServicoRepository.findByNome(request.getNome()).ifPresent(ts -> {
            if (!ts.getId().equals(id)) {
                throw new IllegalArgumentException("Outro Tipo de Serviço com o nome '" + request.getNome() + "' já existe.");
            }
        });

        CategoriaServico categoria = categoriaServicoRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com ID: " + request.getCategoriaId()));
        
        mapRequestToEntity(request, tipoServico, categoria);

        TipoServico updatedTipoServico = tipoServicoRepository.save(tipoServico);
        return mapToResponse(updatedTipoServico);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!tipoServicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de Serviço não encontrado com ID: " + id);
        }
        // Consider implications: what happens to Pedidos referencing this TipoServico?
        // For now, direct delete.
        tipoServicoRepository.deleteById(id);
    }

    private void mapRequestToEntity(TipoServicoRequest request, TipoServico entity, CategoriaServico categoria) {
        entity.setCategoria(categoria);
        entity.setCodigo(request.getCodigo());
        entity.setNome(request.getNome());
        entity.setDescricao(request.getDescricao());
        entity.setPrazoEstimadoDias(request.getPrazoEstimadoDias());
        entity.setValorBase(request.getValorBase());
        entity.setRequerVistoria(request.getRequerVistoria());
        entity.setRequerAnaliseTecnica(request.getRequerAnaliseTecnica());
        entity.setRequerAprovacao(request.getRequerAprovacao());
        entity.setDisponivelPortal(request.getDisponivelPortal());
        entity.setAtivo(request.getAtivo());
    }

    private TipoServicoResponse mapToResponse(TipoServico tipoServico) {
        return TipoServicoResponse.builder()
                .id(tipoServico.getId())
                .categoriaId(tipoServico.getCategoria().getId())
                .categoriaNome(tipoServico.getCategoria().getNome())
                .codigo(tipoServico.getCodigo())
                .nome(tipoServico.getNome())
                .descricao(tipoServico.getDescricao())
                .prazoEstimadoDias(tipoServico.getPrazoEstimadoDias())
                .valorBase(tipoServico.getValorBase())
                .requerVistoria(tipoServico.isRequerVistoria())
                .requerAnaliseTecnica(tipoServico.isRequerAnaliseTecnica())
                .requerAprovacao(tipoServico.isRequerAprovacao())
                .disponivelPortal(tipoServico.isDisponivelPortal())
                .ativo(tipoServico.isAtivo())
                .criadoEm(tipoServico.getCriadoEm())
                .atualizadoEm(tipoServico.getAtualizadoEm())
                .build();
    }
}
