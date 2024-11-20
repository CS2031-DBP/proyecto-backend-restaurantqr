package com.example.proydbp.config;

import com.example.proydbp.exception.InvalidTokenException;
import com.example.proydbp.exception.UnauthorizeOperationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    final private JwtService jwtService;

    JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt;
        String userEmail;

        // Lista de rutas excluidas
        List<String> excludedRoutes = List.of(
                "/auth/login",
                "/auth/register",
                "/auth/register/admin",
                "/auth/register/chef",
                "/auth/register/mesero",
                "/auth/register/repartidor"
        );

        // Excluir si la ruta está en la lista
        if (excludedRoutes.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Falta el Token o es inválido");
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                jwtService.validateToken(jwt, userEmail);
            } catch (InvalidTokenException ex) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "El token es inválido o ha expirado");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
