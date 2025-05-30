package com.simple.config.repository;

import com.simple.config.domain.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoteRepository extends JpaRepository<Lote, UUID>, JpaSpecificationExecutor<Lote> {
    Optional<Lote> findByCodigo(String codigo);
    // JpaSpecificationExecutor can be used for dynamic filtering if needed later
    // e.g. findAll(Specification<Lote> spec, Pageable pageable);
}
