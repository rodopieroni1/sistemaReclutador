package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.io.File;
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
	//cambiar uando se haga el desliegue
	
	@Value("${app.base.url}")
	private String appBaseUrl;
	
	@Value("${app.upload.dir}")
	private String uploadDir;

	@Bean(name = "customPasswordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public ResponseEntity<String> guardarPerfil(String nombre, String dni, String direccion, String email, String clave,
			String password, MultipartFile foto, MultipartFile uploadcv) {
		try {
			if (dni != null && dni.length() > 20) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\":\"El DNI no debe ser mayor de 20 caracteres.\"}");
			}
			if (!dni.matches("\\d+")) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                .body("{\"error\":\"El DNI debe contener solo números, no se permiten letras ni caracteres especiales.\"}");
		    }
			if (email != null && email.length() > 100) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\":\"El Email no debe ser mayor de 100 caracteres.\"}");
			}

			if (perfilRepository.existsByClave(clave)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\":\"Nombre de usuario ya registrado\"}");
			}

			if (perfilRepository.existsByEmail(email)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\":\"Email ya registrado\"}");
			}

			if (perfilRepository.existsByDni(dni)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\":\"El DNI ya se encuentra registrado. ¡Verifique!\"}");
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
			// String hashedPassword = encoder.encode(password);
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

	@Override
	public ResponseEntity<?> eliminarPerfil(int id) {
		perfilRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<?> loginUsuario(LoginRequest credential) {
		Optional<Perfil> perfil = perfilRepository.findByClave(credential.getClave());
		if (perfil.isPresent() && passwordEncoder().matches(credential.getPassword(), perfil.get().getPassword())) {
			String token = JwtUtil.generateToken(perfil.get().getNombre());
			return ResponseEntity.ok().body(Map.of("token", token));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
	}

	@Override
	public ResponseEntity<?> olvideContraseña(Map<String, String> body) {
		String clave = body.get("email");
		Optional<Perfil> perfilOpt = perfilRepository.findByEmail(clave);
		if (perfilOpt.isPresent()) {
			Perfil perfil = perfilOpt.get();
			String token = UUID.randomUUID().toString();
			PasswordResetToken resetToken = new PasswordResetToken();
			resetToken.setToken(token);
			resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
			resetToken.setPerfil(perfil);
			tokenRepository.save(resetToken);
			String resetLink = "http://localhost:4200/reset-password?token=" + token;
			emailService.send(perfil.getEmail(), "Recuperación de contraseña",
					"ATENCION!, si usted no pidio un reseteo de contraseña, desestime este mail. /n Hacé clic en el siguiente enlace para restablecer tu contraseña: "
							+ resetLink);
			return ResponseEntity.ok(Map.of("message", "Si el usuario existe, se envió el enlace"));
		}
		return ResponseEntity.ok(Map.of("message", "Si el usuario existe, se envió el enlace"));
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
	

}
