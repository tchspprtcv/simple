package com.simple.auth.controller;

import com.simple.auth.dto.UsuarioRequest; // DTO for update operations
import com.simple.auth.dto.UsuarioResponse;
import com.simple.auth.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // For more granular access control if needed
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users") // Base path for user-related actions
@RequiredArgsConstructor
public class UserController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> getCurrentUser() {
        // This endpoint will be protected by SecurityConfig to require authentication
        return ResponseEntity.ok(usuarioService.getCurrentAuthenticatedUser());
    }

    @PutMapping("/me")
    // @PreAuthorize("isAuthenticated()") // This is implicitly handled by SecurityConfig global rules
    public ResponseEntity<UsuarioResponse> updateCurrentUserProfile(
            @RequestBody @Valid UsuarioRequest usuarioUpdateRequest // Use @Valid if UsuarioRequest has validation annotations for update
    ) {
        return ResponseEntity.ok(usuarioService.updateCurrentAuthenticatedUser(usuarioUpdateRequest));
    }

    // Example of a test endpoint that requires authentication (already configured in SecurityConfig)
    @GetMapping("/test-secured")
    public ResponseEntity<String> testSecuredEndpoint() {
        return ResponseEntity.ok("This is a SECURED test endpoint from auth-service.");
    }

    // Example of a public test endpoint (already configured in SecurityConfig)
    @GetMapping("/test-public")
    public ResponseEntity<String> testPublicEndpoint() {
        return ResponseEntity.ok("This is a PUBLIC test endpoint from auth-service.");
    }


    // Potential future admin-only endpoints (would require @PreAuthorize("hasRole('ADMIN')") or similar)
    // @GetMapping("/{id}")
    // public ResponseEntity<UsuarioResponse> getUserById(@PathVariable UUID id) {
    //     // return ResponseEntity.ok(usuarioService.findUserById(id));
    //     return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    // }

    // @GetMapping
    // public ResponseEntity<List<UsuarioResponse>> getAllUsers() {
    //     // return ResponseEntity.ok(usuarioService.findAllUsers());
    //     return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    // }
}
