package com.roima.hrms.auth.jwt;
import com.roima.hrms.auth.model.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "RAVIvadher-vadherraviravivadherRAVIvadher-vadherraviravivadherRAVIvadher-vadherraviravivadher";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    //token generation
    public String generateToken(UserPrincipal userPrincipal) {
        return  Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("role",userPrincipal.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
}
