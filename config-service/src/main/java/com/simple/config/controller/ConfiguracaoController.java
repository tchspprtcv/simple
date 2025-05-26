package com.simple.config.controller;

import com.simple.config.dto.ConfiguracaoResponse;
import com.simple.config.service.ConfiguracaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/") // Mapped from /api/configuracoes by gateway
@RequiredArgsConstructor
public class ConfiguracaoController {

    private final ConfiguracaoService configuracaoService;

    @GetMapping
    public ResponseEntity<ConfiguracaoResponse> getTodasConfiguracoesLocais() {
        // This endpoint will provide configurations managed by this service.
        // The API Gateway routes /api/configuracoes here.
        return ResponseEntity.ok(configuracaoService.getTodasConfiguracoesLocais());
    }
}
