package com.simple.config.controller;

import com.simple.config.dto.CategoriaServicoRequest;
import com.simple.config.dto.CategoriaServicoResponse;
import com.simple.config.service.CategoriaServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias-servicos") // Mapped from /api/categorias-servicos by gateway
@RequiredArgsConstructor
public class CategoriaServicoController {

    private final CategoriaServicoService categoriaServicoService;

    @GetMapping
    public ResponseEntity<List<CategoriaServicoResponse>> findAll(
            @RequestParam(name = "apenasAtivos", defaultValue = "true") boolean apenasAtivos) {
        return ResponseEntity.ok(categoriaServicoService.findAll(apenasAtivos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaServicoResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaServicoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaServicoResponse> create(@Valid @RequestBody CategoriaServicoRequest request) {
        CategoriaServicoResponse response = categoriaServicoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaServicoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaServicoRequest request) {
        return ResponseEntity.ok(categoriaServicoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoriaServicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
