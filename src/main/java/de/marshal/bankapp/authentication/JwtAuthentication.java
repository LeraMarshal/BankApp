package de.marshal.bankapp.authentication;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class JwtAuthentication implements Authentication {
    public static JwtAuthentication from(Claims claims) {
        final JwtAuthentication jwtAuthentication = new JwtAuthentication();

        jwtAuthentication.setName(claims.getSubject());
        jwtAuthentication.setAuthenticated(true);

        return jwtAuthentication;
    }

    private boolean authenticated;
    private String name;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return name;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return name;
    }
}
