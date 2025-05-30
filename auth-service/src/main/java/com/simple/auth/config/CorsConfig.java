package com.simple.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Configuração de CORS para permitir requisições de diferentes origens
 */
// @Configuration - Disabled to prevent CORS conflicts with API Gateway
public class CorsConfig {

    // @Bean - CORS is now handled by API Gateway only
    // public CorsFilter corsFilter() {
    //     CorsConfiguration corsConfiguration = new CorsConfiguration();
    //     corsConfiguration.setAllowCredentials(true);
    //     corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://simple-app.com"));
    //     corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
    //             "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
    //             "Access-Control-Request-Method", "Access-Control-Request-Headers"));
    //     corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
    //             "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
    //     corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    //     
    //     UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    //     urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
    //     
    //     return new CorsFilter(urlBasedCorsConfigurationSource);
    // }
}
