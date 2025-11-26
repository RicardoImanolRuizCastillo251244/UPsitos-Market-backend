package org.example.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class TokenManager {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private static final Duration EXPIRATION_TIME = Duration.ofHours(1);

    public String issueToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(EXPIRATION_TIME)))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Jws<Claims> validateToken(String token, String expectedUserId) throws JwtException {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
        if (!claims.getBody().getSubject().equals(expectedUserId)) {
            throw new JwtException("El User-Id no coincide con el token.");
        }
        return claims;
    }
}