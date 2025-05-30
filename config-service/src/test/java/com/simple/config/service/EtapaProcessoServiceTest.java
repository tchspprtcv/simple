package com.simple.config.service;

import com.simple.config.client.AuthServiceClient;
import com.simple.config.client.dto.PerfilResponseDTO;
import com.simple.config.domain.entity.EtapaProcesso;
import com.simple.config.dto.EtapaProcessoResponse;
import com.simple.config.repository.EtapaProcessoRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtapaProcessoServiceTest {

    @Mock
    private EtapaProcessoRepository etapaProcessoRepository;

    @Mock
    private AuthServiceClient authServiceClient;

    @InjectMocks
    private EtapaProcessoService etapaProcessoService;

    private EtapaProcesso etapaProcesso;
    private PerfilResponseDTO perfilResponseDTO;

    @BeforeEach
    void setUp() {
        etapaProcesso = EtapaProcesso.builder()
                .id(1)
                .codigo("ETP001")
                .nome("Etapa Teste")
                .descricao("Descrição da Etapa Teste")
                .ordem(1)
                .tempoEstimado(24)
                .requerDocumento(true)
                .requerPagamento(false)
                .requerAprovacao(true)
                .tipoServicoId(10)
                .perfilResponsavelId(5)
                .etapaAnteriorId(null)
                .criadoEm(LocalDateTime.now().minusDays(1))
                .atualizadoEm(LocalDateTime.now())
                .build();

        perfilResponseDTO = new PerfilResponseDTO(5, "Analista", "Perfil de Analista de Processos");
    }

    @Test
    void findById_whenEtapaExistsAndPerfilExists_shouldReturnEnrichedResponse() {
        when(etapaProcessoRepository.findById(1)).thenReturn(Optional.of(etapaProcesso));
        when(authServiceClient.getPerfilById(5)).thenReturn(perfilResponseDTO);

        EtapaProcessoResponse response = etapaProcessoService.findById(1);

        assertNotNull(response);
        assertEquals(etapaProcesso.getNome(), response.getNome());
        assertEquals(perfilResponseDTO.getNome(), response.getPerfilResponsavelNome());
        verify(etapaProcessoRepository).findById(1);
        verify(authServiceClient).getPerfilById(5);
    }

    @Test
    void findById_whenEtapaExistsAndPerfilResponsavelIdIsNull_shouldReturnResponseWithoutPerfilNome() {
        etapaProcesso.setPerfilResponsavelId(null);
        when(etapaProcessoRepository.findById(1)).thenReturn(Optional.of(etapaProcesso));

        EtapaProcessoResponse response = etapaProcessoService.findById(1);

        assertNotNull(response);
        assertEquals(etapaProcesso.getNome(), response.getNome());
        assertNull(response.getPerfilResponsavelNome());
        verify(etapaProcessoRepository).findById(1);
        verify(authServiceClient, never()).getPerfilById(anyInt());
    }

    @Test
    void findById_whenEtapaExistsAndAuthServiceFails_shouldReturnResponseWithDefaultPerfilNome() {
        Request dummyRequest = Request.create(Request.HttpMethod.GET, "/dummy", Collections.emptyMap(), null, new RequestTemplate());
        when(etapaProcessoRepository.findById(1)).thenReturn(Optional.of(etapaProcesso));
        when(authServiceClient.getPerfilById(5)).thenThrow(new FeignException.InternalServerError("Auth service error", dummyRequest, null, null));


        EtapaProcessoResponse response = etapaProcessoService.findById(1);

        assertNotNull(response);
        assertEquals(etapaProcesso.getNome(), response.getNome());
        assertEquals("[Erro ao buscar perfil]", response.getPerfilResponsavelNome());
        verify(etapaProcessoRepository).findById(1);
        verify(authServiceClient).getPerfilById(5);
    }
    
    @Test
    void findById_whenEtapaExistsAndAuthServiceReturnsNotFound_shouldReturnResponseWithDefaultPerfilNome() {
        Request dummyRequest = Request.create(Request.HttpMethod.GET, "/dummy", Collections.emptyMap(), null, new RequestTemplate());
        when(etapaProcessoRepository.findById(1)).thenReturn(Optional.of(etapaProcesso));
        when(authServiceClient.getPerfilById(5)).thenThrow(new FeignException.NotFound("Perfil not found", dummyRequest, null, null));

        EtapaProcessoResponse response = etapaProcessoService.findById(1);

        assertNotNull(response);
        assertEquals(etapaProcesso.getNome(), response.getNome());
        assertEquals("[Erro ao buscar perfil]", response.getPerfilResponsavelNome()); // Or specific message for not found
        verify(etapaProcessoRepository).findById(1);
        verify(authServiceClient).getPerfilById(5);
    }


    @Test
    void findById_whenEtapaNotFound_shouldThrowEntityNotFoundException() {
        when(etapaProcessoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> etapaProcessoService.findById(1));

        verify(etapaProcessoRepository).findById(1);
        verify(authServiceClient, never()).getPerfilById(anyInt());
    }
}
