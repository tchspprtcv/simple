package com.simple.config.repository;

import com.simple.config.domain.entity.EtapaProcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtapaProcessoRepository extends JpaRepository<EtapaProcesso, Integer> {
    Optional<EtapaProcesso> findByCodigo(String codigo);
    List<EtapaProcesso> findByTipoServicoIdOrderByOrdemAsc(Integer tipoServicoId);
}
