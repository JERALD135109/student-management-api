package com.jerald.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // REST API with HTTP Basic: CSRF protection is not needed here
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public: homepage, static files, error pages, health check
                .requestMatchers("/", "/index.html", "/error", "/actuator/health", "/actuator/health/**").permitAll()
                // Public: reading student records
                .requestMatchers(HttpMethod.GET, "/students", "/students/**").permitAll()
                // Everything else (POST, PUT, DELETE) needs a login
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
