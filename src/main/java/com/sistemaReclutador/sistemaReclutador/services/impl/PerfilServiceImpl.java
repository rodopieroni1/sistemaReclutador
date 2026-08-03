package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.entities.PasswordResetToken;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.repositories.PasswordResetTokenRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.EmailService;
import com.sistemaReclutador.sistemaReclutador.services.PerfilService;

@Configuration
public class PerfilServiceImpl implements PerfilService {

	@Autowired
	private PasswordResetTokenRepository tokenRepository;
	private final JwtUtil jwtUtil = new JwtUtil();
	@Autowired
	private EmailService emailService;
	@Autowired
	private PerfilRepository perfilRepository;
	// cambiar uando se haga el desliegue

	@Value("${app.base.url}")
	private String appBaseUrl;

	@Value("${app.upload.dir}")
	private String uploadDir;
	
	@Value("${app.api.front}")
	private String apiFront;

	@Bean(name = "customPasswordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public ResponseEntity<String> guardarPerfil(String nombre, String dni, String direccion, String email, String clave,
			String password, MultipartFile foto, MultipartFile uploadcv) {

		try {
			// Ejecutar validaciones (id = 0 significa creación)
			Optional<ResponseEntity<String>> errorValidacion = validarDatosPerfil(0, nombre, dni, direccion, email,
					clave);
			if (errorValidacion.isPresent()) {
				return errorValidacion.get();
			}

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String hashedPassword = encoder.encode(password);
			String baseDir = uploadDir.endsWith(File.separator) ? uploadDir : uploadDir + File.separator;
			String fotoDir = baseDir + "fotos" + File.separator;
			String cvDir = baseDir + "documentos" + File.separator;
			File directorioFoto = new File(fotoDir);
			if (!directorioFoto.exists()) {
				directorioFoto.mkdirs();
			}
			File directorioCV = new File(cvDir);
			if (!directorioCV.exists()) {
				directorioCV.mkdirs();
			}
			String fileFoto = foto.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
			String fileCV = uploadcv.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
			File saveFileFoto = new File(fotoDir + fileFoto);
			File saveFileCV = new File(cvDir + fileCV);
			foto.transferTo(saveFileFoto.getAbsoluteFile());
			uploadcv.transferTo(saveFileCV.getAbsoluteFile());

			Perfil perfil = new Perfil();
			perfil.setClave(clave);
			perfil.setEmail(email);
			perfil.setDni(dni);
			perfil.setNombre(nombre);
			perfil.setDireccion(direccion);
			perfil.setPassword(hashedPassword);

			perfil.setFotoUrl(appBaseUrl + "/uploads/fotos/" + fileFoto);
			perfil.setDocumentoUrl(appBaseUrl + "/uploads/documentos/" + fileCV);

			perfilRepository.save(perfil);
			return ResponseEntity.ok("{\"message\":\"Perfil creado correctamente\"}");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\":\"Error al guardar el perfil.\"}");
		}
	}

	public ResponseEntity<String> actualizarPerfil(int id, String nombre, String dni, String direccion, String email,
			String clave, MultipartFile foto, MultipartFile uploadcv) {
		try {
			Optional<Perfil> perfilOpt = perfilRepository.findById(id);
			if (perfilOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Perfil no encontrado\"}");
			}

			try {
				Optional<ResponseEntity<String>> errorValidacion = validarDatosPerfil(id, nombre, dni, direccion, email,
						clave);
				if (errorValidacion.isPresent()) {
					return errorValidacion.get();
				}
			} catch (IllegalArgumentException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
			}
			Perfil perfil = perfilOpt.get();
			perfil.setNombre(nombre);
			perfil.setDni(dni);
			perfil.setDireccion(direccion);
			perfil.setEmail(email);
			perfil.setClave(clave);

			// Guardar archivos si llegan nuevos
			if (foto != null && !foto.isEmpty()) {
				String fileFoto = foto.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
				File fotoDir = new File(uploadDir + "fotos/");
				fotoDir.mkdirs();
				File fotoFile = new File(fotoDir, fileFoto);
				foto.transferTo(fotoFile);
				perfil.setFotoUrl(appBaseUrl + "/uploads/fotos/" + fileFoto);
			}
			if (uploadcv != null && !uploadcv.isEmpty()) {
				String fileCV = uploadcv.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
				File cvDir = new File(uploadDir + "documentos/");
				cvDir.mkdirs();
				File cvFile = new File(cvDir, fileCV);
				uploadcv.transferTo(cvFile);
				perfil.setDocumentoUrl(appBaseUrl + "/uploads/documentos/" + fileCV);
			}
			perfilRepository.save(perfil);
			return ResponseEntity.ok("{\"message\":\"Perfil actualizado correctamente\"}");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\":\"Error al actualizar el perfil.\"}");
		}
	}

	private Optional<ResponseEntity<String>> validarDatosPerfil(Integer id, String nombre, String dni, String direccion,
			String email, String clave) {
		String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";

		// 1. Validaciones de DNI
		if (dni != null && dni.length() > 20) {
			return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"error\":\"El DNI no debe ser mayor de 20 caracteres.\"}"));
		}
		if (dni == null || !dni.matches("\\d+")) {
			return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"{\"error\":\"El DNI debe contener solo números, no se permiten letras ni caracteres especiales.\"}"));
		}

		// Control de DNI Duplicado (Ignorando al propio usuario si es un update)
		if (id == 0) { // CREATE
			if (perfilRepository.existsByDni(dni)) {
				return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\":\"El DNI ya se encuentra registrado. ¡Verifique!\"}"));
			}
		} else { // UPDATE
			Optional<Perfil> perfilExistente = perfilRepository.findByDni(dni);
			if (perfilExistente.isPresent() && perfilExistente.get().getId_perfil() != id) {
				return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\":\"El DNI ya se encuentra registrado por otro usuario.\"}"));
			}
		}

		// 2. Validaciones de Nombre
		if (nombre != null && nombre.length() > 100) {
			return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"error\":\"El Nombre no debe ser mayor de 100 caracteres.\"}"));
		}

		// 2. Validaciones de Direcciones
		if (direccion != null && direccion.length() > 255) {
			return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"error\":\"La direccion no debe ser mayor de 255 caracteres.\"}"));
		}

		// 3. Validaciones de Email
		if (email != null && (!email.matches(regexEmail) || email.length() > 100)) {
			return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"{\"error\":\"El formato del correo electrónico no es válido o supera los 100 caracteres.\"}"));
		}

		// Control de Email Duplicado (Ignorando al propio usuario si es un update)
		if (id == 0) { // CREATE
			if (perfilRepository.existsByEmail(email)) {
				return Optional
						.of(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Email ya registrado\"}"));
			}
		} else { // UPDATE
			Optional<Perfil> perfilExistenteEmail = perfilRepository.findByEmail(email);
			if (perfilExistenteEmail.isPresent() && perfilExistenteEmail.get().getId_perfil() != id) {
				return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\":\"El Email ya se encuentra registrado por otro usuario.\"}"));
			}
		}

		// 4. Validaciones de Clave / Usuario
		if (id == 0) { // CREATE
			if (perfilRepository.existsByClave(clave)) {
				return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\":\"Nombre de usuario ya registrado\"}"));
			}
		} else { // UPDATE
			Optional<Perfil> perfilExistenteClave = perfilRepository.findByClave(clave);
			if (perfilExistenteClave.isPresent() && perfilExistenteClave.get().getId_perfil() != id) {
				return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\":\"Nombre de usuario ya registrado por otro perfil.\"}"));
			}

		}
		if (clave != null && clave.length() > 100) {
			return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"error\":\"La clave no debe ser mayor de 100 caracteres.\"}"));
		}

		return Optional.empty(); // Todo válido
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
		return perfilRepository.findById(id).orElse(null);
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
