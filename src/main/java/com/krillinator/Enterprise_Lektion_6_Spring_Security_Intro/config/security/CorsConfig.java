package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("http://localhost:3000"); // Frontend
        corsConfiguration.addAllowedMethod("*");    // GET POST PUT DELETE
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        corsConfiguration.setExposedHeaders(List.of("Set-Cookie"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/register", corsConfiguration); // Backend
        source.registerCorsConfiguration("/api/v1/who-am-i", corsConfiguration);
        source.registerCorsConfiguration("/bananas", corsConfiguration);

        return source;
    }

}
