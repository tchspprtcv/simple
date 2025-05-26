package com.simple.favorites.repository;

import com.simple.favorites.domain.entity.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, UUID> {
    List<Favorito> findByUsuarioIdOrderByCriadoEmDesc(UUID usuarioId);
    Optional<Favorito> findByUsuarioIdAndTipoServicoId(UUID usuarioId, Integer tipoServicoId);
    void deleteByUsuarioIdAndTipoServicoId(UUID usuarioId, Integer tipoServicoId);
    boolean existsByUsuarioIdAndTipoServicoId(UUID usuarioId, Integer tipoServicoId);
}
