package com.municipio.simple.repository;

import com.municipio.simple.entity.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusPedidoRepository extends JpaRepository<StatusPedido, Long> {
    Optional<StatusPedido> findByCodigo(String codigo);
}
