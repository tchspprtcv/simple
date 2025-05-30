package com.municipio.simple.repository;

import com.municipio.simple.entity.Favorito;
import com.municipio.simple.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {
    List<Favorito> findByUsuarioOrderByOrdem(Usuario usuario);
    Optional<Favorito> findByUsuarioAndTipoServicoId(Usuario usuario, Integer tipoServicoId);
}