package com.simple.favorites.client;

import com.simple.favorites.client.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "auth-service", url = "${services.auth.url}")
public interface AuthServiceClient {

    @GetMapping("/users/{id}")
    UsuarioDTO getUsuarioById(@PathVariable("id") UUID id);
}
