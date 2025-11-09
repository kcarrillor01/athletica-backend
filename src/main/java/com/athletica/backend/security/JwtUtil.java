package com.athletica.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

  private final SecretKey key;
  private final long expirationMs;

  public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration-ms}") long expirationMs) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  public String generateToken(String subject, Map<String, Object> extraClaims) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(exp)
        .signWith(key)
        .compact();
  }

  public Claims validateAndGetClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}

