package com.municipio.simple.controller;

import com.municipio.simple.dto.AuthRequest;
import com.municipio.simple.dto.AuthResponse;
import com.municipio.simple.dto.UsuarioRequest;
import com.municipio.simple.dto.UsuarioResponse;
import com.municipio.simple.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody @Valid AuthRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(
            @RequestBody @Valid UsuarioRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }
}
