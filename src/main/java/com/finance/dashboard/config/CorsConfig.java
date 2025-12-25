package com.finance.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        //allow credentials
        configuration.setAllowCredentials(true);

        //allow specific origins
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));

        //allow specific  methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        //allow specific headers
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

        configuration.setMaxAge(3600L); // 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Register the configuration for all paths
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
