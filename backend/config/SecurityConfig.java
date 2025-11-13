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
                // CSRF deaktivieren (für APIs wichtig)
                .csrf(csrf -> csrf.disable())

                // Form-Login komplett abschalten
                .formLogin(form -> form.disable())

                // Basic Auth komplett abschalten
                .httpBasic(basic -> basic.disable())

                // Logout komplett abschalten (sonst bleibt Login-Filter aktiv)
                .logout(logout -> logout.disable())

                // Autorisierung – alles erlauben
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
