package com.simple.auth.service;

import com.simple.auth.domain.entity.Utilizador;
import com.simple.auth.repository.UtilizadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService") // Explicitly naming the bean
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilizadorRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilizador usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // The Usuario entity already implements UserDetails, so it can be returned directly.
        // Ensure all necessary fields (authorities, password, username, account status) are correctly populated in Usuario.
        return usuario;
    }
}
