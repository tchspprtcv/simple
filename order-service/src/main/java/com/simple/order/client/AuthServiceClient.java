package com.simple.order.client;

import com.simple.order.client.dto.UsuarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "auth-service", url = "${services.auth.url}")
public interface AuthServiceClient {

    @GetMapping("/api/usuarios/{id}")
    UsuarioResponse getUsuarioById(@PathVariable("id") UUID id);
}
