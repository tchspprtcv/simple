package com.simple.config.controller;

import com.simple.config.dto.StatusPedidoResponse;
import com.simple.config.service.StatusPedidoService;
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
@RequestMapping("/status-pedidos") // Using plural form
@RequiredArgsConstructor
@Tag(name = "Status de Pedido Management", description = "Endpoints for managing order statuses")
public class StatusPedidoController {

    private static final Logger logger = LoggerFactory.getLogger(StatusPedidoController.class);
    private final StatusPedidoService statusPedidoService;

    @Operation(summary = "Get Status de Pedido by ID", description = "Retrieve a specific Status de Pedido by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status de Pedido found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusPedidoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Status de Pedido not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<StatusPedidoResponse> findById(@PathVariable Integer id) {
        logger.info("Received request to get StatusPedido by ID: {}", id);
        try {
            StatusPedidoResponse response = statusPedidoService.findById(id);
            logger.info("Successfully retrieved StatusPedido with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            logger.warn("StatusPedido not found for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving StatusPedido with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get all Status de Pedido", description = "Retrieve all Status de Pedido, ordered by 'ordem'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status de Pedido found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class, subTypes = {StatusPedidoResponse.class})))
    })
    @GetMapping
    public ResponseEntity<List<StatusPedidoResponse>> findAll() {
        logger.info("Received request to get all StatusPedido");
        try {
            List<StatusPedidoResponse> responses = statusPedidoService.findAll();
            logger.info("Successfully retrieved all StatusPedido, count: {}", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error retrieving all StatusPedido: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get Status de Pedido by Codigo", description = "Retrieve a specific Status de Pedido by its Codigo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status de Pedido found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusPedidoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Status de Pedido not found for the given codigo",
                    content = @Content)
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<StatusPedidoResponse> findByCodigo(@PathVariable String codigo) {
        logger.info("Received request to get StatusPedido by Codigo: {}", codigo);
        try {
            StatusPedidoResponse response = statusPedidoService.findByCodigo(codigo);
            logger.info("Successfully retrieved StatusPedido with Codigo: {}", codigo);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            logger.warn("StatusPedido not found for Codigo {}: {}", codigo, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving StatusPedido with Codigo {}: {}", codigo, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
