package com.simple.favorites.client;

import com.simple.favorites.dto.TipoServicoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// The 'url' property can be omitted if service discovery (e.g., Eureka, Consul) is used
// and the 'name' corresponds to the registered service ID.
// For now, using direct URL from application.yml.
@FeignClient(name = "config-service", url = "${services.config.url}")
public interface ConfigServiceClient {

    // Path matches the @GetMapping in config-service's TipoServicoController
    @GetMapping("/tipos-servicos/{id}") 
    TipoServicoDTO getTipoServicoById(@PathVariable("id") Integer id); // TipoServico ID is Integer
}
