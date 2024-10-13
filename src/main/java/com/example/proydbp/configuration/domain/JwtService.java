package com.example.proydbp.configuration.domain;
import com.auth0.jwt.JWT;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.proydbp.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("y8J#7xZ5v!3F%wP$A9sH@1tGzU*Qm2Lc")
    private String secret;

    private final UserService userService;

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


    public void validateToken(String token, String userEmail) throws AuthenticationException {
        JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                token,
                userDetails.getAuthorities()
        );
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }

}
