package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.entities.PasswordResetToken;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.repositories.PasswordResetTokenRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.EmailService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http://localhost:4200")
@Configuration
@RestController
@RequestMapping("/perfiles")
public class PerfilController {

	private final String UPLOAD_DIR = "C:/Users/Rodrigo/Documents/SistemaReclutadorFront/proyectoReclutador/src/assets/uploads/"; // Cambia
	private JwtUtil jwtUtil;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	@Autowired
	private PasswordResetTokenRepository tokenRepository;

	@Bean(name = "customPasswordEncoder")
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest credential) {
	    Optional<Perfil> perfil = perfilRepository.findByClave(credential.getClave());
	    if (perfil.isPresent() && passwordEncoder().matches(credential.getPassword(), perfil.get().getPassword())) {
	        String token = jwtUtil.generateToken(perfil.get().getNombre());
	        return ResponseEntity.ok().body(Map.of("token", token));
	    }
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<Perfil> obtenerPerfilPorName(@PathVariable String name) {
	    Perfil nameReturn = perfilRepository.findByName(name);
	    if (nameReturn == null || nameReturn.equals(null) ) {
	        return null;
	    }
	    return ResponseEntity.ok(nameReturn);
	}
	
	@PostMapping("/verificar")
	public ResponseEntity<Map<String, Boolean>> verificarEmailYDni(@RequestBody Map<String, String> datos) {
	    String email = datos.get("email");
	    String dni = datos.get("dni");
	    boolean emailExists = perfilRepository.existsByEmail(email);
	    boolean dniExists = perfilRepository.existsByDni(dni);
	    Map<String, Boolean> response = Map.of(
	        "emailExists", emailExists,
	        "dniExists", dniExists
	    );
	    return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<String> crearPerfil(@RequestParam("nombre") String nombre, @RequestParam("dni") String dni,
	        @RequestParam("direccion") String direccion, @RequestParam("email") String email,
	        @RequestParam("clave") String clave, @RequestParam("password") String password,
	        @RequestParam("foto") MultipartFile foto, @RequestParam("uploadcv") MultipartFile uploadcv) {
	    try {
	    	// Hashear la contraseña antes de guardarla
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        String hashedPassword = encoder.encode(password);
	        // Define directorios de almacenamiento
	        String fotoDir = UPLOAD_DIR + "fotos/";
	        String cvDir = UPLOAD_DIR + "documentos/";
	        // Crear directorios si no existen
	        File directorioFoto = new File(fotoDir);
	        if (!directorioFoto.exists()) {
	            directorioFoto.mkdirs();
	        }
	        File directorioCV = new File(cvDir);
	        if (!directorioCV.exists()) {
	            directorioCV.mkdirs();
	        } 	        String fileFoto = foto.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
	        String fileCV = uploadcv.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
	        File saveFileFoto = new File(fotoDir + fileFoto);
	        File saveFileCV = new File(cvDir + fileCV);
	        System.out.println(System.getProperty("java.io.tmpdir"));

	        foto.transferTo(saveFileFoto);
	        uploadcv.transferTo(saveFileCV);
	        Perfil perfil = new Perfil();
	        
	        List<Perfil> existentes  = perfilRepository.verificarDniClaveEmail(clave, email, dni);
	        
	        for (Perfil p : existentes) {
	            if (p.getClave().equals(clave)) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("{\"error\":\"Nombre de usuario ya registrado\"}");
	            }
	            if (p.getEmail().equals(email)) {
	            	System.out.println("VER CORREOS: "+ p.getEmail() + "-"+ email);
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("{\"error\":\"Email ya registrado\"}");
	            }
	            if (p.getDni().equals(dni)) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("{\"error\":\"DNI ya registrado\"}");
	            }
	        }


	        
	        perfil.setClave(clave);
	        perfil.setEmail(email);
	        perfil.setDni(dni);
	        
	        perfil.setNombre(nombre);
	        perfil.setDireccion(direccion);
	        perfil.setPassword(hashedPassword);
	        // Guardar URLs en la base de datos
	        perfil.setFotoUrl("http://localhost:8080/uploads/fotos/" + fileFoto);
	        perfil.setDocumentoUrl("http://localhost:8080/uploads/documentos/" + fileCV);
	        // Guardar perfil en la base de datos
	        perfilRepository.save(perfil);
	        return ResponseEntity.ok("{\"message\":\"Perfil creado correctamente\"}");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	        	    .body("{\"error\":\"Error al guardar el perfil.\"}");
	    }
	}

	@GetMapping("/{id}")
	public Perfil obtenerPerfilPorId(@PathVariable int id) {
		return perfilRepository.findById(id).orElse(null);
	}

	@GetMapping
	public ResponseEntity<List<Perfil>> listarPerfiles() {
		List<Perfil> perfiles = perfilRepository.findAll();
		return new ResponseEntity<>(perfiles, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> actualizarPerfil(
	    @PathVariable int id,
	    @RequestParam("nombre") String nombre,
	    @RequestParam("dni") String dni,
	    @RequestParam("direccion") String direccion,
	    @RequestParam("email") String email,
	    @RequestParam("clave") String clave,
	    //@RequestParam("password") String password,
	    @RequestParam(value = "foto", required = false) MultipartFile foto,
	    @RequestParam(value = "uploadcv", required = false) MultipartFile uploadcv) {
	    try {
	        Optional<Perfil> perfilOpt = perfilRepository.findById(id);
	        if (perfilOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("{\"error\":\"Perfil no encontrado\"}");
	        }
	        //String hashedPassword = encoder.encode(password);
	        Perfil perfil = perfilOpt.get();
	        perfil.setNombre(nombre);
	        perfil.setDni(dni);
	        perfil.setDireccion(direccion);
	        perfil.setEmail(email);
	        perfil.setClave(clave);

	        // Guardar archivos si llegan nuevos
	        if (foto != null && !foto.isEmpty()) {
	            String fileFoto = foto.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
	            File fotoDir = new File(UPLOAD_DIR + "fotos/");
	            fotoDir.mkdirs();
	            File fotoFile = new File(fotoDir, fileFoto);
	            foto.transferTo(fotoFile);
	            perfil.setFotoUrl("http://localhost:8080/uploads/fotos/" + fileFoto);
	        }
	        if (uploadcv != null && !uploadcv.isEmpty()) {
	            String fileCV = uploadcv.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
	            File cvDir = new File(UPLOAD_DIR + "documentos/");
	            cvDir.mkdirs();
	            File cvFile = new File(cvDir, fileCV);
	            uploadcv.transferTo(cvFile);
	            perfil.setDocumentoUrl("http://localhost:8080/uploads/documentos/" + fileCV);
	        }
	        perfilRepository.save(perfil);
	        return ResponseEntity.ok("{\"message\":\"Perfil actualizado correctamente\"}");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("{\"error\":\"Error al actualizar el perfil.\"}");
	    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarPerfil(@PathVariable int id) {
		perfilRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/olvide-password")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
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
	            "Hacé clic en el siguiente enlace para restablecer tu contraseña: " + resetLink);
	        return ResponseEntity.ok(Map.of("message", "Si el usuario existe, se envió el enlace"));
	    }

	    return ResponseEntity.ok(Map.of("message", "Si el usuario existe, se envió el enlace"));
	}
	
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
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
	    tokenRepository.delete(resetToken); // eliminar el token usado

	    return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
	}
	
	
}
