package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistemaReclutador.sistemaReclutador.dto.SignupRequest;
import com.sistemaReclutador.sistemaReclutador.services.AuthService;

@RestController
@RequestMapping("/login")
public class SignupController {

	private final AuthService authService;
	
	public SignupController(AuthService authService) {
		this.authService=authService;
	}
	
	@PostMapping
	public ResponseEntity<String> signupUser(@RequestBody SignupRequest signupRequest){
		boolean isUserCreate = authService.createUser(signupRequest);
		if(isUserCreate) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Usuario Creado satisfactoriamente");
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo Crear el usuario");			
		}		
	}	
}
