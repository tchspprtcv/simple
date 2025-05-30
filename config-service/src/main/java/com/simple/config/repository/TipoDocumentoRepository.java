package com.simple.config.repository;

import com.simple.config.domain.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {
    Optional<TipoDocumento> findByCodigo(String codigo);
    List<TipoDocumento> findByAtivo(boolean ativo);
    // Add other query methods if needed, e.g., findByObrigatorioAndAtivo, etc.
}
