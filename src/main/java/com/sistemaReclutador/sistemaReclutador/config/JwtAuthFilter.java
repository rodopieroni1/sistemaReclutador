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
import io.jsonwebtoken.io.IOException;
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
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException, java.io.IOException {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			String username = jwtUtil.extraerUsername(token);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				if (jwtUtil.validarToken(token)) {
					Optional<Perfil> perfil = perfilRepository.findByClave(username);
					if (perfil.isEmpty()) {
					    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					    return;
					}
					String sessionIdToken = jwtUtil.extraerSessionId(token);
					if (!sessionIdToken.equals(perfil.get().getSessionId())) {
					    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					    return;
					}
					perfil.get().setFechaUltimaActividad(LocalDateTime.now());
					perfilRepository.save(perfil.get());
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,
							null, java.util.Collections.emptyList());
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
		chain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    String path = request.getRequestURI();
	    return path.startsWith("/uploads/");
	}

}
