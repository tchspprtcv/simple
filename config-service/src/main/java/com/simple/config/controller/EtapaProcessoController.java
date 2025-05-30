package com.simple.config.controller;

import com.simple.config.dto.EtapaProcessoResponse;
import com.simple.config.service.EtapaProcessoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etapas-processo")
@RequiredArgsConstructor
@Tag(name = "Etapa de Processo Management", description = "Endpoints for managing process stages")
public class EtapaProcessoController {

    private static final Logger logger = LoggerFactory.getLogger(EtapaProcessoController.class);
    private final EtapaProcessoService etapaProcessoService;

    @Operation(summary = "Get Etapa de Processo by ID", description = "Retrieve a specific Etapa de Processo by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Etapa de Processo found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EtapaProcessoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Etapa de Processo not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EtapaProcessoResponse> findById(@PathVariable Integer id) {
        logger.info("Received request to get EtapaProcesso by ID: {}", id);
        try {
            EtapaProcessoResponse response = etapaProcessoService.findById(id);
            logger.info("Successfully retrieved EtapaProcesso with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            logger.warn("EtapaProcesso not found for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Or build an error DTO
        } catch (Exception e) {
            logger.error("Error retrieving EtapaProcesso with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Or build an error DTO
        }
    }

    @Operation(summary = "Get all Etapas de Processo", description = "Retrieve all Etapas de Processo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Etapas de Processo found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class, subTypes = {EtapaProcessoResponse.class})))
    })
    @GetMapping
    public ResponseEntity<List<EtapaProcessoResponse>> findAll() {
        logger.info("Received request to get all EtapasProcesso");
        try {
            List<EtapaProcessoResponse> responses = etapaProcessoService.findAll();
            logger.info("Successfully retrieved all EtapasProcesso, count: {}", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error retrieving all EtapasProcesso: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get all Etapas de Processo by TipoServico ID", description = "Retrieve all Etapas de Processo for a given TipoServico ID, ordered by 'ordem'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Etapas de Processo found for TipoServico ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class, subTypes = {EtapaProcessoResponse.class}))),
            @ApiResponse(responseCode = "404", description = "No Etapas de Processo found for TipoServico ID or TipoServico does not exist",
                    content = @Content)
    })
    @GetMapping("/tipo-servico/{tipoServicoId}")
    public ResponseEntity<List<EtapaProcessoResponse>> findByTipoServicoId(@PathVariable Integer tipoServicoId) {
        logger.info("Received request to get all EtapasProcesso for TipoServico ID: {}", tipoServicoId);
        try {
            List<EtapaProcessoResponse> responses = etapaProcessoService.findByTipoServicoId(tipoServicoId);
            logger.info("Successfully retrieved EtapasProcesso for TipoServico ID: {}, count: {}", tipoServicoId, responses.size());
            if (responses.isEmpty()) {
                // Depending on desired behavior, could return 200 with empty list or 404
                // For now, returning 200 with empty list is consistent with JPA behavior if no records found.
                // If TipoServico itself should be validated first, that logic would be in the service.
            }
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error retrieving EtapasProcesso for TipoServico ID {}: {}", tipoServicoId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Basic CRUD operation endpoints (POST, PUT, DELETE) can be added here.
    // They would typically take an EtapaProcessoRequest DTO.
}
