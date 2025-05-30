package com.simple.config.client;

import com.simple.config.client.dto.PerfilResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${services.auth.url}")
public interface AuthServiceClient {

    @GetMapping("/perfis/{id}")
    PerfilResponseDTO getPerfilById(@PathVariable("id") Integer id);
}
