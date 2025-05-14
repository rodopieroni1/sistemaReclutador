package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;
import com.sistemaReclutador.sistemaReclutador.repositories.UsuarioRepository;
import com.sistemaReclutador.sistemaReclutador.services.AuthService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
	private AuthService authService;
	private JwtUtil jwtUtil;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest credential) {
	    Optional<Usuario> user = usuarioRepository.findByClave(credential.getClave());
	    if (user.isPresent() && passwordEncoder().matches(credential.getPassword(), user.get().getContraseña())) {
	        String token = jwtUtil.generateToken(user.get().getNombre());
	        System.out.println("SALE5TOKEN: "+ credential.getImagen());
	        return ResponseEntity.ok().body(Map.of("token", token));
	    }
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
	}

    // Obtener todos los usuarios
    @ResponseStatus(HttpStatus.OK)
	@GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // Crear un nuevo usuario
    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable int id, @RequestBody Usuario usuarioDetails) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setClave(usuarioDetails.getClave());
            usuario.setContraseña(usuarioDetails.getContraseña());
            usuario.setNombre(usuarioDetails.getNombre());
            return usuarioRepository.save(usuario);
        }
        return null; // Devuelve null si el usuario no existe
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public String deleteUsuario(@PathVariable int id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            usuarioRepository.deleteById(id);
            return "Usuario eliminado con éxito";
        }
        return "Usuario no encontrado";
    }
}
