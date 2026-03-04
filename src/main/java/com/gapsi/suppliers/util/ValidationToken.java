package com.gapsi.suppliers.util;

import com.gapsi.suppliers.config.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ValidationToken {

    private final JwtService jwtService;

    public boolean validationToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        return jwtService.validateToken(token);
    }

    public Long extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);

        if (!jwtService.validateToken(token)) {
            return null;
        }

        return jwtService.extractUserId(token);
    }
}
