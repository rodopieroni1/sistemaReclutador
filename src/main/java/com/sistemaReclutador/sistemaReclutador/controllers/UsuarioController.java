package com.sistemaReclutador.sistemaReclutador.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistemaReclutador.sistemaReclutador.dto.CambiarPasswordRequest;
import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.UsuarioService;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
	

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest credential) {
	    return usuarioService.login(credential);
	}

	@GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable int id) {
        return usuarioService.findById(id);
    }

    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.saveUsuario(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseRest<Usuario>> updateUsuario(@PathVariable int id, @RequestBody Usuario usuarioDetails) {
		return usuarioService.actualizarUsuario(usuarioDetails);
    }
    
    @PutMapping("/contrasena-admin")
    public ResponseEntity<?> modificarContrasena(
            Authentication authentication,
            @RequestBody CambiarPasswordRequest request) {

        try {
            usuarioService.cambiarPasswordAutenticado(
                    authentication.getName(),
                    request.getPassword()
            );

            return ResponseEntity.ok(
                    Map.of(
                        "message",
                        "Contraseña modificada correctamente"
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable int id) {
    	return usuarioService.eliminarUsuario(id);
    }
}
