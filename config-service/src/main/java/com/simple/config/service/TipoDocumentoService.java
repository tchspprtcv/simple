package com.simple.config.service;

import com.simple.config.domain.entity.TipoDocumento;
import com.simple.config.dto.TipoDocumentoResponse;
import com.simple.config.repository.TipoDocumentoRepository;
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
public class TipoDocumentoService {

    private static final Logger logger = LoggerFactory.getLogger(TipoDocumentoService.class);
    private final TipoDocumentoRepository tipoDocumentoRepository;

    @Transactional(readOnly = true)
    public TipoDocumentoResponse findById(Integer id) {
        logger.info("Attempting to find TipoDocumento with ID: {}", id);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("TipoDocumento not found with ID: {}", id);
                    return new EntityNotFoundException("TipoDocumento não encontrado com ID: " + id);
                });
        logger.info("TipoDocumento found with ID: {}", id);
        return mapToResponse(tipoDocumento);
    }

    @Transactional(readOnly = true)
    public List<TipoDocumentoResponse> findAll(Boolean ativo) {
        logger.info("Attempting to find all TiposDocumento, filtering by ativo: {}", ativo);
        List<TipoDocumento> tiposDocumento;
        if (ativo == null) {
            tiposDocumento = tipoDocumentoRepository.findAll();
        } else {
            tiposDocumento = tipoDocumentoRepository.findByAtivo(ativo);
        }
        return tiposDocumento.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoDocumentoResponse findByCodigo(String codigo) {
        logger.info("Attempting to find TipoDocumento by codigo: {}", codigo);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByCodigo(codigo)
                .orElseThrow(() -> {
                    logger.warn("TipoDocumento not found with codigo: {}", codigo);
                    return new EntityNotFoundException("TipoDocumento não encontrado com codigo: " + codigo);
                });
        logger.info("TipoDocumento found with codigo: {}", codigo);
        return mapToResponse(tipoDocumento);
    }

    private TipoDocumentoResponse mapToResponse(TipoDocumento tipoDocumento) {
        return TipoDocumentoResponse.builder()
                .id(tipoDocumento.getId())
                .codigo(tipoDocumento.getCodigo())
                .nome(tipoDocumento.getNome())
                .descricao(tipoDocumento.getDescricao())
                .formatoPermitido(tipoDocumento.getFormatoPermitido())
                .tamanhoMaximo(tipoDocumento.getTamanhoMaximo())
                .obrigatorio(tipoDocumento.isObrigatorio())
                .ativo(tipoDocumento.isAtivo())
                .criadoEm(tipoDocumento.getCriadoEm())
                .atualizadoEm(tipoDocumento.getAtualizadoEm())
                .build();
    }

    // Basic CRUD methods (create, update, delete) can be added here.
    // For create/update, a TipoDocumentoRequest DTO would be needed.
}
