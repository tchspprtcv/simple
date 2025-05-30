package com.simple.auth.service;

import com.simple.auth.domain.entity.Perfil;
import com.simple.auth.dto.PerfilResponse;
import com.simple.auth.repository.PerfilRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private static final Logger logger = LoggerFactory.getLogger(PerfilService.class);
    private final PerfilRepository perfilRepository;

    @Transactional(readOnly = true)
    public PerfilResponse findPerfilById(Integer id) {
        logger.info("Attempting to find perfil with ID: {}", id);
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Perfil not found with ID: {}", id);
                    return new EntityNotFoundException("Perfil não encontrado com ID: " + id);
                });
        logger.info("Perfil found with ID: {}", id);
        return mapToPerfilResponse(perfil);
    }

    private PerfilResponse mapToPerfilResponse(Perfil perfil) {
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
