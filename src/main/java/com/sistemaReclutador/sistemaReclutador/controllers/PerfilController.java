package com.sistemaReclutador.sistemaReclutador.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.services.PerfilService;

@CrossOrigin(origins = "http://localhost:4200")
@Configuration
@RestController
@RequestMapping("/perfiles")
public class PerfilController {

	@Autowired
	private PerfilService perfilService;
	
	@GetMapping("/auth/ping")
	public ResponseEntity<?> ping() {
	    return ResponseEntity.ok().build();
	}
	
	@PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest credential) {
        return perfilService.loginUsuario(credential);
    }
	
	@PostMapping("/auth/logout")
	public ResponseEntity<?> logout(Authentication authentication) {
		System.out.println("===== LOGOUT =====");
	    System.out.println("Authentication: " + authentication);
	    if (authentication != null) {
	        System.out.println("Usuario: " + authentication.getName());
	        perfilService.cerrarSesion(authentication.getName());
	    } else {
	        System.out.println("Authentication es NULL");
	    }
	    return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada"));
	}
		
	@GetMapping("/name/{name}")
	public ResponseEntity<Perfil> obtenerPerfilPorName(@PathVariable String name) {
	    return perfilService.obtenerPerfilPorName(name);
	}
	
	@PostMapping("/verificar")
	public ResponseEntity<Map<String, Boolean>> verificarEmailYDni(@RequestBody Map<String, String> datos) {
	   return perfilService.verificarEmailYDni(datos);
	}

	@PostMapping
	public ResponseEntity<String> crearPerfil(@RequestParam("nombre") String nombre, @RequestParam("dni") String dni,
	        @RequestParam("direccion") String direccion, @RequestParam("email") String email,
	        @RequestParam("clave") String clave, @RequestParam("password") String password,
	        @RequestParam("foto") MultipartFile foto, @RequestParam("uploadcv") MultipartFile uploadcv) {
	    return perfilService.guardarPerfil(nombre, dni, direccion, email, clave, password, foto, uploadcv);
	}

	@GetMapping("/{id}")
	public Perfil obtenerPerfilPorId(@PathVariable int id) {
		return perfilService.findById(id); 
	}

	@GetMapping
	public ResponseEntity<List<Perfil>> listarPerfiles() {
		return perfilService.listarPerfiles();
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> actualizarPerfil(
	    @PathVariable int id,
	    @RequestParam("nombre") String nombre,
	    @RequestParam("dni") String dni,
	    @RequestParam("direccion") String direccion,
	    @RequestParam("email") String email,
	    @RequestParam("clave") String clave,
	    @RequestParam(value = "foto", required = false) MultipartFile foto,
	    @RequestParam(value = "uploadcv", required = false) MultipartFile uploadcv) {
	    return perfilService.actualizarPerfil(id, nombre, dni, direccion, email, clave, foto, uploadcv);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminarPerfil(@PathVariable int id) {
		return perfilService.eliminarPerfil(id);	
	}
	
	@PostMapping("/olvide-password")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
	   return perfilService.olvideContraseña(body);
	}
		
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
		 return perfilService.resetearContraseña(body);
	}

}
