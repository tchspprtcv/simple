package com.municipio.simple.controller;

import com.municipio.simple.dto.PedidoRequest;
import com.municipio.simple.dto.PedidoResponse;
import com.municipio.simple.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ATENDENTE', 'GESTOR', 'TECNICO')")
    public ResponseEntity<Page<PedidoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(pedidoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ATENDENTE', 'GESTOR', 'TECNICO', 'FISCAL')")
    public ResponseEntity<PedidoResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<PedidoResponse> findByCodigoAcompanhamento(@PathVariable String codigo) {
        return ResponseEntity.ok(pedidoService.findByCodigoAcompanhamento(codigo));
    }

    @GetMapping("/cidadao/{cidadaoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ATENDENTE', 'GESTOR')")
    public ResponseEntity<Page<PedidoResponse>> findByCidadao(
            @PathVariable UUID cidadaoId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(pedidoService.findByCidadao(cidadaoId, pageable));
    }

    @GetMapping("/meus-pedidos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<PedidoResponse>> findByUsuarioLogado(Pageable pageable) {
        return ResponseEntity.ok(pedidoService.findByUsuarioLogado(pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ATENDENTE')")
    public ResponseEntity<PedidoResponse> create(@RequestBody @Valid PedidoRequest request) {
        return ResponseEntity.ok(pedidoService.create(request));
    }

    @PatchMapping("/{id}/status/{statusId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ATENDENTE', 'GESTOR', 'TECNICO')")
    public ResponseEntity<PedidoResponse> updateStatus(
            @PathVariable UUID id,
            @PathVariable Long statusId
    ) {
        return ResponseEntity.ok(pedidoService.updateStatus(id, statusId));
    }
}
