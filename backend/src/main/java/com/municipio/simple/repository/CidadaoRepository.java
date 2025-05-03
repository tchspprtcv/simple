package com.municipio.simple.repository;

import com.municipio.simple.entity.Cidadao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CidadaoRepository extends JpaRepository<Cidadao, UUID> {
    Optional<Cidadao> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
}
