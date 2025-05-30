package com.simple.citizen.controller;

import com.simple.citizen.dto.CidadaoRequest;
import com.simple.citizen.dto.CidadaoResponse;
import com.simple.citizen.service.UtenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/utentes") // The API Gateway will route /api/cidadaos to this service, so base path here is /
@RequiredArgsConstructor
@Tag(name = "Utentes", description = "Utentes management endpoints")
public class UtenteController {

    private final UtenteService cidadaoService;

    @Operation(summary = "Get all utentes", description = "Retrieve a paginated list of all utentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))
    })
    // GET / (mapped from /api/cidadaos by gateway)
    @GetMapping
    public ResponseEntity<Page<CidadaoResponse>> findAll(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(cidadaoService.findAll(pageable));
    }

    @Operation(summary = "Get utente by ID", description = "Retrieve a specific utente by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CidadaoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Utente not found",
                    content = @Content)
    })
    // GET /{id} (mapped from /api/cidadaos/{id} by gateway)
    @GetMapping("/{id}")
    public ResponseEntity<CidadaoResponse> findById(
            @Parameter(description = "Utente ID") @PathVariable UUID id) {
        return ResponseEntity.ok(cidadaoService.findById(id));
    }

    @Operation(summary = "Find utente by document", description = "Find a utente by document type and number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CidadaoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Utente not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid document parameters",
                    content = @Content)
    })
    // GET /documento (mapped from /api/cidadaos/documento by gateway)
    @GetMapping("/documento")
    public ResponseEntity<CidadaoResponse> findByDocumento(
            @Parameter(description = "Document type") @RequestParam String tipoDocumento,
            @Parameter(description = "Document number") @RequestParam String numeroDocumento
    ) {
        return ResponseEntity.ok(cidadaoService.findByDocumento(tipoDocumento, numeroDocumento));
    }

    @Operation(summary = "Create new utente", description = "Create a new utente record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utente created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CidadaoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Utente already exists",
                    content = @Content)
    })
    // POST / (mapped from /api/cidadaos by gateway)
    @PostMapping
    public ResponseEntity<CidadaoResponse> create(@RequestBody @Valid CidadaoRequest request) {
        CidadaoResponse response = cidadaoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update utente", description = "Update an existing utente record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CidadaoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Utente not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content)
    })
    // PUT /{id} (mapped from /api/cidadaos/{id} by gateway)
    @PutMapping("/{id}")
    public ResponseEntity<CidadaoResponse> update(
            @Parameter(description = "Utente ID") @PathVariable UUID id,
            @RequestBody @Valid CidadaoRequest request
    ) {
        return ResponseEntity.ok(cidadaoService.update(id, request));
    }
    
    @Operation(summary = "Delete utente", description = "Delete a utente record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Utente deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Utente not found",
                    content = @Content)
    })
    // DELETE /{id} (mapped from /api/cidadaos/{id} by gateway)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Utente ID") @PathVariable UUID id) {
        cidadaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Example of a public test endpoint (already configured in SecurityConfig)
    @GetMapping("/test-public")
    public ResponseEntity<String> testPublicEndpoint() {
        return ResponseEntity.ok("This is a PUBLIC test endpoint from auth-service.");
    }
}
