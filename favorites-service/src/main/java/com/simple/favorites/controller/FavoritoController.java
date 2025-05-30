package com.simple.favorites.controller;

import com.simple.favorites.dto.FavoritoRequest;
import com.simple.favorites.dto.FavoritoResponse;
import com.simple.favorites.service.FavoritoService;
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
import java.util.UUID;

@RestController
@RequestMapping("/favoritos") // API Gateway routes /api/favoritos to this service
@RequiredArgsConstructor
@Tag(name = "Favorites", description = "User favorites management endpoints")
public class FavoritoController {

    private final FavoritoService favoritoService;

    @Operation(summary = "Get favorites by user ID", description = "Retrieve all favorites for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorites retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)))
    })
    // GET / (mapped from /api/favoritos by gateway)
    @GetMapping
    public ResponseEntity<List<FavoritoResponse>> findByUsuarioId(
            @Parameter(description = "User ID") @RequestHeader("X-User-ID") UUID usuarioId // Placeholder for actual user ID from JWT
    ) {
        return ResponseEntity.ok(favoritoService.findByUsuarioId(usuarioId));
    }

    @Operation(summary = "Add favorite", description = "Add a service type to user's favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Favorite added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoritoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Favorite already exists",
                    content = @Content)
    })
    // POST / (mapped from /api/favoritos by gateway)
    @PostMapping
    public ResponseEntity<FavoritoResponse> addFavorito(
            @RequestHeader("X-User-ID") UUID usuarioId, // Placeholder
            @Valid @RequestBody FavoritoRequest request
    ) {
        FavoritoResponse response = favoritoService.addFavorito(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Remove favorite by service type", description = "Remove a favorite by service type ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Favorite removed successfully"),
            @ApiResponse(responseCode = "404", description = "Favorite not found",
                    content = @Content)
    })
    // DELETE /{tipoServicoId} (mapped from /api/favoritos/{tipoServicoId} by gateway)
    @DeleteMapping("/{tipoServicoId}")
    public ResponseEntity<Void> removeFavorito(
            @RequestHeader("X-User-ID") UUID usuarioId, // Placeholder
            @Parameter(description = "Service type ID") @PathVariable Integer tipoServicoId // tipoServicoId is Integer
    ) {
        favoritoService.removeFavorito(usuarioId, tipoServicoId);
        return ResponseEntity.noContent().build();
    }

    // Example of a public test endpoint (already configured in SecurityConfig)
    @GetMapping("/test-public")
    public ResponseEntity<String> testPublicEndpoint() {
        return ResponseEntity.ok("This is a PUBLIC test endpoint from auth-service.");
    }
}
