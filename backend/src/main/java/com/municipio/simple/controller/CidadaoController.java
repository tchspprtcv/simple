package com.municipio.simple.controller;

import com.municipio.simple.dto.CidadaoRequest;
import com.municipio.simple.dto.CidadaoResponse;
import com.municipio.simple.service.CidadaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cidadaos")
@RequiredArgsConstructor
public class CidadaoController {

    private final CidadaoService cidadaoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ATENDENTE', 'GESTOR')")
    public ResponseEntity<Page<CidadaoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(cidadaoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ATENDENTE', 'GESTOR', 'TECNICO')")
    public ResponseEntity<CidadaoResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(cidadaoService.findById(id));
    }

    @GetMapping("/documento")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ATENDENTE', 'GESTOR', 'TECNICO')")
    public ResponseEntity<CidadaoResponse> findByDocumento(
            @RequestParam String tipoDocumento,
            @RequestParam String numeroDocumento
    ) {
        return ResponseEntity.ok(cidadaoService.findByDocumento(tipoDocumento, numeroDocumento));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ATENDENTE')")
    public ResponseEntity<CidadaoResponse> create(@RequestBody @Valid CidadaoRequest request) {
        return ResponseEntity.ok(cidadaoService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ATENDENTE')")
    public ResponseEntity<CidadaoResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid CidadaoRequest request
    ) {
        return ResponseEntity.ok(cidadaoService.update(id, request));
    }
}
