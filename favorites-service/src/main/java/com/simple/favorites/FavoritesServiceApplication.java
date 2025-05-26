package com.simple.favorites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // Enable Feign Clients for inter-service communication
public class FavoritesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FavoritesServiceApplication.class, args);
    }

}
