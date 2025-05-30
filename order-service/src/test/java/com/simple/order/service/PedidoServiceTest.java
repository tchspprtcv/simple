package com.simple.order.service;

import com.simple.order.client.AuthServiceClient;
import com.simple.order.client.CitizenServiceClient;
import com.simple.order.client.ConfigServiceClient;
import com.simple.order.client.dto.*; // Import all DTOs
import com.simple.order.domain.entity.Pedido;
import com.simple.order.domain.entity.StatusPedido;
import com.simple.order.dto.PedidoResponse;
import com.simple.order.repository.PedidoRepository;
import com.simple.order.repository.StatusPedidoRepository;


import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
// Add other necessary imports like BigDecimal, LocalDateTime

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private StatusPedidoRepository statusPedidoRepository; // Not directly used in mapToResponse tests but good to have if other service methods are tested

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private CitizenServiceClient citizenServiceClient;

    @Mock
    private ConfigServiceClient configServiceClient;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private UUID pedidoId;

    @BeforeEach
    void setUp() {
        pedidoId = UUID.randomUUID();
        pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setCidadaoId(UUID.randomUUID());
        pedido.setTipoServicoId(1);
        pedido.setUsuarioCriacaoId(UUID.randomUUID());
        pedido.setDataInicio(LocalDateTime.now());
        pedido.setCriadoEm(LocalDateTime.now());
        pedido.setAtualizadoEm(LocalDateTime.now());

        StatusPedido statusPedido = new StatusPedido();
        statusPedido.setId(1);
        statusPedido.setNome("Novo Status");
        statusPedido.setCodigo("NOVO");
        pedido.setStatus(statusPedido);

        // Common mock for findById, as all test cases will go through it
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
    }

    @Test
    void mapToResponse_shouldEnrichAllFieldsSuccessfully() {
        pedido.setUsuarioResponsavelId(UUID.randomUUID());
        pedido.setEtapaAtualId(2);

        when(citizenServiceClient.getCidadaoById(pedido.getCidadaoId())).thenReturn(new CidadaoResponse("Nome Cidadao"));
        when(configServiceClient.getTipoServicoById(pedido.getTipoServicoId())).thenReturn(new TipoServicoResponse("Nome Tipo Servico"));
        when(authServiceClient.getUsuarioById(pedido.getUsuarioCriacaoId())).thenReturn(new UsuarioResponse("Nome Usuario Criacao"));
        when(authServiceClient.getUsuarioById(pedido.getUsuarioResponsavelId())).thenReturn(new UsuarioResponse("Nome Usuario Responsavel"));
        when(configServiceClient.getEtapaProcessoById(pedido.getEtapaAtualId())).thenReturn(new EtapaProcessoResponse("Nome Etapa Processo"));

        PedidoResponse response = pedidoService.findById(pedidoId);

        assertNotNull(response);
        assertEquals("Nome Cidadao", response.getCidadaoNome());
        assertEquals("Nome Tipo Servico", response.getTipoServicoNome());
        assertEquals("Nome Usuario Criacao", response.getUsuarioCriacaoNome());
        assertEquals("Nome Usuario Responsavel", response.getUsuarioResponsavelNome());
        assertEquals("Nome Etapa Processo", response.getEtapaAtualNome());
    }

    @Test
    void mapToResponse_shouldPartiallyEnrichWhenOptionalFieldsAreNull() {
        pedido.setUsuarioResponsavelId(null);
        pedido.setEtapaAtualId(null);

        when(citizenServiceClient.getCidadaoById(pedido.getCidadaoId())).thenReturn(new CidadaoResponse("Nome Cidadao"));
        when(configServiceClient.getTipoServicoById(pedido.getTipoServicoId())).thenReturn(new TipoServicoResponse("Nome Tipo Servico"));
        when(authServiceClient.getUsuarioById(pedido.getUsuarioCriacaoId())).thenReturn(new UsuarioResponse("Nome Usuario Criacao"));

        PedidoResponse response = pedidoService.findById(pedidoId);

        assertNotNull(response);
        assertEquals("Nome Cidadao", response.getCidadaoNome());
        assertEquals("Nome Tipo Servico", response.getTipoServicoNome());
        assertEquals("Nome Usuario Criacao", response.getUsuarioCriacaoNome());
        assertNull(response.getUsuarioResponsavelNome());
        assertNull(response.getEtapaAtualNome());

        verify(authServiceClient, times(1)).getUsuarioById(pedido.getUsuarioCriacaoId());
        verify(authServiceClient, never()).getUsuarioById(null); // Ensure it's not called with null for responsavel
        verify(configServiceClient, never()).getEtapaProcessoById(any()); // Corrected any()
    }


    @Test
    void mapToResponse_shouldHandleErrorFromCitizenService() {
        pedido.setUsuarioResponsavelId(null); // Keep it simple
        pedido.setEtapaAtualId(null);

        when(citizenServiceClient.getCidadaoById(pedido.getCidadaoId())).thenThrow(FeignException.class);
        when(configServiceClient.getTipoServicoById(pedido.getTipoServicoId())).thenReturn(new TipoServicoResponse("Nome Tipo Servico"));
        when(authServiceClient.getUsuarioById(pedido.getUsuarioCriacaoId())).thenReturn(new UsuarioResponse("Nome Usuario Criacao"));

        PedidoResponse response = pedidoService.findById(pedidoId);

        assertNotNull(response);
        assertNull(response.getCidadaoNome());
        assertEquals("Nome Tipo Servico", response.getTipoServicoNome());
        assertEquals("Nome Usuario Criacao", response.getUsuarioCriacaoNome());
        assertNull(response.getUsuarioResponsavelNome());
        assertNull(response.getEtapaAtualNome());
    }

    @Test
    void mapToResponse_shouldHandleErrorsFromAllServices() {
        pedido.setUsuarioResponsavelId(UUID.randomUUID());
        pedido.setEtapaAtualId(2);

        when(citizenServiceClient.getCidadaoById(pedido.getCidadaoId())).thenThrow(FeignException.class);
        when(configServiceClient.getTipoServicoById(pedido.getTipoServicoId())).thenThrow(FeignException.class);
        when(authServiceClient.getUsuarioById(pedido.getUsuarioCriacaoId())).thenThrow(FeignException.class);
        when(authServiceClient.getUsuarioById(pedido.getUsuarioResponsavelId())).thenThrow(FeignException.class);
        when(configServiceClient.getEtapaProcessoById(pedido.getEtapaAtualId())).thenThrow(FeignException.class);

        PedidoResponse response = pedidoService.findById(pedidoId);

        assertNotNull(response);
        assertNull(response.getCidadaoNome());
        assertNull(response.getTipoServicoNome());
        assertNull(response.getUsuarioCriacaoNome());
        assertNull(response.getUsuarioResponsavelNome());
        assertNull(response.getEtapaAtualNome());
    }
}
