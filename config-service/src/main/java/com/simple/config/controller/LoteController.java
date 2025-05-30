package com.simple.config.controller;

import com.simple.config.dto.LoteResponse;
import com.simple.config.service.LoteService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lotes")
@RequiredArgsConstructor
@Tag(name = "Lote Management", description = "Endpoints for managing land lots")
public class LoteController {

    private static final Logger logger = LoggerFactory.getLogger(LoteController.class);
    private final LoteService loteService;

    @Operation(summary = "Get Lote by ID", description = "Retrieve a specific Lote by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lote found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoteResponse> findById(@PathVariable UUID id) {
        logger.info("Received request to get Lote by ID: {}", id);
        try {
            LoteResponse response = loteService.findById(id);
            logger.info("Successfully retrieved Lote with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            logger.warn("Lote not found for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving Lote with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get all Lotes (paginated)", description = "Retrieve all Lotes with pagination support.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lotes found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))) // Page<LoteResponse>
    })
    @GetMapping
    public ResponseEntity<Page<LoteResponse>> findAll(@PageableDefault(size = 20, sort = "codigo") Pageable pageable) {
        logger.info("Received request to get all Lotes with pagination: {}", pageable);
        try {
            Page<LoteResponse> responses = loteService.findAll(pageable);
            logger.info("Successfully retrieved Lotes, page: {}, total elements: {}", responses.getNumber(), responses.getTotalElements());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error retrieving Lotes with pagination: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get Lote by Codigo", description = "Retrieve a specific Lote by its Codigo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lote found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote not found for the given codigo",
                    content = @Content)
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<LoteResponse> findByCodigo(@PathVariable String codigo) {
        logger.info("Received request to get Lote by Codigo: {}", codigo);
        try {
            LoteResponse response = loteService.findByCodigo(codigo);
            logger.info("Successfully retrieved Lote with Codigo: {}", codigo);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            logger.warn("Lote not found for Codigo {}: {}", codigo, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving Lote with Codigo {}: {}", codigo, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
