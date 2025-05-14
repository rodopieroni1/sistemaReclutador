package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.AuthService;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes desde el frontend
@RestController
@RequestMapping("/perfiles")
public class PerfilController {

	private final String UPLOAD_DIR = "C:/Users/Rodrigo/Documents/SistemaReclutadorFront/proyectoReclutador/src/assets/uploads/"; // Cambia
	/*public PerfilController(AuthService authService) {
	}*/
	private JwtUtil jwtUtil;
	@Autowired
	private PerfilRepository perfilRepository;

	@Bean
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
	public ResponseEntity<String> obtenerPerfilPorName(@PathVariable String name) {
	    String nameReturn = perfilRepository.findByName(name);
	    
	    System.out.println("Respuesta enviada al frontend: " + nameReturn);

	    if (nameReturn == null || nameReturn.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL no encontrado");
	    }

	    return ResponseEntity.ok(nameReturn);
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
	        }

	        // Sanitizar nombres de archivo
	        String fileFoto = foto.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
	        String fileCV = uploadcv.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

	        // Crear rutas de archivos
	        File saveFileFoto = new File(fotoDir + fileFoto);
	        File saveFileCV = new File(cvDir + fileCV);

	        // Guardar archivos
	        foto.transferTo(saveFileFoto);
	        uploadcv.transferTo(saveFileCV);

	        // Crear el objeto Perfil
	        Perfil perfil = new Perfil();
	        perfil.setNombre(nombre);
	        perfil.setDni(dni);
	        perfil.setDireccion(direccion);
	        perfil.setEmail(email);
	        perfil.setClave(clave);
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
		System.out.println("PARAMETROOOO:"+ id);
		return perfilRepository.findById(id).orElse(null);
	}

	@GetMapping
	public ResponseEntity<List<Perfil>> listarPerfiles() {
		List<Perfil> perfiles = perfilRepository.findAll();
		System.out.println("perfiles:"+ perfiles);
		return new ResponseEntity<>(perfiles, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public Perfil actualizarPerfil(@PathVariable int id, @RequestBody Perfil perfilDetails) {
		Optional<Perfil> perfilActualizado = perfilRepository.findById(id);
		if (perfilActualizado.isPresent()) {
			Perfil perfil = perfilActualizado.get();
			perfil.setDireccion(perfilDetails.getDireccion());
			perfil.setDni(perfilDetails.getDni());
			perfil.setNombre(perfilDetails.getNombre());
			return perfilRepository.save(perfil);
		}
		return null; // Devuelve null si el usuario no existe
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarPerfil(@PathVariable int id) {
		perfilRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
