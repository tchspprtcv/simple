package com.simple.config.controller;

import com.simple.config.dto.TipoDocumentoResponse;
import com.simple.config.service.TipoDocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/tipos-documento") // As per instruction for singular endpoint path, but controller name is plural
@RequiredArgsConstructor
@Tag(name = "Tipos de Documento Management", description = "Endpoints for managing document types")
public class TipoDocumentoController {

    private static final Logger logger = LoggerFactory.getLogger(TipoDocumentoController.class);
    private final TipoDocumentoService tipoDocumentoService;

    @Operation(summary = "Get Tipo de Documento by ID", description = "Retrieve a specific Tipo de Documento by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de Documento found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoDocumentoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de Documento not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumentoResponse> findById(@PathVariable Integer id) {
        logger.info("Received request to get TipoDocumento by ID: {}", id);
        try {
            TipoDocumentoResponse response = tipoDocumentoService.findById(id);
            logger.info("Successfully retrieved TipoDocumento with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            logger.warn("TipoDocumento not found for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving TipoDocumento with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get all Tipos de Documento", description = "Retrieve all Tipos de Documento, optionally filtered by 'ativo' status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipos de Documento found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class, subTypes = {TipoDocumentoResponse.class})))
    })
    @GetMapping
    public ResponseEntity<List<TipoDocumentoResponse>> findAll(
            @Parameter(description = "Filter by active status (true or false). If not provided, returns all.")
            @RequestParam(required = false) Boolean ativo) {
        logger.info("Received request to get all TiposDocumento with ativo filter: {}", ativo);
        try {
            List<TipoDocumentoResponse> responses = tipoDocumentoService.findAll(ativo);
            logger.info("Successfully retrieved TiposDocumento, count: {}", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error retrieving TiposDocumento: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Get Tipo de Documento by Codigo", description = "Retrieve a specific Tipo de Documento by its Codigo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de Documento found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoDocumentoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de Documento not found for the given codigo",
                    content = @Content)
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<TipoDocumentoResponse> findByCodigo(@PathVariable String codigo) {
        logger.info("Received request to get TipoDocumento by Codigo: {}", codigo);
        try {
            TipoDocumentoResponse response = tipoDocumentoService.findByCodigo(codigo);
            logger.info("Successfully retrieved TipoDocumento with Codigo: {}", codigo);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            logger.warn("TipoDocumento not found for Codigo {}: {}", codigo, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving TipoDocumento with Codigo {}: {}", codigo, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
