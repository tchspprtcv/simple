package com.simple.favorites.service;

import com.simple.favorites.client.AuthServiceClient;
import com.simple.favorites.client.ConfigServiceClient;
import com.simple.favorites.client.dto.UsuarioDTO;
import com.simple.favorites.domain.entity.Favorito;
import com.simple.favorites.dto.FavoritoRequest;
import com.simple.favorites.dto.FavoritoResponse;
import com.simple.favorites.dto.TipoServicoDTO;
import com.simple.favorites.repository.FavoritoRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final ConfigServiceClient configServiceClient;
    private final AuthServiceClient authServiceClient;

    @Transactional(readOnly = true)
    public List<FavoritoResponse> findByUsuarioId(UUID usuarioId) {
        List<Favorito> favoritos = favoritoRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId);
        return favoritos.stream()
                .map(this::mapToResponseWithDetails)
                .collect(Collectors.toList());
    }

    @Transactional
    public FavoritoResponse addFavorito(UUID usuarioId, FavoritoRequest request) {
        // Check if already favorited
        if (favoritoRepository.existsByUsuarioIdAndTipoServicoId(usuarioId, request.getTipoServicoId())) {
            // Or simply return existing favorite, or throw custom exception
            throw new IllegalArgumentException("Este serviço já foi favoritado."); 
        }

        // Validate TipoServicoId by calling config-service
        TipoServicoDTO tipoServicoDetails = fetchTipoServicoDetails(request.getTipoServicoId());
        if (tipoServicoDetails == null) {
            throw new EntityNotFoundException("Tipo de Serviço não encontrado com ID: " + request.getTipoServicoId());
        }

        // Fetch user details to ensure user exists and to potentially use user's name, though not strictly required for add.
        // For now, let's keep it simple and not fetch user details during add, only during read.
        // If validation of user ID is needed here, it would be a call to authServiceClient.

        Favorito favorito = new Favorito();
        favorito.setUsuarioId(usuarioId);
        favorito.setTipoServicoId(request.getTipoServicoId());
        // criadoEm is set by @CreationTimestamp

        Favorito savedFavorito = favoritoRepository.save(favorito);
        return mapToResponse(savedFavorito, tipoServicoDetails);
    }

    @Transactional
    public void removeFavorito(UUID usuarioId, Integer tipoServicoId) {
        if (!favoritoRepository.existsByUsuarioIdAndTipoServicoId(usuarioId, tipoServicoId)) {
            throw new EntityNotFoundException("Favorito não encontrado para este utilizador e tipo de serviço.");
        }
        favoritoRepository.deleteByUsuarioIdAndTipoServicoId(usuarioId, tipoServicoId);
    }

    private TipoServicoDTO fetchTipoServicoDetails(Integer tipoServicoId) {
        try {
            // This call can throw FeignException if service is down or returns 404
            return configServiceClient.getTipoServicoById(tipoServicoId);
        } catch (Exception e) { // Catching generic Exception, but specific FeignException handling is better
            log.error("Erro ao buscar detalhes do TipoServico ID {}: {}", tipoServicoId, e.getMessage());
            // Depending on requirements, could return null, throw custom error, or have fallback
            return null; 
        }
    }
    
    private FavoritoResponse mapToResponseWithDetails(Favorito favorito) {
        TipoServicoDTO tipoServicoDetails = fetchTipoServicoDetails(favorito.getTipoServicoId());
        // If tipoServicoDetails is null (service unavailable or not found), response will have null details.
        // This can be handled based on requirements (e.g., error, partial response).

        UsuarioDTO usuarioDetails = fetchUsuarioDetails(favorito.getUsuarioId());
        // If usuarioDetails is null, response will have null for usuarioNome.

        return mapToResponse(favorito, tipoServicoDetails, usuarioDetails);
    }

    private UsuarioDTO fetchUsuarioDetails(UUID usuarioId) {
        try {
            return authServiceClient.getUsuarioById(usuarioId);
        } catch (FeignException e) {
            log.error("Erro ao buscar detalhes do Usuario ID {}: {} - Status: {}", usuarioId, e.getMessage(), e.status());
            return null;
        } catch (Exception e) { // Catching other potential exceptions
            log.error("Erro inesperado ao buscar detalhes do Usuario ID {}: {}", usuarioId, e.getMessage());
            return null;
        }
    }

    private FavoritoResponse mapToResponse(Favorito favorito, TipoServicoDTO tipoServicoDetails, UsuarioDTO usuarioDetails) {
        String usuarioNome = null;
        if (usuarioDetails != null) {
            usuarioNome = usuarioDetails.getNome();
        } else {
            // Default or placeholder if user details could not be fetched
            log.warn("Detalhes do usuário não encontrados para ID: {}. Usando nome de usuário padrão/nulo.", favorito.getUsuarioId());
            // usuarioNome = "[Usuário não encontrado]"; // Or keep as null
        }

        return FavoritoResponse.builder()
                .id(favorito.getId())
                .usuarioId(favorito.getUsuarioId())
                .usuarioNome(usuarioNome)
                .tipoServicoId(favorito.getTipoServicoId())
                .criadoEm(favorito.getCriadoEm())
                .tipoServicoDetails(tipoServicoDetails)
                .build();
    }
}
