package com.simple.order.repository;

import com.simple.order.domain.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    Optional<Pedido> findByCodigoAcompanhamento(String codigoAcompanhamento);
    
    Page<Pedido> findByCidadaoId(UUID cidadaoId, Pageable pageable);
    
    Page<Pedido> findByStatusId(Integer statusId, Pageable pageable); // Changed from StatusPedido object to statusId
    
    Page<Pedido> findByTipoServicoId(UUID tipoServicoId, Pageable pageable); // Changed from TipoServico object to tipoServicoId
    
    List<Pedido> findTop10ByCidadaoIdOrderByCriadoEmDesc(UUID cidadaoId);
    
    Page<Pedido> findByUsuarioCriacaoId(UUID usuarioCriacaoId, Pageable pageable);

    // Consider adding methods for filtering by usuarioResponsavelId if needed
    // Page<Pedido> findByUsuarioResponsavelId(UUID usuarioResponsavelId, Pageable pageable);
}
