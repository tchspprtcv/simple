package com.simple.favorites.controller;

import com.simple.favorites.dto.FavoritoRequest;
import com.simple.favorites.dto.FavoritoResponse;
import com.simple.favorites.service.FavoritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/") // API Gateway routes /api/favoritos to this service
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    // GET / (mapped from /api/favoritos by gateway)
    @GetMapping
    public ResponseEntity<List<FavoritoResponse>> findByUsuarioId(
            @RequestHeader("X-User-ID") UUID usuarioId // Placeholder for actual user ID from JWT
    ) {
        return ResponseEntity.ok(favoritoService.findByUsuarioId(usuarioId));
    }

    // POST / (mapped from /api/favoritos by gateway)
    @PostMapping
    public ResponseEntity<FavoritoResponse> addFavorito(
            @RequestHeader("X-User-ID") UUID usuarioId, // Placeholder
            @Valid @RequestBody FavoritoRequest request
    ) {
        FavoritoResponse response = favoritoService.addFavorito(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // DELETE /{tipoServicoId} (mapped from /api/favoritos/{tipoServicoId} by gateway)
    @DeleteMapping("/{tipoServicoId}")
    public ResponseEntity<Void> removeFavorito(
            @RequestHeader("X-User-ID") UUID usuarioId, // Placeholder
            @PathVariable Integer tipoServicoId // tipoServicoId is Integer
    ) {
        favoritoService.removeFavorito(usuarioId, tipoServicoId);
        return ResponseEntity.noContent().build();
    }
}
