package com.municipio.simple.service;

import com.municipio.simple.dto.AuthRequest;
import com.municipio.simple.dto.AuthResponse;
import com.municipio.simple.dto.UsuarioRequest;
import com.municipio.simple.dto.UsuarioResponse;
import com.municipio.simple.entity.Perfil;
import com.municipio.simple.entity.Usuario;
import com.municipio.simple.repository.PerfilRepository;
import com.municipio.simple.repository.UsuarioRepository;
import com.municipio.simple.security.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );
        
        var user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow();
        
        user.setUltimoAcesso(LocalDateTime.now());
        usuarioRepository.save(user);
        
        var jwtToken = jwtTokenProvider.generateToken(user);
        var refreshToken = jwtTokenProvider.generateRefreshToken(user);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UsuarioResponse register(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Perfil perfil = perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));

        var user = new Usuario();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());
        user.setSenha(passwordEncoder.encode(request.getSenha()));
        user.setPerfil(perfil);
        user.setAtivo(true);

        usuarioRepository.save(user);

        return UsuarioResponse.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .perfil(user.getPerfil().getNome())
                .ativo(user.isAtivo())
                .ultimoAcesso(user.getUltimoAcesso())
                .criadoEm(user.getCriadoEm())
                .build();
    }
}
