package com.finance.dashboard.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int expiration;

    SecretKey getSigningKey(){
        byte[] decodedSecret = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedSecret);

    }

    String generateJWTToken(String email){
        SecretKey key = getSigningKey();
        return Jwts.builder().subject(email).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expiration)).signWith(key).compact();
    }

    private Claims extractAllClaims(String token){
        SecretKey key = getSigningKey();
        try{
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    String extractEmail(String token){
        Claims allClaims = extractAllClaims(token);
        return allClaims.getSubject();
    }

    Boolean isTokenExpired(String token){
        Claims allClaims = extractAllClaims(token);
        Date expirationDate = allClaims.getExpiration();
        return expirationDate.before(new Date()); //if the expirationDate is before the current date it is expired
    }

    Boolean validateToken(String token, String email){
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }
}
