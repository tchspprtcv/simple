package com.simple.config.controller;

import com.simple.config.dto.ConfiguracaoResponse;
import com.simple.config.service.ConfiguracaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracoes") // Mapped from /api/configuracoes by gateway
@RequiredArgsConstructor
@Tag(name = "Configuration", description = "Application configuration endpoints")
public class ConfiguracaoController {

    private final ConfiguracaoService configuracaoService;

    @Operation(summary = "Get all configurations", description = "Retrieve all application configurations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configurations retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConfiguracaoResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ConfiguracaoResponse> getTodasConfiguracoesLocais() {
        // This endpoint will provide configurations managed by this service.
        // The API Gateway routes /api/configuracoes here.
        return ResponseEntity.ok(configuracaoService.getTodasConfiguracoesLocais());
    }

    // Example of a public test endpoint (already configured in SecurityConfig)
    @GetMapping("/test-public")
    public ResponseEntity<String> testPublicEndpoint() {
        return ResponseEntity.ok("This is a PUBLIC test endpoint from auth-service.");
    }
}
