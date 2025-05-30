package com.simple.config.service;

import com.simple.config.client.AuthServiceClient;
import com.simple.config.client.dto.PerfilResponseDTO;
import com.simple.config.domain.entity.EtapaProcesso;
import com.simple.config.dto.EtapaProcessoResponse;
import com.simple.config.repository.EtapaProcessoRepository;
import feign.FeignException;
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
public class EtapaProcessoService {

    private static final Logger logger = LoggerFactory.getLogger(EtapaProcessoService.class);
    private final EtapaProcessoRepository etapaProcessoRepository;
    private final AuthServiceClient authServiceClient;

    @Transactional(readOnly = true)
    public EtapaProcessoResponse findById(Integer id) {
        logger.info("Attempting to find EtapaProcesso with ID: {}", id);
        EtapaProcesso etapaProcesso = etapaProcessoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("EtapaProcesso not found with ID: {}", id);
                    return new EntityNotFoundException("EtapaProcesso não encontrada com ID: " + id);
                });
        logger.info("EtapaProcesso found with ID: {}", id);
        return mapToResponse(etapaProcesso);
    }

    @Transactional(readOnly = true)
    public List<EtapaProcessoResponse> findAll() {
        logger.info("Attempting to find all EtapasProcesso");
        return etapaProcessoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EtapaProcessoResponse> findByTipoServicoId(Integer tipoServicoId) {
        logger.info("Attempting to find all EtapasProcesso for TipoServico ID: {}", tipoServicoId);
        return etapaProcessoRepository.findByTipoServicoIdOrderByOrdemAsc(tipoServicoId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private EtapaProcessoResponse mapToResponse(EtapaProcesso etapaProcesso) {
        EtapaProcessoResponse response = EtapaProcessoResponse.builder()
                .id(etapaProcesso.getId())
                .codigo(etapaProcesso.getCodigo())
                .nome(etapaProcesso.getNome())
                .descricao(etapaProcesso.getDescricao())
                .ordem(etapaProcesso.getOrdem())
                .tempoEstimado(etapaProcesso.getTempoEstimado())
                .requerDocumento(etapaProcesso.isRequerDocumento())
                .requerPagamento(etapaProcesso.isRequerPagamento())
                .requerAprovacao(etapaProcesso.isRequerAprovacao())
                .tipoServicoId(etapaProcesso.getTipoServicoId())
                .perfilResponsavelId(etapaProcesso.getPerfilResponsavelId())
                .etapaAnteriorId(etapaProcesso.getEtapaAnteriorId())
                .criadoEm(etapaProcesso.getCriadoEm())
                .atualizadoEm(etapaProcesso.getAtualizadoEm())
                .build();

        if (etapaProcesso.getPerfilResponsavelId() != null) {
            try {
                logger.info("Fetching PerfilResponsavelNome for ID: {}", etapaProcesso.getPerfilResponsavelId());
                PerfilResponseDTO perfil = authServiceClient.getPerfilById(etapaProcesso.getPerfilResponsavelId());
                if (perfil != null) {
                    response.setPerfilResponsavelNome(perfil.getNome());
                    logger.info("Successfully fetched PerfilResponsavelNome: {}", perfil.getNome());
                } else {
                     logger.warn("PerfilResponsavelNome not found for ID: {} (authServiceClient returned null)", etapaProcesso.getPerfilResponsavelId());
                    response.setPerfilResponsavelNome("[Perfil não encontrado ou nulo]");
                }
            } catch (FeignException e) {
                logger.error("Error fetching PerfilResponsavelNome for ID: {} from auth-service. Error: {} - Status: {}",
                        etapaProcesso.getPerfilResponsavelId(), e.getMessage(), e.status(), e);
                response.setPerfilResponsavelNome("[Erro ao buscar perfil]");
            } catch (Exception e) {
                logger.error("Unexpected error fetching PerfilResponsavelNome for ID: {}. Error: {}",
                        etapaProcesso.getPerfilResponsavelId(), e.getMessage(), e);
                response.setPerfilResponsavelNome("[Erro inesperado ao buscar perfil]");
            }
        }
        return response;
    }

    // Basic CRUD methods (create, update, delete) can be added here.
    // For create/update, an EtapaProcessoRequest DTO would be needed.
    // Example:
    // public EtapaProcessoResponse create(EtapaProcessoRequest request) { ... }
    // public EtapaProcessoResponse update(Integer id, EtapaProcessoRequest request) { ... }
    // public void delete(Integer id) { ... }
}
