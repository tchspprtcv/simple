package com.simple.order.repository;

import com.simple.order.domain.entity.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusPedidoRepository extends JpaRepository<StatusPedido, Integer> { // Changed ID type from Long to Integer
    Optional<StatusPedido> findByCodigo(String codigo);
}
