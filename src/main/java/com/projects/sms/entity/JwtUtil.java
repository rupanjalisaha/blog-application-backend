package com.projects.sms.entity;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
    private String SECRET;

	private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
	
    public String generate(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                		.map(GrantedAuthority::getAuthority)
                		.toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1200000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
        		.setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValid(String token, UserDetails user) {
    	return getUsername(token).equals(user.getUsername()) && !isExpired(token);
    }
    boolean isExpired(String token) {
        Date expiry = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiry.before(new Date());
}
    
}