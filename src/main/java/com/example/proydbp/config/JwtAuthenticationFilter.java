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

        // Excluir rutas específicas del filtro
        if (request.getRequestURI().equals("/auth/login") || request.getRequestURI().equals("/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Falta el Token o es inválido");
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                jwtService.validateToken(jwt, userEmail);
            } catch (InvalidTokenException ex) {
                throw ex;
            }
        }

        filterChain.doFilter(request, response);
    }

}
