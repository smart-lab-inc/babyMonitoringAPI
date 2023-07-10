package com.smartlab.babymonitoringapi.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.Map;


public class JWTUtils {

    public static String generateToken(String email, Map<String, Object> payload, String jwtSecret, int expirationMinutes) {
        Date expirationTime = new Date(System.currentTimeMillis() + 1000L * 60 * expirationMinutes);

        return Jwts.builder()
                .setSubject(email)
                .addClaims(payload)
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }


    public static Boolean isValidateToken(String token, String secretKey) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    public static String getEmailFromJWT(String token, String secretKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes()).build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
