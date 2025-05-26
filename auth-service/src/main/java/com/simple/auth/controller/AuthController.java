package com.simple.auth.controller;

import com.simple.auth.dto.AuthRequest;
import com.simple.auth.dto.AuthResponse;
import com.simple.auth.dto.UsuarioRequest; // This is the registration DTO
import com.simple.auth.dto.UsuarioResponse;
import com.simple.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") // Base path for authentication actions
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
            @RequestBody @Valid UsuarioRequest request // UsuarioRequest serves as the DTO for registration
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    // TODO: Add a /auth/refresh-token endpoint if refresh tokens are fully implemented
    // It would take a refresh token and return a new access token.
    // Example:
    // @PostMapping("/refresh-token")
    // public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    //     // ... logic to validate refresh token and issue new access token ...
    //     // This typically involves a RefreshTokenService and storing/validating refresh tokens
    //     return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    // }
}
