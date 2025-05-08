package com.municipio.simple.service;

import com.municipio.simple.dto.UsuarioRequest;
import com.municipio.simple.dto.UsuarioResponse;
import com.municipio.simple.entity.Usuario;
import com.municipio.simple.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse getCurrentAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof Usuario) {
            username = ((Usuario) principal).getEmail();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new IllegalStateException("Principal type not supported: " + principal.getClass().getName());
        }

        Usuario user = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Map Usuario entity to UsuarioResponse DTO
        return UsuarioResponse.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .perfil(user.getPerfil() != null ? user.getPerfil().getNome() : null) // Handle potential null perfil
                .ativo(user.isAtivo())
                .ultimoAcesso(user.getUltimoAcesso())
                .criadoEm(user.getCriadoEm())
                .build();
    }

    // Add other user-related service methods here if needed

    public UsuarioResponse updateCurrentAuthenticatedUser(UsuarioRequest usuarioRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof Usuario) {
            username = ((Usuario) principal).getEmail();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new IllegalStateException("Principal type not supported: " + principal.getClass().getName());
        }

        Usuario user = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Update user fields
        if (usuarioRequest.getNome() != null && !usuarioRequest.getNome().isEmpty()) {
            user.setNome(usuarioRequest.getNome());
        }
        // Add other updatable fields here, e.g., telefone, if they exist in Usuario and UsuarioRequest
        // Note: Email and Senha updates usually have separate, more secure flows.
        // Perfil update is typically an admin-only function.

        Usuario updatedUser = usuarioRepository.save(user);

        return UsuarioResponse.builder()
                .id(updatedUser.getId())
                .nome(updatedUser.getNome())
                .email(updatedUser.getEmail())
                .perfil(updatedUser.getPerfil() != null ? updatedUser.getPerfil().getNome() : null)
                .ativo(updatedUser.isAtivo())
                .ultimoAcesso(updatedUser.getUltimoAcesso())
                .criadoEm(updatedUser.getCriadoEm())
                .build();
    }
}