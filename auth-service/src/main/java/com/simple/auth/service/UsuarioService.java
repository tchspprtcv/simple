package com.simple.auth.service;

import com.simple.auth.dto.UsuarioRequest; // Renamed from monolith's UsuarioRequest to avoid confusion if a different one is needed later
import com.simple.auth.dto.UsuarioResponse;
import com.simple.auth.domain.entity.Utilizador;
import com.simple.auth.repository.UtilizadorRepository;
// PerfilRepository might be needed if we allow profile updates through this service
// import com.simple.auth.repository.PerfilRepository; 
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UtilizadorRepository usuarioRepository;
    // private final PerfilRepository perfilRepository; // Uncomment if managing Perfil

    @Transactional(readOnly = true)
    public UsuarioResponse getCurrentAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof Utilizador) {
            username = ((Utilizador) principal).getUsername(); // UserDetails.getUsername()
        } else if (principal instanceof String) { // Fallback if principal is just the username string
            username = (String) principal;
        } else {
            // This case should ideally not be reached if authentication is correctly set up
            // with a UserDetails object.
            throw new IllegalStateException("Principal type not supported for fetching current user: " + principal.getClass().getName());
        }

        Utilizador user = usuarioRepository.findByEmail(username) // Assuming email is the username
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return mapToUsuarioResponse(user);
    }

    @Transactional
    public UsuarioResponse updateCurrentAuthenticatedUser(UsuarioRequest usuarioUpdateRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof Utilizador) {
            username = ((Utilizador) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new IllegalStateException("Principal type not supported for updating current user: " + principal.getClass().getName());
        }

        Utilizador user = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Update user fields from the request
        if (usuarioUpdateRequest.getNome() != null && !usuarioUpdateRequest.getNome().isBlank()) {
            user.setNome(usuarioUpdateRequest.getNome());
        }
        
        // Note: Email and Password updates should have dedicated, secure processes.
        // Perfil updates would typically be an admin function and might involve PerfilRepository.
        // For now, only 'nome' is updatable as per the DTO.

        Utilizador updatedUser = usuarioRepository.save(user);
        return mapToUsuarioResponse(updatedUser);
    }

    private UsuarioResponse mapToUsuarioResponse(Utilizador user) {
        return UsuarioResponse.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .perfil(user.getPerfil() != null ? user.getPerfil().getNome() : null)
                .ativo(user.isAtivo())
                .ultimoAcesso(user.getUltimoAcesso())
                .criadoEm(user.getCriadoEm())
                .build();
    }

    // Potential future methods:
    // - findUserById(UUID id)
    // - getAllUsers() (with pagination, for admin purposes)
    // - updateUserProfile(UUID userId, PerfilUpdateRequest perfilUpdateRequest) (admin)
    // - activateDeactivateUser(UUID userId, boolean status) (admin)
}
