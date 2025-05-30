package com.simple.config.controller;

import com.simple.config.dto.CategoriaServicoRequest;
import com.simple.config.dto.CategoriaServicoResponse;
import com.simple.config.service.CategoriaServicoService;
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
@RequestMapping("/categorias-servicos") // Mapped from /api/categorias-servicos by gateway
@RequiredArgsConstructor
@Tag(name = "Service Categories", description = "Service category management endpoints")
public class CategoriaServicoController {

    private final CategoriaServicoService categoriaServicoService;

    @Operation(summary = "Get all service categories", description = "Retrieve all service categories with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service categories retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)))
    })
    @GetMapping
    public ResponseEntity<List<CategoriaServicoResponse>> findAll(
            @Parameter(description = "Filter only active service categories") @RequestParam(name = "apenasAtivos", defaultValue = "true") boolean apenasAtivos) {
        return ResponseEntity.ok(categoriaServicoService.findAll(apenasAtivos));
    }

    @Operation(summary = "Get service category by ID", description = "Retrieve a specific service category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service category found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaServicoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Service category not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaServicoResponse> findById(
            @Parameter(description = "Service category ID") @PathVariable Integer id) {
        return ResponseEntity.ok(categoriaServicoService.findById(id));
    }

    @Operation(summary = "Create new service category", description = "Create a new service category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service category created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaServicoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Service category already exists",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CategoriaServicoResponse> create(@Valid @RequestBody CategoriaServicoRequest request) {
        CategoriaServicoResponse response = categoriaServicoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update service category", description = "Update an existing service category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service category updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaServicoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Service category not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaServicoResponse> update(
            @Parameter(description = "Service category ID") @PathVariable Integer id,
            @Valid @RequestBody CategoriaServicoRequest request) {
        return ResponseEntity.ok(categoriaServicoService.update(id, request));
    }

    @Operation(summary = "Delete service category", description = "Delete a service category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Service category not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Service category ID") @PathVariable Integer id) {
        categoriaServicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Example of a public test endpoint (already configured in SecurityConfig)
    @GetMapping("/test-public")
    public ResponseEntity<String> testPublicEndpoint() {
        return ResponseEntity.ok("This is a PUBLIC test endpoint from auth-service.");
    }
}
