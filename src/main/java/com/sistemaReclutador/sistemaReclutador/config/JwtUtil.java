package com.sistemaReclutador.sistemaReclutador.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
	private static final long EXPIRATION_TIME = 5 * 60 * 1000;
	private static final String SECRET = "clave-secreta-super-segura-clave-muy-larga";
	private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());


	public static String generateToken(String username, String sessionId) {
	    return Jwts.builder()
	            .setSubject(username)
	            .claim("sessionId", sessionId)
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	            .signWith(SECRET_KEY)
	            .compact();
	}
	
	public static String generateTokenUsuario(String username) {
	    return Jwts.builder()
	            .setSubject(username)
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	            .signWith(SECRET_KEY)
	            .compact();
	}

	public String extraerUsername(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(SECRET_KEY)
	            .build()
	            .parseClaimsJws(token)
	            .getBody()
	            .getSubject();
	}
	


	public static boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public boolean validarToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
			return !claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public String extraerSessionId(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(SECRET_KEY)
	            .build()
	            .parseClaimsJws(token)
	            .getBody()
	            .get("sessionId", String.class);
	}

}
