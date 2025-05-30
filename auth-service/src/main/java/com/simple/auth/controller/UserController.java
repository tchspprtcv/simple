package com.simple.auth.controller;

import com.simple.auth.dto.UsuarioRequest; // DTO for update operations
import com.simple.auth.dto.UsuarioResponse;
import com.simple.auth.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // For more granular access control if needed
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users") // Base path for user-related actions
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User profile and account management endpoints")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UsuarioService usuarioService;

    @Operation(summary = "Get current user profile", description = "Get the profile of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> getCurrentUser() {
        // This endpoint will be protected by SecurityConfig to require authentication
        return ResponseEntity.ok(usuarioService.getCurrentAuthenticatedUser());
    }

    @Operation(summary = "Update current user profile", description = "Update the profile of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request format",
                    content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/me")
    // @PreAuthorize("isAuthenticated()") // This is implicitly handled by SecurityConfig global rules
    public ResponseEntity<UsuarioResponse> updateCurrentUserProfile(
            @RequestBody @Valid UsuarioRequest usuarioUpdateRequest // Use @Valid if UsuarioRequest has validation annotations for update
    ) {
        return ResponseEntity.ok(usuarioService.updateCurrentAuthenticatedUser(usuarioUpdateRequest));
    }

    @Operation(summary = "Test secured endpoint", description = "Test endpoint that requires authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access granted",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    // Example of a test endpoint that requires authentication (already configured in SecurityConfig)
    @GetMapping("/test-secured")
    public ResponseEntity<String> testSecuredEndpoint() {
        return ResponseEntity.ok("This is a SECURED test endpoint from auth-service.");
    }

    @Operation(summary = "Test public endpoint", description = "Test endpoint that does not require authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Public access granted",
                    content = @Content(mediaType = "text/plain"))
    })
    // Example of a public test endpoint (already configured in SecurityConfig)
    @GetMapping("/test-public")
    public ResponseEntity<String> testPublicEndpoint() {
        return ResponseEntity.ok("This is a PUBLIC test endpoint from auth-service.");
    }


    // Potential future admin-only endpoints (would require @PreAuthorize("hasRole('ADMIN')") or similar)
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "User not authenticated (if endpoint is secured and no/invalid token)",
                    content = @Content)
    })
    // @SecurityRequirement(name = "bearerAuth") // Add if this endpoint requires authentication
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getUserById(@PathVariable UUID id) {
        logger.info("Received request to get user by ID: {}", id);
        try {
            UsuarioResponse usuarioResponse = usuarioService.findUserById(id);
            logger.info("Successfully retrieved user with ID: {}", id);
            return ResponseEntity.ok(usuarioResponse);
        } catch (EntityNotFoundException e) {
            logger.warn("User not found for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving user with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @GetMapping
    // public ResponseEntity<List<UsuarioResponse>> getAllUsers() {
    //     // return ResponseEntity.ok(usuarioService.findAllUsers());
    //     return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    // }
}
