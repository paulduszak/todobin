package in.todob.todobin.authentication.utils;

import in.todob.todobin.authentication.model.UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.prefix")
    private String PREFIX;

    @Value("${jwt.key}")
    private String SIGNING_KEY;

    @Value("${jwt.expiration}")
    private int EXPIRATION_TIME;

    private final SignatureAlgorithm SIGNING_ALG = SignatureAlgorithm.HS512;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                   .setSubject(userPrincipal.getUsername())
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                   .signWith(SIGNING_ALG, SIGNING_KEY)
                   .compact();
    }

    public String retrieveUsername(String token) {

        if (StringUtils.hasText(token))
            return Jwts.parser()
                       .setSigningKey(SIGNING_KEY)
                       .parseClaimsJws(token.replace(PREFIX, ""))
                       .getBody()
                       .getSubject();

        return null;

    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
