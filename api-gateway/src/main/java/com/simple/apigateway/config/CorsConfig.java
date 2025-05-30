package com.simple.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Allow specific origins
        corsConfig.setAllowedOrigins(Arrays.asList(
            "http://localhost:9000",
            "http://127.0.0.1:9000",
            "http://simple-frontend:9000"
        ));
        
        // Allow all HTTP methods
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Allow all headers
        corsConfig.setAllowedHeaders(Arrays.asList(
            "Authorization", 
            "Content-Type", 
            "Accept", 
            "Origin", 
            "X-Requested-With", 
            "Access-Control-Request-Method", 
            "Access-Control-Request-Headers",
            "X-User-ID"
        ));
        
        // Expose headers
        corsConfig.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));
        
        // Allow credentials
        corsConfig.setAllowCredentials(true);
        
        // Cache preflight response for 1 hour
        corsConfig.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}