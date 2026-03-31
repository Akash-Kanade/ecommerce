package com.ecommerce.userservice.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtility {


    private final long EXPIRATION;

    private final String SECRET;

    JwtUtility( @Value("${jwt.expiration}") long expiration,
                @Value("${jwt.secret}") String secret)
    {
        this.EXPIRATION = expiration;
        this.SECRET = secret;
    }

    public String generateToken(String username)
    {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails)
    {
        try{
            return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
        }
        catch(Exception e)
        {
            return false;
        }

    }

    public  String extractUsername(String token)
    {
        return verifySignatureAndExtractClaims(token).getSubject();
    }

    public Date getExpiration(String token)
    {
        return verifySignatureAndExtractClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token)
    {
        return getExpiration(token).before(new Date());
    }

    private Claims verifySignatureAndExtractClaims(String token)
    {
        return Jwts.parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignedKey()
    {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}
