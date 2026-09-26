package com.sistemaReclutador.sistemaReclutador.config;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final PerfilRepository perfilRepository;
	private final UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, java.io.IOException {

		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			// 1. Primero validamos el JWT
			if (!jwtUtil.validarToken(token)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			// 2. Extraemos username y tipo
			String username = jwtUtil.extraerUsername(token);
			String tipo = jwtUtil.extraerTipo(token);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// 3. Si es un Usuario (reclutador/administrador)
				if ("USUARIO".equals(tipo)) {
					Optional<Usuario> usuario = usuarioRepository.findByClave(username);
					if (usuario.isEmpty()) {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						return;
					}
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,
							null, java.util.Collections.emptyList());

					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					// 4. Si es Perfil, mantenemos la lógica actual
					Optional<Perfil> perfil = perfilRepository.findByClave(username);
					if (perfil.isEmpty()) {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						return;
					}
					// 5. Extraemos y comprobamos el sessionId
					String sessionIdToken = jwtUtil.extraerSessionId(token);
					if (sessionIdToken == null || !sessionIdToken.equals(perfil.get().getSessionId())) {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						return;
					}
					// 6. Actualizamos la última actividad
					perfil.get().setFechaUltimaActividad(LocalDateTime.now());
					perfilRepository.save(perfil.get());
					// 7. Autenticamos al postulante
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