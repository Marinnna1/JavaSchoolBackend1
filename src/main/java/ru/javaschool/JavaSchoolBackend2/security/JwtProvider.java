package ru.javaschool.JavaSchoolBackend2.security;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("TopSecretValue")
    private String jwtSecret;

    @Value("3600000")
    private long tokenDurabilityInMilliseconds = 3600000;

    public String generateToken(String login) {
        //set token expiration time
        Date now = new Date();
        Date tokenExpirationTime = new Date(now.getTime() + tokenDurabilityInMilliseconds);
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(tokenExpirationTime)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            System.out.println("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            System.out.println("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            System.out.println("Malformed jwt: corrupted token");
        } catch (Exception e) {
            System.out.println("invalid token");
        }
        return false;
    }

    String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }


}
