package com.simple.config.controller;

import com.simple.config.dto.TipoServicoRequest;
import com.simple.config.dto.TipoServicoResponse;
import com.simple.config.service.TipoServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-servicos") // Mapped from /api/tipos-servicos by gateway
@RequiredArgsConstructor
public class TipoServicoController {

    private final TipoServicoService tipoServicoService;

    @GetMapping
    public ResponseEntity<List<TipoServicoResponse>> findAll(
            @RequestParam(name = "apenasAtivos", defaultValue = "true") boolean apenasAtivos,
            @RequestParam(name = "categoriaId", required = false) Integer categoriaId) {
        return ResponseEntity.ok(tipoServicoService.findAll(apenasAtivos, categoriaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoServicoResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoServicoService.findById(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<TipoServicoResponse> findByCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(tipoServicoService.findByCodigo(codigo));
    }

    @PostMapping
    public ResponseEntity<TipoServicoResponse> create(@Valid @RequestBody TipoServicoRequest request) {
        TipoServicoResponse response = tipoServicoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoServicoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody TipoServicoRequest request) {
        return ResponseEntity.ok(tipoServicoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoServicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
