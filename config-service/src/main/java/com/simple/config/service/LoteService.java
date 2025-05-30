package com.simple.config.service;

import com.simple.config.domain.entity.Lote;
import com.simple.config.dto.LoteResponse;
import com.simple.config.repository.LoteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoteService {

    private static final Logger logger = LoggerFactory.getLogger(LoteService.class);
    private final LoteRepository loteRepository;

    @Transactional(readOnly = true)
    public LoteResponse findById(UUID id) {
        logger.info("Attempting to find Lote with ID: {}", id);
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Lote not found with ID: {}", id);
                    return new EntityNotFoundException("Lote não encontrado com ID: " + id);
                });
        logger.info("Lote found with ID: {}", id);
        return mapToResponse(lote);
    }

    @Transactional(readOnly = true)
    public List<LoteResponse> findAll() {
        logger.info("Attempting to find all Lotes");
        List<Lote> lotes = loteRepository.findAll(); // Consider Pageable for large datasets
        return lotes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<LoteResponse> findAll(Pageable pageable) {
        logger.info("Attempting to find all Lotes with pagination: {}", pageable);
        Page<Lote> lotesPage = loteRepository.findAll(pageable);
        return lotesPage.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public LoteResponse findByCodigo(String codigo) {
        logger.info("Attempting to find Lote by codigo: {}", codigo);
        Lote lote = loteRepository.findByCodigo(codigo)
                .orElseThrow(() -> {
                    logger.warn("Lote not found with codigo: {}", codigo);
                    return new EntityNotFoundException("Lote não encontrado com codigo: " + codigo);
                });
        logger.info("Lote found with codigo: {}", codigo);
        return mapToResponse(lote);
    }

    private LoteResponse mapToResponse(Lote lote) {
        return LoteResponse.builder()
                .id(lote.getId())
                .codigo(lote.getCodigo())
                .quadra(lote.getQuadra())
                .numero(lote.getNumero())
                .area(lote.getArea())
                .endereco(lote.getEndereco())
                .bairro(lote.getBairro())
                .cidade(lote.getCidade())
                .estado(lote.getEstado())
                .cep(lote.getCep())
                .latitude(lote.getLatitude())
                .longitude(lote.getLongitude())
                .situacao(lote.getSituacao())
                .valorBase(lote.getValorBase())
                .observacoes(lote.getObservacoes())
                .criadoEm(lote.getCriadoEm())
                .atualizadoEm(lote.getAtualizadoEm())
                .build();
    }

    // Basic CRUD methods (create, update, delete) can be added here.
    // For create/update, a LoteRequest DTO would be needed.
}
