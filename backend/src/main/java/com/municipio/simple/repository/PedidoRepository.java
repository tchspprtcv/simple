package com.municipio.simple.repository;

import com.municipio.simple.entity.Cidadao;
import com.municipio.simple.entity.Pedido;
import com.municipio.simple.entity.StatusPedido;
import com.municipio.simple.entity.TipoServico;
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
    Page<Pedido> findByCidadao(Cidadao cidadao, Pageable pageable);
    Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);
    Page<Pedido> findByTipoServico(TipoServico tipoServico, Pageable pageable);
    List<Pedido> findTop10ByCidadaoOrderByCriadoEmDesc(Cidadao cidadao);
}
