package com.gapsi.suppliers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class AuthenticationManagerConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordConfig passwordEncoder;

    @Autowired
    public AuthenticationManagerConfig(UserDetailsService userDetailsService, PasswordConfig passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Configuración del AuthenticationManager sin el uso de HttpSecurity
        return authenticationConfiguration.getAuthenticationManager();
    }
}