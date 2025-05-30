package com.simple.config.controller;

import com.simple.config.dto.TipoServicoRequest;
import com.simple.config.dto.TipoServicoResponse;
import com.simple.config.service.TipoServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-servicos") // Mapped from /api/tipos-servicos by gateway
@RequiredArgsConstructor
@Tag(name = "Service Types", description = "Service type management endpoints")
public class TipoServicoController {

    private final TipoServicoService tipoServicoService;

    @Operation(summary = "Get all service types", description = "Retrieve all service types with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service types retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)))
    })
    @GetMapping
    public ResponseEntity<List<TipoServicoResponse>> findAll(
            @Parameter(description = "Filter only active service types") @RequestParam(name = "apenasAtivos", defaultValue = "true") boolean apenasAtivos,
            @Parameter(description = "Filter by category ID") @RequestParam(name = "categoriaId", required = false) Integer categoriaId) {
        return ResponseEntity.ok(tipoServicoService.findAll(apenasAtivos, categoriaId));
    }

    @Operation(summary = "Get service type by ID", description = "Retrieve a specific service type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service type found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoServicoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Service type not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoServicoResponse> findById(
            @Parameter(description = "Service type ID") @PathVariable Integer id) {
        return ResponseEntity.ok(tipoServicoService.findById(id));
    }

    @Operation(summary = "Get service type by code", description = "Retrieve a specific service type by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service type found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoServicoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Service type not found",
                    content = @Content)
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<TipoServicoResponse> findByCodigo(
            @Parameter(description = "Service type code") @PathVariable String codigo) {
        return ResponseEntity.ok(tipoServicoService.findByCodigo(codigo));
    }

    @Operation(summary = "Create new service type", description = "Create a new service type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service type created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoServicoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Service type already exists",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<TipoServicoResponse> create(@Valid @RequestBody TipoServicoRequest request) {
        TipoServicoResponse response = tipoServicoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update service type", description = "Update an existing service type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service type updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoServicoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Service type not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoServicoResponse> update(
            @Parameter(description = "Service type ID") @PathVariable Integer id,
            @Valid @RequestBody TipoServicoRequest request) {
        return ResponseEntity.ok(tipoServicoService.update(id, request));
    }

    @Operation(summary = "Delete service type", description = "Delete a service type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Service type not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Service type ID") @PathVariable Integer id) {
        tipoServicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Example of a public test endpoint (already configured in SecurityConfig)
    @GetMapping("/test-public")
    public ResponseEntity<String> testPublicEndpoint() {
        return ResponseEntity.ok("This is a PUBLIC test endpoint from auth-service.");
    }
}
