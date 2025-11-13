package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF deaktivieren (für APIs notwendig)
                .csrf(csrf -> csrf.disable())

                // Session-basierte/Form-Logins
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // Autorisierung festlegen
                .authorizeHttpRequests(auth -> auth
                        // Login-Route erlauben
                        .requestMatchers("/api/login").permitAll()
                        // Weitere API-Endpunkte habt: auch freigeben
                        .requestMatchers("/api/**").permitAll()
                        // alles andere auch erlauben
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
