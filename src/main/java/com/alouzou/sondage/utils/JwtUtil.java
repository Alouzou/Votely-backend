package com.alouzou.sondage.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "CeciEstUneCleSecreteSuperLonguePourJWT2025!!";
    private static final long EXPIRATION_TIME = 86400000; // 1 jour

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

}
