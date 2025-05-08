package com.municipio.simple.controller;

import com.municipio.simple.dto.UsuarioRequest;
import com.municipio.simple.dto.UsuarioResponse;
import com.municipio.simple.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> getCurrentUser() {
        return ResponseEntity.ok(usuarioService.getCurrentAuthenticatedUser());
    }

    // Add other user-related endpoints here if needed

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponse> updateCurrentUserProfile(@RequestBody UsuarioRequest usuarioRequest) {
        return ResponseEntity.ok(usuarioService.updateCurrentAuthenticatedUser(usuarioRequest));
    }
}