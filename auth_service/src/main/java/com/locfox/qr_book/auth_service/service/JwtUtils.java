package com.locfox.qr_book.auth_service.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

/// Service for working with JWT tokens
@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;
    private RedisTemplate<String, String> tokenTemplate;

    @Autowired
    public JwtUtils(RedisTemplate<String, String> tokenTemplate) {
        this.tokenTemplate = tokenTemplate;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /// Returns a new JWT token based on the account id
    ///
    /// @param id cannot be `null`
    /// @return generated token
    public String generateJwtToken(Long id) {
        return Jwts.builder()
                .subject(Assert.notNull(id, "Id cannot be null").toString())
                .expiration(new Date(System.currentTimeMillis() + 3600_000))
                .issuedAt(new Date())
                .signWith(this.getSigningKey())
                .compact();
    }

    /// Checks if the token is valid: checks the signature as well as the lifetime.
    ///
    /// @param token can be null
    /// @return true if token is valid
    public boolean verifyToken(String token) {
        var jwtParser = Jwts.parser()
                .verifyWith((SecretKey) this.getSigningKey())
                .build();

        try {
            jwtParser.parse(token);
        } catch (JwtException e) {
            return false;
        }

        return true;
    }

    /// Returns the JWT `sub` value or `null` if not present.
    ///
    /// @param token cant be null
    /// @return the JWT `sub` value or `null` if not present.
    /// @throws IllegalArgumentException if token is null
    public String getSubject(String token) {
        return Jwts.parser()
                .decryptWith((SecretKey) getSigningKey())
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(Assert.hasText(token, "Token cant be null"))
                .getPayload()
                .getSubject();
    }

    /// Adds the `token` to the token blacklist
    ///
    /// @param token cannot be `null`
    /// @throws IllegalArgumentException if `token` is `null`
    public void addToBlackList(String token) {
        tokenTemplate.opsForValue().set(Assert.notNull(token, "Token cannot be null"), "", Duration.ofHours(1));
    }

    /// Checks if the `token` is in a black list.
    ///
    /// @param token can be `null`
    /// @return true if the `token` is in a black list
    public boolean isInBlackList(String token) {
        return token != null && tokenTemplate.hasKey(token);
    }

}
