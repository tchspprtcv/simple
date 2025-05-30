package com.simple.auth.controller;

import com.simple.auth.dto.AuthRequest;
import com.simple.auth.dto.AuthResponse;
import com.simple.auth.dto.UsuarioRequest; // This is the registration DTO
import com.simple.auth.dto.UsuarioResponse;
import com.simple.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Authentication and registration endpoints")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "User login", description = "Authenticate user with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody @Valid AuthRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content)
    })
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
