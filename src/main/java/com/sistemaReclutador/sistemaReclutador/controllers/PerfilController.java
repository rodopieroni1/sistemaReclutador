package com.sistemaReclutador.sistemaReclutador.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.services.PerfilService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/perfiles")
@RequiredArgsConstructor
@Validated
public class PerfilController {

	private final PerfilService perfilService;

	@GetMapping("/auth/ping")
	public ResponseEntity<Void> ping() {
		return ResponseEntity.ok().build();
	}

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest credential) {
		return perfilService.loginUsuario(credential);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<Map<String, String>> logout(Authentication authentication) {
		log.info("Authentication: {}", authentication);
		if (authentication != null) {
			log.info("Usuario: {}", authentication.getName());
			perfilService.cerrarSesion(authentication.getName());
		} else {
			log.warn("Authentication es NULL");
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
	public ResponseEntity<String> crearPerfil(
			@RequestParam("nombre") String nombre,
			@RequestParam("dni") String dni,
			@RequestParam("direccion") String direccion,
			@RequestParam("email") String email,
			@RequestParam("clave") String clave,
			@RequestParam("password") String password,
			@RequestParam("foto") MultipartFile foto,
			@RequestParam("uploadcv") MultipartFile uploadcv) {
		return perfilService.guardarPerfil(nombre, dni, direccion, email, clave, password, foto, uploadcv);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Perfil> obtenerPerfilPorId(@PathVariable Integer id) {
		return ResponseEntity.ok(perfilService.findById(id));
	}

	@GetMapping
	public ResponseEntity<List<Perfil>> listarPerfiles() {
		return perfilService.listarPerfiles();
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> actualizarPerfil(
			@PathVariable Integer id,
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
	public ResponseEntity<?> eliminarPerfil(@PathVariable Integer id) {
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