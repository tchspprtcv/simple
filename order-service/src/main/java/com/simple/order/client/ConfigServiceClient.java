package com.simple.order.client;

import com.simple.order.client.dto.EtapaProcessoResponse;
import com.simple.order.client.dto.TipoServicoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "config-service", url = "${services.config.url}")
public interface ConfigServiceClient {

    @GetMapping("/api/tipos-servico/{id}")
    TipoServicoResponse getTipoServicoById(@PathVariable("id") Integer id);

    @GetMapping("/api/etapas-processo/{id}")
    EtapaProcessoResponse getEtapaProcessoById(@PathVariable("id") Integer id);
}
