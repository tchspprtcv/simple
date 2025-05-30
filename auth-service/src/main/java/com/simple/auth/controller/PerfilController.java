package com.simple.auth.controller;

import com.simple.auth.dto.PerfilResponse;
import com.simple.auth.service.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/perfis") // Changed from /api/perfis to /perfis
@RequiredArgsConstructor
@Tag(name = "Perfil Management", description = "Endpoints for managing user profiles (roles/permissions)")
public class PerfilController {

    private static final Logger logger = LoggerFactory.getLogger(PerfilController.class);
    private final PerfilService perfilService;

    @Operation(summary = "Get perfil by ID", description = "Retrieve a specific perfil by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PerfilResponse.class))),
            @ApiResponse(responseCode = "404", description = "Perfil not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "User not authenticated (if endpoint is secured and no/invalid token)",
                    content = @Content)
    })
    // @SecurityRequirement(name = "bearerAuth") // Add if this endpoint requires authentication
    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponse> getPerfilById(@PathVariable Integer id) {
        logger.info("Received request to get perfil by ID: {}", id);
        try {
            PerfilResponse perfilResponse = perfilService.findPerfilById(id);
            logger.info("Successfully retrieved perfil with ID: {}", id);
            return ResponseEntity.ok(perfilResponse);
        } catch (EntityNotFoundException e) {
            logger.warn("Perfil not found for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving perfil with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
