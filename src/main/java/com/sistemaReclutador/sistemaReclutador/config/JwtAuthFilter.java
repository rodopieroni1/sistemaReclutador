package com.sistemaReclutador.sistemaReclutador.config;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PerfilRepository perfilRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, java.io.IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            // 1. Primero validamos el JWT
            if (!jwtUtil.validarToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 2. Recién ahora extraemos el username
            String username = jwtUtil.extraerUsername(token);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // 3. Buscamos el perfil
                Optional<Perfil> perfil = perfilRepository.findByClave(username);

                if (perfil.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // 4. Extraemos y comprobamos el sessionId
                String sessionIdToken = jwtUtil.extraerSessionId(token);

                if (sessionIdToken == null ||
                        !sessionIdToken.equals(perfil.get().getSessionId())) {

                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // 5. Actualizamos la última actividad
                perfil.get().setFechaUltimaActividad(LocalDateTime.now());
                perfilRepository.save(perfil.get());

                // 6. Autenticamos al usuario
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                java.util.Collections.emptyList()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {

        String path = request.getRequestURI();

        return path.startsWith("/uploads/");
    }
}