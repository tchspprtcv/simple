package com.simple.config.service;

import com.simple.config.domain.entity.StatusPedido;
import com.simple.config.dto.StatusPedidoResponse;
import com.simple.config.repository.StatusPedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusPedidoServiceTest {

    @Mock
    private StatusPedidoRepository statusPedidoRepository;

    @InjectMocks
    private StatusPedidoService statusPedidoService;

    private StatusPedido status1;
    private StatusPedido status2;

    @BeforeEach
    void setUp() {
        status1 = StatusPedido.builder()
                .id(1)
                .codigo("NOVO")
                .nome("Novo Pedido")
                .descricao("Pedido recém criado")
                .cor("#007BFF")
                .icone("fas fa-plus-circle")
                .ordem(1)
                .visivelPortal(true)
                .criadoEm(LocalDateTime.now().minusDays(2))
                .atualizadoEm(LocalDateTime.now().minusDays(1))
                .build();

        status2 = StatusPedido.builder()
                .id(2)
                .codigo("EM_PROCESSAMENTO")
                .nome("Em Processamento")
                .descricao("Pedido sendo processado")
                .cor("#FFC107")
                .icone("fas fa-cogs")
                .ordem(2)
                .visivelPortal(true)
                .criadoEm(LocalDateTime.now().minusDays(1))
                .atualizadoEm(LocalDateTime.now())
                .build();
    }

    @Test
    void findById_whenStatusExists_shouldReturnStatusResponse() {
        when(statusPedidoRepository.findById(1)).thenReturn(Optional.of(status1));

        StatusPedidoResponse response = statusPedidoService.findById(1);

        assertNotNull(response);
        assertEquals(status1.getNome(), response.getNome());
        assertEquals(status1.getCodigo(), response.getCodigo());
        verify(statusPedidoRepository).findById(1);
    }

    @Test
    void findById_whenStatusNotFound_shouldThrowEntityNotFoundException() {
        when(statusPedidoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> statusPedidoService.findById(1));
        verify(statusPedidoRepository).findById(1);
    }

    @Test
    void findAll_shouldReturnListOfStatusResponse() {
        when(statusPedidoRepository.findAllByOrderByOrdemAsc()).thenReturn(Arrays.asList(status1, status2));

        List<StatusPedidoResponse> responses = statusPedidoService.findAll();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(status1.getNome(), responses.get(0).getNome());
        assertEquals(status2.getNome(), responses.get(1).getNome());
        verify(statusPedidoRepository).findAllByOrderByOrdemAsc();
    }
    
    @Test
    void findByCodigo_whenStatusExists_shouldReturnStatusResponse() {
        when(statusPedidoRepository.findByCodigo("NOVO")).thenReturn(Optional.of(status1));

        StatusPedidoResponse response = statusPedidoService.findByCodigo("NOVO");

        assertNotNull(response);
        assertEquals(status1.getNome(), response.getNome());
        assertEquals(status1.getCodigo(), response.getCodigo());
        verify(statusPedidoRepository).findByCodigo("NOVO");
    }

    @Test
    void findByCodigo_whenStatusNotFound_shouldThrowEntityNotFoundException() {
        when(statusPedidoRepository.findByCodigo("INEXISTENTE")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> statusPedidoService.findByCodigo("INEXISTENTE"));
        verify(statusPedidoRepository).findByCodigo("INEXISTENTE");
    }
}
