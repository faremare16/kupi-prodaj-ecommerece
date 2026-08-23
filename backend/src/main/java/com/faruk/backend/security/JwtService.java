package com.faruk.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    // čita tajni kljuc iz aplication.propreties(koji ga vuce iz os)
    @Value("${jwt.secret}")
    private String secretKey;

    // kada tajni kljuc ističe
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // citanje iz tokena:
    // iz tokena izvlaci username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // univerzalna za izvlacenje bilo kojeg podatka
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // generisanje tokena:
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("roles", roles);

        return generateToken(claims, userDetails);

    }
    //glavna metoda za kreiranje jwt tokena
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // ko je vlasnik
                .setIssuedAt(new Date(System.currentTimeMillis())) // kada je izdat
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // kada ističe
                .signWith(getSignIngKey(), SignatureAlgorithm.HS256) // pecatiranje secret keyom
                .compact();
    }

    //validiranje tokena
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignIngKey())
                .build()
                .parseClaimsJws(token).getBody();

    }

    private Key getSignIngKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
