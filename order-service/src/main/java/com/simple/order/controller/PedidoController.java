package com.simple.order.controller;

import com.simple.order.dto.PedidoRequest;
import com.simple.order.dto.PedidoResponse;
import com.simple.order.service.PedidoService;
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
@RequestMapping("/pedidos") // API Gateway routes /api/pedidos to this service
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Pedido management endpoints")
public class PedidoController {

    private final PedidoService pedidoService;

    // GET / (mapped from /api/pedidos by gateway)
    @Operation(summary = "Get all orders", description = "Retrieve all pedidos in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(pedidoService.findAll(pageable));
    }

    // GET /{id} (mapped from /api/pedidos/{id} by gateway)
    @Operation(summary = "Get pedido by ID", description = "Retrieve a specific pedido by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pedido not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> findById(
            @Parameter(description = "Pedido ID") @PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    // GET /codigo/{codigo} (mapped from /api/pedidos/codigo/{codigo} by gateway)
    @Operation(summary = "Get pedido by tracking code", description = "Retrieve an pedido by its tracking code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pedido not found",
                    content = @Content)
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<PedidoResponse> findByCodigoAcompanhamento(
            @Parameter(description = "Pedido tracking code") @PathVariable String codigo) {
        return ResponseEntity.ok(pedidoService.findByCodigoAcompanhamento(codigo));
    }

    // GET /utente/{utenteId} (mapped from /api/pedidos/utente/{utenteId} by gateway)
    @Operation(summary = "Get pedidos by utente ID", description = "Retrieve all pedidos for a specific utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<Page<PedidoResponse>> findByCidadao(
            @Parameter(description = "Citizen ID") @PathVariable UUID utenteId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(pedidoService.findByCidadaoId(utenteId, pageable));
    }

    // GET /meus-pedidos (mapped from /api/pedidos/meus-pedidos by gateway)
    @Operation(summary = "Get current user's pedidos", description = "Retrieve all pedidos for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/meus-pedidos")
    public ResponseEntity<Page<PedidoResponse>> findMyPedidos(
            @RequestHeader("X-User-ID") UUID usuarioId, // Get user ID from custom header set by gateway
            Pageable pageable
    ) {
        // This endpoint assumes the gateway extracts the user ID from the token and passes it as a header
        return ResponseEntity.ok(pedidoService.findByUsuarioCriacaoId(usuarioId, pageable));
    }

    // POST / (mapped from /api/pedidos by gateway)
    @Operation(summary = "Create new pedido", description = "Create a new pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content)
    })
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
    @Operation(summary = "Update pedido status", description = "Update the status of an existing pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pedido not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content)
    })
    @PatchMapping("/{id}/status/{statusId}")
    public ResponseEntity<PedidoResponse> updateStatus(
            @Parameter(description = "Pedido ID") @PathVariable UUID id,
            @Parameter(description = "Status ID") @PathVariable Integer statusId // Changed from Long to Integer
    ) {
        return ResponseEntity.ok(pedidoService.updateStatus(id, statusId));
    }
    
    // PATCH /{id}/responsavel/{usuarioResponsavelId} (assign responsible user)
    @Operation(summary = "Assign responsible user", description = "Assign a responsible user to an pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Responsible user assigned successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pedido not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content)
    })
    @PatchMapping("/{id}/responsavel/{usuarioResponsavelId}")
    public ResponseEntity<PedidoResponse> assignResponsavel(
            @Parameter(description = "Pedido ID") @PathVariable UUID id,
            @Parameter(description = "Responsible User ID") @PathVariable UUID usuarioResponsavelId
    ) {
        return ResponseEntity.ok(pedidoService.assignResponsavel(id, usuarioResponsavelId));
    }

    // Example of a public test endpoint (already configured in SecurityConfig)
    @GetMapping("/test-public")
    public ResponseEntity<String> testPublicEndpoint() {
        return ResponseEntity.ok("This is a PUBLIC test endpoint from auth-service.");
    }
}
