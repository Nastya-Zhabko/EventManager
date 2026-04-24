package dev.nastyazhabko.eventmanager.security.jwt;

import dev.nastyazhabko.eventcommon.security.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey SECRET_KEY;
    private final long EXPIRATION_TIME;

    public JwtUtil(@Value("${jwt.secret-key}") String keyString,
                   @Value("${jwt.lifetime}") long expirationTime) {
        SECRET_KEY = Keys.hmacShaKeyFor(keyString.getBytes());
        EXPIRATION_TIME = expirationTime;
    }

    public String generateToken(String login, Integer userId, UserRole role) {
        return Jwts.builder()
                .setSubject(login)
                .claim("userId", userId)
                .claim("role", role.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getLoginFromToken(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
