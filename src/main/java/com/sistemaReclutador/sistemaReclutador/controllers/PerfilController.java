package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.web.bind.annotation.*;

import com.sistemaReclutador.sistemaReclutador.dto.SignupRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.AuthService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/perfiles")
public class PerfilController {

		private final AuthService authService;
		
		@Autowired
		public PerfilController(AuthService authService) {
				this.authService=authService;
		}
		 
	    @Autowired
	    private PerfilRepository perfilRepository;

	    @PostMapping
	    public ResponseEntity<Perfil> crearPerfil(@RequestBody Perfil perfil) {
	        Perfil nuevoPerfil = perfilRepository.save(perfil);
	        return new ResponseEntity<>(nuevoPerfil, HttpStatus.CREATED);
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
	    
	   
		
		@PostMapping
		public ResponseEntity<String> signupPerfil(@RequestBody SignupRequest signupRequest){
			boolean isUserCreate = authService.createUser(signupRequest);
			if(isUserCreate) {
				return ResponseEntity.status(HttpStatus.CREATED).body("Usuario Creado satisfactoriamente");
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo Crear el usuario");			
			}		
		}	
	}
