package com.paymybuddy.paymybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration de sécurité de l'application.
 * <p>
 * Le projet est actuellement en mode prototype: toutes les routes sont accessibles
 * et la protection CSRF est désactivée pour simplifier les formulaires.
 */
@Configuration
public class SecurityConfig {

    /**
     * Déclare la chaîne de filtres Spring Security.
     *
     * @param http objet de configuration HTTP.
     * @return la chaîne de filtres à appliquer.
     * @throws Exception en cas d'erreur de configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    /**
     * Encodeur de mots de passe basé sur BCrypt.
     *
     * @return l'encodeur utilisé par les services.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
