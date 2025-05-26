package com.simple.config.repository;

import com.simple.config.domain.entity.CategoriaServico;
import com.simple.config.domain.entity.TipoServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoServicoRepository extends JpaRepository<TipoServico, Integer> {
    List<TipoServico> findByAtivoTrueOrderByNome();
    List<TipoServico> findByCategoriaAndAtivoTrueOrderByNome(CategoriaServico categoria); // Added OrderByNome
    List<TipoServico> findByCategoria_IdAndAtivoTrueOrderByNome(Integer categoriaId); // Find by Categoria ID
    Optional<TipoServico> findByCodigo(String codigo);
    Optional<TipoServico> findByNome(String nome); // Added for checking duplicates by name
}
