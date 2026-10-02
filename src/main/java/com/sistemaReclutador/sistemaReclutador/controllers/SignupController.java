package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistemaReclutador.sistemaReclutador.dto.SignupRequest;
import com.sistemaReclutador.sistemaReclutador.exceptions.ClaveYaRegistradaException;
import com.sistemaReclutador.sistemaReclutador.exceptions.EmailYaRegistradoException;
import com.sistemaReclutador.sistemaReclutador.services.AuthService;

@RestController
@RequestMapping("/login")
public class SignupController {

	private final AuthService authService;

	public SignupController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping
	public ResponseEntity<String> signupUser(@RequestBody SignupRequest signupRequest) {
		try {
			boolean isUserCreate = authService.createUser(signupRequest);
			if (isUserCreate) {
				return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo crear el usuario");
		} catch (EmailYaRegistradoException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (ClaveYaRegistradaException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
