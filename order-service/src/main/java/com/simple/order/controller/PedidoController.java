package com.simple.order.controller;

import com.simple.order.dto.PedidoRequest;
import com.simple.order.dto.PedidoResponse;
import com.simple.order.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/") // API Gateway routes /api/pedidos to this service
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // GET / (mapped from /api/pedidos by gateway)
    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(pedidoService.findAll(pageable));
    }

    // GET /{id} (mapped from /api/pedidos/{id} by gateway)
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    // GET /codigo/{codigo} (mapped from /api/pedidos/codigo/{codigo} by gateway)
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<PedidoResponse> findByCodigoAcompanhamento(@PathVariable String codigo) {
        return ResponseEntity.ok(pedidoService.findByCodigoAcompanhamento(codigo));
    }

    // GET /cidadao/{cidadaoId} (mapped from /api/pedidos/cidadao/{cidadaoId} by gateway)
    @GetMapping("/cidadao/{cidadaoId}")
    public ResponseEntity<Page<PedidoResponse>> findByCidadao(
            @PathVariable UUID cidadaoId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(pedidoService.findByCidadaoId(cidadaoId, pageable));
    }

    // GET /usuario/{usuarioId} (e.g. /meus-pedidos, where usuarioId is extracted from JWT)
    // (mapped from /api/pedidos/usuario/{usuarioId} or /api/pedidos/meus-pedidos by gateway)
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<PedidoResponse>> findByUsuarioId(
            @PathVariable UUID usuarioId, // Assuming userId is passed from gateway/auth context
            Pageable pageable
    ) {
        return ResponseEntity.ok(pedidoService.findByUsuarioCriacaoId(usuarioId, pageable));
    }

    // POST / (mapped from /api/pedidos by gateway)
    @PostMapping
    public ResponseEntity<PedidoResponse> create(
            @RequestBody @Valid PedidoRequest request,
            @RequestHeader("X-User-ID") UUID usuarioCriacaoId // Example: Get user ID from custom header set by gateway
    ) {
        // In a real scenario, usuarioCriacaoId would be reliably obtained from the security context (e.g., JWT claims)
        // The gateway could extract it from the token and pass it as a header.
        PedidoResponse response = pedidoService.create(request, usuarioCriacaoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PATCH /{id}/status/{statusId} (mapped from /api/pedidos/{id}/status/{statusId} by gateway)
    @PatchMapping("/{id}/status/{statusId}")
    public ResponseEntity<PedidoResponse> updateStatus(
            @PathVariable UUID id,
            @PathVariable Integer statusId // Changed from Long to Integer
    ) {
        return ResponseEntity.ok(pedidoService.updateStatus(id, statusId));
    }
    
    // PATCH /{id}/responsavel/{usuarioResponsavelId} (assign responsible user)
    @PatchMapping("/{id}/responsavel/{usuarioResponsavelId}")
    public ResponseEntity<PedidoResponse> assignResponsavel(
            @PathVariable UUID id,
            @PathVariable UUID usuarioResponsavelId
    ) {
        return ResponseEntity.ok(pedidoService.assignResponsavel(id, usuarioResponsavelId));
    }
}
