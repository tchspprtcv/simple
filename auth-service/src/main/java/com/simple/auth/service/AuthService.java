package com.simple.auth.service;

import com.simple.auth.dto.AuthRequest;
import com.simple.auth.dto.AuthResponse;
import com.simple.auth.dto.UsuarioRequest;
import com.simple.auth.dto.UsuarioResponse;
import com.simple.auth.domain.entity.Perfil;
import com.simple.auth.domain.entity.Usuario;
import com.simple.auth.repository.PerfilRepository;
import com.simple.auth.repository.UsuarioRepository;
import com.simple.auth.security.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );
        
        var user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o email: " + request.getEmail()));
        
        user.setUltimoAcesso(LocalDateTime.now());
        usuarioRepository.save(user);
        
        var jwtToken = jwtTokenProvider.generateToken(user);
        // Assuming refresh token generation is desired as per the original monolith's JwtTokenProvider
        var refreshToken = jwtTokenProvider.generateRefreshToken(user); 
        
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public UsuarioResponse register(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + request.getEmail());
        }

        Perfil perfil = perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado com ID: " + request.getPerfilId()));

        var user = new Usuario();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());
        user.setSenha(passwordEncoder.encode(request.getSenha()));
        user.setPerfil(perfil);
        user.setAtivo(true); // Default to active on registration
        // criadoEm and atualizadoEm will be set by @CreationTimestamp / @UpdateTimestamp

        Usuario savedUser = usuarioRepository.save(user);

        return UsuarioResponse.builder()
                .id(savedUser.getId())
                .nome(savedUser.getNome())
                .email(savedUser.getEmail())
                .perfil(savedUser.getPerfil().getNome()) // Assuming Perfil entity has getNome()
                .ativo(savedUser.isAtivo())
                .ultimoAcesso(savedUser.getUltimoAcesso())
                .criadoEm(savedUser.getCriadoEm())
                .build();
    }
}
