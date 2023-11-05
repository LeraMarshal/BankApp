package de.marshal.bankapp.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtService {
    private final SecretKey secret;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public boolean validateToken(@NonNull String token) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException eje) {
            log.warn("Token expired, exceptionMsg=[{}]", eje.getMessage());
        } catch (UnsupportedJwtException uje) {
            log.warn("Unsupported jwt, exceptionMsg=[{}]", uje.getMessage());
        } catch (MalformedJwtException mje) {
            log.warn("Malformed jwt, exceptionMsg=[{}]", mje.getMessage());
        } catch (SignatureException sige) {
            log.warn("Invalid signature, exceptionMsg=[{}]", sige.getMessage());
        } catch (Throwable t) {
            log.warn("Invalid token, exceptionMsg=[{}]", t.getMessage());
        }

        return false;
    }

    public Claims getClaims(@NonNull String token) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
