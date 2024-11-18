package com.example.proydbp.config;
import com.auth0.jwt.JWT;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.proydbp.exception.InvalidTokenException;
import com.example.proydbp.user.domain.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service

public class JwtService {

    @Value("sdfi0olkftdnrbftyui0ifdbzdfvxggui9poymftdnrgbgyuhoiuyfntbdz")
    private String secret;

    final private UserService userService;

    JwtService(UserService userService) {
        this.userService = userService;
    }


    public String extractUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    public String generateToken(UserDetails data){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * 60 * 10);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(data.getUsername())
                .withClaim("role", data.getAuthorities().toArray()[0].toString())
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    public void validateToken(String token, String userEmail) throws InvalidTokenException {

        try {
            JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("El Token es inválido o ha expirado");
        }

        // Si el token es válido, se obtiene los detalles del usuario
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

        // Se establece el contexto de seguridad con los detalles del usuario y el token
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }
}
