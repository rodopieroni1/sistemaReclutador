package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.dto.PerfilDTO;
import com.sistemaReclutador.sistemaReclutador.entities.PasswordResetToken;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.exceptions.ResourceNotFoundException;
import com.sistemaReclutador.sistemaReclutador.repositories.PasswordResetTokenRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.EmailService;
import com.sistemaReclutador.sistemaReclutador.services.PerfilService;
import com.sistemaReclutador.sistemaReclutador.strategies.FileStorageStrategy;
import com.sistemaReclutador.sistemaReclutador.strategies.PerfilStrategy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {

	private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageStrategy fileStorageService;
    private final List<PerfilStrategy> validationStrategies; 
	
	// cambiar uando se haga el desliegue
	@Value("${app.api.front}")
	private String apiFront;

	@Bean(name = "customPasswordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public ResponseEntity<String> guardarPerfil(String nombre, String dni, String direccion, String email, 
	                                           String clave, String password, MultipartFile foto, MultipartFile uploadcv) {
	    
	    PerfilDTO dto = new PerfilDTO(0, nombre, dni, direccion, email, clave);
	    validarDatosPerfil(dto);
	    Perfil perfil = new Perfil();
	    perfil.setNombre(nombre);
	    perfil.setDni(dni);
	    perfil.setDireccion(direccion);
	    perfil.setEmail(email);
	    perfil.setClave(clave);
	    perfil.setPassword(passwordEncoder.encode(password));

	    perfil.setFotoUrl(fileStorageService.storeFile(foto, "fotos"));
	    perfil.setDocumentoUrl(fileStorageService.storeFile(uploadcv, "documentos"));

	    perfilRepository.save(perfil);
	    
	    return ResponseEntity.ok("{\"message\":\"Perfil creado correctamente\"}");
	}

	@Override
	public ResponseEntity<String> actualizarPerfil(int id, String nombre, String dni, String direccion, 
	                                              String email, String clave, MultipartFile foto, MultipartFile uploadcv) {
	
	        Optional<Perfil> perfilOpt = perfilRepository.findById(id);
	        if (perfilOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("{\"error\":\"Perfil no encontrado\"}");
	        }
	        PerfilDTO dto = new PerfilDTO(id, nombre, dni, direccion, email, clave);
	         validarDatosPerfil(dto);

	        Perfil perfil = perfilOpt.get();
	        perfil.setNombre(nombre);
	        perfil.setDni(dni);
	        perfil.setDireccion(direccion);
	        perfil.setEmail(email);
	        perfil.setClave(clave);

	        if (foto != null && !foto.isEmpty()) {
	            perfil.setFotoUrl(fileStorageService.storeFile(foto, "fotos"));
	        }

	        if (uploadcv != null && !uploadcv.isEmpty()) {
	            perfil.setDocumentoUrl(fileStorageService.storeFile(uploadcv, "documentos"));
	        }

	        perfilRepository.save(perfil);
	        return ResponseEntity.ok("{\"message\":\"Perfil actualizado correctamente\"}");
	}

	private void validarDatosPerfil(PerfilDTO dto) {
	    for (PerfilStrategy strategy : validationStrategies) {
	        Optional<String> error = strategy.validar(dto, perfilRepository);
	        if (error.isPresent()) {
	            throw new IllegalArgumentException(error.get());
	        }
	    }
	}
	
	
	@Override
	public ResponseEntity<?> eliminarPerfil(int id) {
		perfilRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<?> loginUsuario(LoginRequest credential) {
		Optional<Perfil> perfil = perfilRepository.findByClave(credential.getClave());
		if (perfil.isPresent() && passwordEncoder().matches(credential.getPassword(), perfil.get().getPassword())) {
			if (perfil.get().getSessionId() != null && !perfil.get().getSessionId().isBlank()) {
				LocalDateTime ultimaActividad = perfil.get().getFechaUltimaActividad();
				if (ultimaActividad != null) {
					Duration tiempoSinActividad = Duration.between(ultimaActividad, LocalDateTime.now());
					// Si hace menos de 5 minutos que hubo actividad,
					// no permitimos un nuevo login
					if (tiempoSinActividad.toMinutes() < 1) {
						long minutosRestantes = 1 - tiempoSinActividad.toMinutes();
						return ResponseEntity.status(HttpStatus.CONFLICT)
								.body(Map.of("error", "Ya existe una sesión activa para este usuario, o Espere 1 minuto, si no CERRO CORRECTAMENTE la sesion anterior",
										"minutosRestantes", minutosRestantes));
					}
				}
				// La sesión anterior expiró, se reemplaza por una nueva
				perfil.get().setSessionId(null);
			}
			// Generar un identificador único de sesión
			String sessionId = UUID.randomUUID().toString();
			perfil.get().setSessionId(sessionId);
			perfil.get().setFechaUltimaActividad(LocalDateTime.now());
			perfilRepository.save(perfil.get());
			String token = JwtUtil.generateToken(perfil.get().getClave(), sessionId);
			return ResponseEntity.ok().body(Map.of("token", token));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
	}

	@Override
	public ResponseEntity<?> olvideContraseña(Map<String, String> body) {
		String clave = body.get("clave");
		String email = body.get("email");
		Optional<Perfil> perfilOpt = perfilRepository.findByEmail(clave, email);
		try {
			if (perfilOpt.isPresent()) {
				Perfil perfil = perfilOpt.get();
				PasswordResetToken tokenExistente =  tokenRepository.findByPerfil(perfil).orElse(null);
				if (tokenExistente != null) {
				    tokenExistente.setToken(UUID.randomUUID().toString());
				    tokenExistente.setExpiryDate(LocalDateTime.now().plusHours(1));
				    tokenRepository.save(tokenExistente);
				    String resetLink = apiFront + "/reset-password?token=" + tokenExistente.getToken();
				    emailService.send(perfil.getEmail(), "Recuperación de contraseña",
							"ATENCION!, si usted no pidio un reseteo de contraseña, desestime este mail. /n Hacé clic en el siguiente enlace para restablecer tu contraseña: "
									+ resetLink);
					return ResponseEntity.ok(Map.of("message", "Se envió el enlace al mail ingresado"));
				} else {
				    PasswordResetToken token = new PasswordResetToken();
				    token.setPerfil(perfil);
				    token.setToken(UUID.randomUUID().toString());
				    token.setExpiryDate(LocalDateTime.now().plusHours(1));
				    tokenRepository.save(token);
				    String resetLink = apiFront + "/reset-password?token=" + token.getToken();
				    emailService.send(perfil.getEmail(), "Recuperación de contraseña",
							"ATENCION!, si usted no pidio un reseteo de contraseña, desestime este mail. /n Hacé clic en el siguiente enlace para restablecer tu contraseña: "
									+ resetLink);
					return ResponseEntity.ok(Map.of("message", "Se envió el enlace al mail ingresado"));
				}
				
				
				
			} else {
				return ResponseEntity.ok(Map.of("message", "Esta clave no se corresponde al mail ingersado"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(Map.of("message", "Ocurrio un error inespeado"));
		}
	}

	@Override
	public ResponseEntity<?> resetearContraseña(Map<String, String> body) {
		String token = body.get("token");
		String newPassword = body.get("newPassword");
		Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
		if (tokenOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Token inválido"));
		}
		PasswordResetToken resetToken = tokenOpt.get();
		if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Token expirado"));
		}

		Perfil perfil = resetToken.getPerfil();
		perfil.setPassword(passwordEncoder().encode(newPassword));
		perfilRepository.save(perfil);
		tokenRepository.delete(resetToken);

		return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));

	}

	@Override
	public ResponseEntity<Map<String, Boolean>> verificarEmailYDni(Map<String, String> datos) {
		String email = datos.get("email");
		String dni = datos.get("dni");
		boolean emailExists = perfilRepository.existsByEmail(email);
		boolean dniExists = perfilRepository.existsByDni(dni);
		Map<String, Boolean> response = Map.of("emailExists", emailExists, "dniExists", dniExists);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Perfil> obtenerPerfilPorName(String name) {
		Perfil nameReturn = perfilRepository.findByName(name);
		if (nameReturn == null || nameReturn.equals(null)) {
			return null;
		}
		return ResponseEntity.ok(nameReturn);
	}

	@Override
	public Perfil findById(int id) {
	    return perfilRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Perfil con ID " + id + " no encontrado"));
	}

	@Override
	public ResponseEntity<List<Perfil>> listarPerfiles() {
		List<Perfil> perfiles = perfilRepository.findAll();
		return new ResponseEntity<>(perfiles, HttpStatus.OK);
	}

	@Override
	public void cerrarSesion(String clave) {
		Optional<Perfil> perfil = perfilRepository.findByClave(clave);
		if (perfil.isPresent()) {
			perfil.get().setSessionId(null);
			perfilRepository.save(perfil.get());
		}

	}

}
