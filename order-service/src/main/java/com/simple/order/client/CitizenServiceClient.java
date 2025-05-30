package com.simple.order.client;

import com.simple.order.client.dto.CidadaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "citizen-service", url = "${services.citizen.url}")
public interface CitizenServiceClient {

    @GetMapping("/api/cidadaos/{id}")
    CidadaoResponse getCidadaoById(@PathVariable("id") UUID id);
}
