package com.simple.config.repository;

import com.simple.config.domain.entity.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusPedidoRepository extends JpaRepository<StatusPedido, Integer> {
    Optional<StatusPedido> findByCodigo(String codigo);
    List<StatusPedido> findAllByOrderByOrdemAsc();
}
