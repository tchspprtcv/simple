package com.municipio.simple.controller;

import com.municipio.simple.dto.ConfiguracaoResponse;
import com.municipio.simple.service.ConfiguracaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracoes")
@RequiredArgsConstructor
public class ConfiguracaoController {

    private final ConfiguracaoService configuracaoService;

    @GetMapping
    public ResponseEntity<ConfiguracaoResponse> getTodasConfiguracoes() {
        return ResponseEntity.ok(configuracaoService.getTodasConfiguracoes());
    }
}