package org.example.config;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.jsonwebtoken.JwtException;
import org.jetbrains.annotations.NotNull;

public class JwtMiddleware implements Handler {

    private final TokenManager tokenManager;

    public JwtMiddleware(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String authHeader = ctx.header("Authorization");
        String userIdHeader = ctx.header("User-Id");

        if (authHeader == null || !authHeader.startsWith("Bearer ") || userIdHeader == null) {
            ctx.status(401).result("Token de autorización o User-Id no proporcionado.");
            return;
        }

        String token = authHeader.substring(7);

        try {
            tokenManager.validateToken(token, userIdHeader);
        } catch (JwtException e) {
            ctx.status(401).result("Token inválido o expirado: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error interno al validar el token.");
        }
    }
}