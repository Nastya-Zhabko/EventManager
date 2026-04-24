package dev.nastyazhabko.eventnotificator.security;

import dev.nastyazhabko.eventcommon.security.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    private final SecretKey SECRET_KEY;
    private final long EXPIRATION_TIME;

    public JwtUtil(@Value("${jwt.secret-key}") String keyString,
                   @Value("${jwt.lifetime}") long expirationTime) {
        SECRET_KEY = Keys.hmacShaKeyFor(keyString.getBytes());
        EXPIRATION_TIME = expirationTime;
    }

    public Integer getIdFromToken(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody()
                .get("userId", Integer.class);
    }

    public UserRole getRolesFromToken(String jwt) {
        return UserRole.valueOf(Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody()
                .get("role", String.class));
    }
}
