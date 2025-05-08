package com.municipio.simple.repository;

import com.municipio.simple.entity.CategoriaServico;
import com.municipio.simple.entity.TipoServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoServicoRepository extends JpaRepository<TipoServico, Integer> {
    List<TipoServico> findByAtivoTrueOrderByNome();
    List<TipoServico> findByCategoriaAndAtivoTrue(CategoriaServico categoria);
    Optional<TipoServico> findByCodigo(String codigo);
}
