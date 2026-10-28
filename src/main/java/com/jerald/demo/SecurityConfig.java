package com.jerald.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless REST API: no CSRF protection needed
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public: homepage, static files, errors, health, and login itself
                .requestMatchers("/", "/index.html", "/error", "/auth/login",
                                 "/actuator/health", "/actuator/health/**").permitAll()
                // Public: reading student records
                .requestMatchers(HttpMethod.GET, "/students", "/students/**").permitAll()
                // Everything else needs a valid token (or Basic login)
                .anyRequest().authenticated())
            // Check the Bearer token on every request, before the default login check
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            // Basic login still works, so existing curl tests and the current frontend keep working
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
