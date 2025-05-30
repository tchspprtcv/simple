package com.simple.config.service;

import com.simple.config.domain.entity.TipoDocumento;
import com.simple.config.dto.TipoDocumentoResponse;
import com.simple.config.repository.TipoDocumentoRepository;
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
class TipoDocumentoServiceTest {

    @Mock
    private TipoDocumentoRepository tipoDocumentoRepository;

    @InjectMocks
    private TipoDocumentoService tipoDocumentoService;

    private TipoDocumento doc1AtivoObrigatorio;
    private TipoDocumento doc2AtivoNaoObrigatorio;
    private TipoDocumento doc3InativoObrigatorio;

    @BeforeEach
    void setUp() {
        doc1AtivoObrigatorio = TipoDocumento.builder()
                .id(1)
                .codigo("RG")
                .nome("Registro Geral")
                .descricao("Documento de identidade oficial")
                .formatoPermitido("pdf,jpg")
                .tamanhoMaximo(2048)
                .obrigatorio(true)
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        doc2AtivoNaoObrigatorio = TipoDocumento.builder()
                .id(2)
                .codigo("COMPRES")
                .nome("Comprovante de Residência")
                .obrigatorio(false)
                .ativo(true)
                .build();
        
        doc3InativoObrigatorio = TipoDocumento.builder()
                .id(3)
                .codigo("CERTNASC")
                .nome("Certidão de Nascimento")
                .obrigatorio(true)
                .ativo(false)
                .build();
    }

    @Test
    void findById_whenDocumentoExists_shouldReturnDocumentoResponse() {
        when(tipoDocumentoRepository.findById(1)).thenReturn(Optional.of(doc1AtivoObrigatorio));

        TipoDocumentoResponse response = tipoDocumentoService.findById(1);

        assertNotNull(response);
        assertEquals(doc1AtivoObrigatorio.getNome(), response.getNome());
        assertEquals(doc1AtivoObrigatorio.getCodigo(), response.getCodigo());
        assertTrue(response.isObrigatorio());
        assertTrue(response.isAtivo());
        verify(tipoDocumentoRepository).findById(1);
    }

    @Test
    void findById_whenDocumentoNotFound_shouldThrowEntityNotFoundException() {
        when(tipoDocumentoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> tipoDocumentoService.findById(1));
        verify(tipoDocumentoRepository).findById(1);
    }

    @Test
    void findAll_whenAtivoIsNull_shouldReturnAllDocumentos() {
        when(tipoDocumentoRepository.findAll()).thenReturn(Arrays.asList(doc1AtivoObrigatorio, doc2AtivoNaoObrigatorio, doc3InativoObrigatorio));

        List<TipoDocumentoResponse> responses = tipoDocumentoService.findAll(null);

        assertNotNull(responses);
        assertEquals(3, responses.size());
        verify(tipoDocumentoRepository).findAll();
    }

    @Test
    void findAll_whenAtivoIsTrue_shouldReturnOnlyAtivos() {
        when(tipoDocumentoRepository.findByAtivo(true)).thenReturn(Arrays.asList(doc1AtivoObrigatorio, doc2AtivoNaoObrigatorio));

        List<TipoDocumentoResponse> responses = tipoDocumentoService.findAll(true);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertTrue(responses.stream().allMatch(TipoDocumentoResponse::isAtivo));
        verify(tipoDocumentoRepository).findByAtivo(true);
    }

    @Test
    void findAll_whenAtivoIsFalse_shouldReturnOnlyInativos() {
        when(tipoDocumentoRepository.findByAtivo(false)).thenReturn(Arrays.asList(doc3InativoObrigatorio));

        List<TipoDocumentoResponse> responses = tipoDocumentoService.findAll(false);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertFalse(responses.get(0).isAtivo());
        verify(tipoDocumentoRepository).findByAtivo(false);
    }
    
    @Test
    void findByCodigo_whenDocumentoExists_shouldReturnDocumentoResponse() {
        when(tipoDocumentoRepository.findByCodigo("RG")).thenReturn(Optional.of(doc1AtivoObrigatorio));

        TipoDocumentoResponse response = tipoDocumentoService.findByCodigo("RG");

        assertNotNull(response);
        assertEquals(doc1AtivoObrigatorio.getNome(), response.getNome());
        assertEquals(doc1AtivoObrigatorio.getCodigo(), response.getCodigo());
        verify(tipoDocumentoRepository).findByCodigo("RG");
    }

    @Test
    void findByCodigo_whenDocumentoNotFound_shouldThrowEntityNotFoundException() {
        when(tipoDocumentoRepository.findByCodigo("INEXISTENTE")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> tipoDocumentoService.findByCodigo("INEXISTENTE"));
        verify(tipoDocumentoRepository).findByCodigo("INEXISTENTE");
    }
}
