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
import feign.Request;
import feign.RequestTemplate;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritoServiceTest {

    @Mock
    private FavoritoRepository favoritoRepository;

    @Mock
    private ConfigServiceClient configServiceClient;

    @Mock
    private AuthServiceClient authServiceClient;

    @InjectMocks
    private FavoritoService favoritoService;

    private UUID usuarioId;
    private Integer tipoServicoId;
    private Favorito favorito;
    private TipoServicoDTO tipoServicoDTO;
    private UsuarioDTO usuarioDTO;
    private FavoritoRequest favoritoRequest;
     private Request dummyRequest;


    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        tipoServicoId = 1;

        favorito = new Favorito();
        favorito.setId(UUID.randomUUID());
        favorito.setUsuarioId(usuarioId);
        favorito.setTipoServicoId(tipoServicoId);
        favorito.setCriadoEm(LocalDateTime.now());

        tipoServicoDTO = new TipoServicoDTO(tipoServicoId, "SERV001", "Serviço Teste", "Descrição Teste");
        usuarioDTO = new UsuarioDTO(usuarioId, "Nome Usuário Teste", "usuario@teste.com");

        favoritoRequest = new FavoritoRequest();
        favoritoRequest.setTipoServicoId(tipoServicoId);

        dummyRequest = Request.create(Request.HttpMethod.GET, "/dummy", Collections.emptyMap(), null, new RequestTemplate());
    }

    @Test
    void findByUsuarioId_success() {
        when(favoritoRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId)).thenReturn(List.of(favorito));
        when(configServiceClient.getTipoServicoById(tipoServicoId)).thenReturn(tipoServicoDTO);
        when(authServiceClient.getUsuarioById(usuarioId)).thenReturn(usuarioDTO);

        List<FavoritoResponse> responses = favoritoService.findByUsuarioId(usuarioId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        FavoritoResponse response = responses.get(0);
        assertEquals(usuarioDTO.getNome(), response.getUsuarioNome());
        assertNotNull(response.getTipoServicoDetails());
        assertEquals(tipoServicoDTO.getNome(), response.getTipoServicoDetails().getNome());
        verify(favoritoRepository).findByUsuarioIdOrderByCriadoEmDesc(usuarioId);
        verify(configServiceClient).getTipoServicoById(tipoServicoId);
        verify(authServiceClient).getUsuarioById(usuarioId);
    }

    @Test
    void findByUsuarioId_authServiceFails() {
        when(favoritoRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId)).thenReturn(List.of(favorito));
        when(configServiceClient.getTipoServicoById(tipoServicoId)).thenReturn(tipoServicoDTO);
        when(authServiceClient.getUsuarioById(usuarioId)).thenThrow(new FeignException.InternalServerError("Auth service error", dummyRequest, null, null));


        List<FavoritoResponse> responses = favoritoService.findByUsuarioId(usuarioId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        FavoritoResponse response = responses.get(0);
        assertNull(response.getUsuarioNome()); // Or default value if implemented
        assertNotNull(response.getTipoServicoDetails());
        assertEquals(tipoServicoDTO.getNome(), response.getTipoServicoDetails().getNome());
    }

    @Test
    void findByUsuarioId_configServiceFails() {
        when(favoritoRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId)).thenReturn(List.of(favorito));
        when(configServiceClient.getTipoServicoById(tipoServicoId)).thenThrow(new FeignException.InternalServerError("Config service error", dummyRequest, null, null));
        // authServiceClient might or might not be called depending on short-circuit or parallel execution strategy,
        // but for this test, we assume it would be called if config didn't fail, or its result doesn't matter if config fails.
        // If UsuarioDTO is fetched first, then mock it:
        when(authServiceClient.getUsuarioById(usuarioId)).thenReturn(usuarioDTO);


        List<FavoritoResponse> responses = favoritoService.findByUsuarioId(usuarioId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        FavoritoResponse response = responses.get(0);
        assertEquals(usuarioDTO.getNome(), response.getUsuarioNome());
        assertNull(response.getTipoServicoDetails());
    }

    @Test
    void addFavorito_success() {
        when(favoritoRepository.existsByUsuarioIdAndTipoServicoId(usuarioId, tipoServicoId)).thenReturn(false);
        when(configServiceClient.getTipoServicoById(tipoServicoId)).thenReturn(tipoServicoDTO);
        // For addFavorito, current implementation fetches UsuarioDTO only for the response mapping, not before save.
        // So, we mock authServiceClient for the response mapping part if it's called.
        // However, the current mapToResponse in addFavorito directly calls fetchTipoServicoDetails, but not fetchUsuarioDetails.
        // Let's adjust mapToResponse in the service or this test. The service code uses mapToResponse(Favorito, TipoServicoDTO) for add.
        // This specific mapToResponse doesn't fetch User details. Let's assume that's intended for 'add' and only 'read' fetches user details.
        // The service code was: return mapToResponse(savedFavorito, tipoServicoDetails);
        // This mapToResponse is private and has two signatures.
        // mapToResponse(Favorito, TipoServicoDTO) -> used by addFavorito
        // mapToResponse(Favorito, TipoServicoDTO, UsuarioDTO) -> used by mapToResponseWithDetails
        // So, the addFavorito path, as currently written, will NOT include usuarioNome.
        // This is fine, as the enrichment is primarily for GET operations.
        // For the test, we'll verify the response from addFavorito.

        Favorito savedFavorito = new Favorito();
        savedFavorito.setId(UUID.randomUUID());
        savedFavorito.setUsuarioId(usuarioId);
        savedFavorito.setTipoServicoId(tipoServicoId);
        savedFavorito.setCriadoEm(LocalDateTime.now());
        when(favoritoRepository.save(any(Favorito.class))).thenReturn(savedFavorito);

        FavoritoResponse response = favoritoService.addFavorito(usuarioId, favoritoRequest);

        assertNotNull(response);
        assertEquals(tipoServicoId, response.getTipoServicoId());
        assertNotNull(response.getTipoServicoDetails());
        assertEquals(tipoServicoDTO.getNome(), response.getTipoServicoDetails().getNome());
        assertNull(response.getUsuarioNome()); // As per current addFavorito logic
        verify(favoritoRepository).save(any(Favorito.class));
    }


    @Test
    void addFavorito_alreadyExists() {
        when(favoritoRepository.existsByUsuarioIdAndTipoServicoId(usuarioId, tipoServicoId)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> favoritoService.addFavorito(usuarioId, favoritoRequest));
    }

    @Test
    void addFavorito_tipoServicoNotFound() {
        when(favoritoRepository.existsByUsuarioIdAndTipoServicoId(usuarioId, tipoServicoId)).thenReturn(false);
        when(configServiceClient.getTipoServicoById(tipoServicoId)).thenReturn(null); // Or throw FeignException.NotFound
        assertThrows(EntityNotFoundException.class, () -> favoritoService.addFavorito(usuarioId, favoritoRequest));
    }

    @Test
    void removeFavorito_success() {
        when(favoritoRepository.existsByUsuarioIdAndTipoServicoId(usuarioId, tipoServicoId)).thenReturn(true);
        doNothing().when(favoritoRepository).deleteByUsuarioIdAndTipoServicoId(usuarioId, tipoServicoId);
        assertDoesNotThrow(() -> favoritoService.removeFavorito(usuarioId, tipoServicoId));
        verify(favoritoRepository).deleteByUsuarioIdAndTipoServicoId(usuarioId, tipoServicoId);
    }

    @Test
    void removeFavorito_notFound() {
        when(favoritoRepository.existsByUsuarioIdAndTipoServicoId(usuarioId, tipoServicoId)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> favoritoService.removeFavorito(usuarioId, tipoServicoId));
    }
}
