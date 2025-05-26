package com.simple.config.repository;

import com.simple.config.domain.entity.CategoriaServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaServicoRepository extends JpaRepository<CategoriaServico, Integer> {
    List<CategoriaServico> findByAtivoTrueOrderByOrdem();
    Optional<CategoriaServico> findByNome(String nome); // Added for checking duplicates by name
}
