package com.simple.auth.repository;

import com.simple.auth.domain.entity.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, UUID> {
    Optional<Utilizador> findByEmail(String email);
    boolean existsByEmail(String email);
}
