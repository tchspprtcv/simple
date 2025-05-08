package com.municipio.simple.controller;

import com.municipio.simple.dto.FavoritoRequest;
import com.municipio.simple.dto.FavoritoResponse;
import com.municipio.simple.service.FavoritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FavoritoController {

    private final FavoritoService favoritoService;

    @GetMapping
    public ResponseEntity<List<FavoritoResponse>> listarFavoritos() {
        return ResponseEntity.ok(favoritoService.listarFavoritosDoUsuarioLogado());
    }

    @PostMapping
    public ResponseEntity<FavoritoResponse> adicionarFavorito(@Valid @RequestBody FavoritoRequest favoritoRequest) {
        FavoritoResponse favoritoResponse = favoritoService.adicionarFavorito(favoritoRequest);
        return new ResponseEntity<>(favoritoResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{tipoServicoId}")
    public ResponseEntity<Void> removerFavorito(@PathVariable Integer tipoServicoId) {
        favoritoService.removerFavorito(tipoServicoId);
        return ResponseEntity.noContent().build();
    }
}