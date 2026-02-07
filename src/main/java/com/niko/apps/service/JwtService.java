package com.niko.apps.service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;


@Component
public class JwtService {
	
	public static final String SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";
	
	
	// Create a new claims map and start token creation
	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, email);
	}
	
	
	// Create JWT token 
	private String createToken(Map<String, Object> claims, String email) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.signWith(getSignKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	
	
	// Take the bytes of the SECRET variable defined above and 
	// use built in method for generating a usable SECRET_KEY instance
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	// Extract email from the token
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	
	// Extract expiration from token 
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	
	// Extract the value of a individual (single) claim
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	
	// Get all the claims from the token
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	// Check if token is expired
	private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }
	
	

	// Check if token is valid
	public Boolean validateToken(String token, UserDetails userDetails) {
		
		// Extract email from token
		final String username = extractEmail(token);
		
		// Check extracted email from token with the user email in DB
		// Also check if the token is expired.
		return (username.equals(userDetails.getUsername())) && isTokenExpired(token);
	}
	
 }
