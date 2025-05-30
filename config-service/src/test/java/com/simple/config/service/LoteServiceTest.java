package com.simple.config.service;

import com.simple.config.domain.entity.Lote;
import com.simple.config.dto.LoteResponse;
import com.simple.config.repository.LoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoteServiceTest {

    @Mock
    private LoteRepository loteRepository;

    @InjectMocks
    private LoteService loteService;

    private Lote lote1;
    private Lote lote2;
    private UUID lote1Id;
    private UUID lote2Id;

    @BeforeEach
    void setUp() {
        lote1Id = UUID.randomUUID();
        lote1 = Lote.builder()
                .id(lote1Id)
                .codigo("L001")
                .quadra("Q1")
                .numero("N1")
                .area(new BigDecimal("360.50"))
                .endereco("Rua Teste 1")
                .situacao("DISPONIVEL")
                .valorBase(new BigDecimal("100000.00"))
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        lote2Id = UUID.randomUUID();
        lote2 = Lote.builder()
                .id(lote2Id)
                .codigo("L002")
                .quadra("Q2")
                .numero("N2")
                .area(new BigDecimal("450.00"))
                .endereco("Rua Teste 2")
                .situacao("VENDIDO")
                .valorBase(new BigDecimal("150000.00"))
                .build();
    }

    @Test
    void findById_whenLoteExists_shouldReturnLoteResponse() {
        when(loteRepository.findById(lote1Id)).thenReturn(Optional.of(lote1));

        LoteResponse response = loteService.findById(lote1Id);

        assertNotNull(response);
        assertEquals(lote1.getCodigo(), response.getCodigo());
        assertEquals(lote1.getArea(), response.getArea());
        verify(loteRepository).findById(lote1Id);
    }

    @Test
    void findById_whenLoteNotFound_shouldThrowEntityNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();
        when(loteRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loteService.findById(nonExistentId));
        verify(loteRepository).findById(nonExistentId);
    }

    @Test
    void findAll_shouldReturnListOfLoteResponse() {
        when(loteRepository.findAll()).thenReturn(Arrays.asList(lote1, lote2));

        List<LoteResponse> responses = loteService.findAll();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(lote1.getCodigo(), responses.get(0).getCodigo());
        assertEquals(lote2.getCodigo(), responses.get(1).getCodigo());
        verify(loteRepository).findAll();
    }

    @Test
    void findAll_withPageable_shouldReturnPageOfLoteResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Lote> lotes = Arrays.asList(lote1, lote2);
        Page<Lote> lotePage = new PageImpl<>(lotes, pageable, lotes.size());

        when(loteRepository.findAll(pageable)).thenReturn(lotePage);

        Page<LoteResponse> responses = loteService.findAll(pageable);

        assertNotNull(responses);
        assertEquals(2, responses.getTotalElements());
        assertEquals(1, responses.getTotalPages());
        assertEquals(lote1.getCodigo(), responses.getContent().get(0).getCodigo());
        verify(loteRepository).findAll(pageable);
    }

    @Test
    void findByCodigo_whenLoteExists_shouldReturnLoteResponse() {
        when(loteRepository.findByCodigo("L001")).thenReturn(Optional.of(lote1));

        LoteResponse response = loteService.findByCodigo("L001");

        assertNotNull(response);
        assertEquals(lote1.getCodigo(), response.getCodigo());
        assertEquals(lote1.getArea(), response.getArea());
        verify(loteRepository).findByCodigo("L001");
    }

    @Test
    void findByCodigo_whenLoteNotFound_shouldThrowEntityNotFoundException() {
        when(loteRepository.findByCodigo("INEXISTENTE")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loteService.findByCodigo("INEXISTENTE"));
        verify(loteRepository).findByCodigo("INEXISTENTE");
    }
}
