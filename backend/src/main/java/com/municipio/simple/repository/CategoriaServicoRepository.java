package com.municipio.simple.repository;

import com.municipio.simple.entity.CategoriaServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaServicoRepository extends JpaRepository<CategoriaServico, Integer> {
    List<CategoriaServico> findByAtivoTrueOrderByOrdem();
}
