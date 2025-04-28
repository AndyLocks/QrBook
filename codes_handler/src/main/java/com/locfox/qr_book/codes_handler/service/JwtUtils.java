package com.locfox.qr_book.codes_handler.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;

/// Service for working with JWT tokens
@Service
public class JwtUtils {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public JwtUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /// Checks if the token is valid: checks the signature as well as the lifetime.
    ///
    /// @param token can be null
    /// @return true if token is valid
    public boolean isTokenValid(String token) {
        if (token == null || token.isEmpty()) return false;

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

    /// Checks if the `token` is in a black list.
    ///
    /// @param token can be null
    /// @return true if the `token` is in a black list
    public boolean isInBlackList(String token) {
        return stringRedisTemplate.hasKey(token);
    }

}
