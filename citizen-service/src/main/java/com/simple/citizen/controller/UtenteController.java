package com.simple.citizen.controller;

import com.simple.citizen.dto.CidadaoRequest;
import com.simple.citizen.dto.CidadaoResponse;
import com.simple.citizen.service.UtenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/") // The API Gateway will route /api/cidadaos to this service, so base path here is /
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService cidadaoService;

    // GET / (mapped from /api/cidadaos by gateway)
    @GetMapping
    public ResponseEntity<Page<CidadaoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(cidadaoService.findAll(pageable));
    }

    // GET /{id} (mapped from /api/cidadaos/{id} by gateway)
    @GetMapping("/{id}")
    public ResponseEntity<CidadaoResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(cidadaoService.findById(id));
    }

    // GET /documento (mapped from /api/cidadaos/documento by gateway)
    @GetMapping("/documento")
    public ResponseEntity<CidadaoResponse> findByDocumento(
            @RequestParam String tipoDocumento,
            @RequestParam String numeroDocumento
    ) {
        return ResponseEntity.ok(cidadaoService.findByDocumento(tipoDocumento, numeroDocumento));
    }

    // POST / (mapped from /api/cidadaos by gateway)
    @PostMapping
    public ResponseEntity<CidadaoResponse> create(@RequestBody @Valid CidadaoRequest request) {
        CidadaoResponse response = cidadaoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT /{id} (mapped from /api/cidadaos/{id} by gateway)
    @PutMapping("/{id}")
    public ResponseEntity<CidadaoResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid CidadaoRequest request
    ) {
        return ResponseEntity.ok(cidadaoService.update(id, request));
    }
    
    // DELETE /{id} (mapped from /api/cidadaos/{id} by gateway)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cidadaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
