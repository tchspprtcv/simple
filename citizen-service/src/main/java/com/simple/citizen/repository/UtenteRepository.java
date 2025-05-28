package com.simple.citizen.repository;

import com.simple.citizen.domain.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {
    Optional<Utente> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
    Optional<Utente> findByEmail(String email); // Added as email is unique
}
