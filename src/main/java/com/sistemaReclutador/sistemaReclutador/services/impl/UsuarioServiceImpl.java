package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;
import com.sistemaReclutador.sistemaReclutador.repositories.UsuarioRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.UsuarioService;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository usuarioRepository;
	private JwtUtil jwtUtil;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public ResponseEntity<?> login(LoginRequest credential) {
		try {
			Optional<Usuario> user = usuarioRepository.findByClave(credential.getClave());
			if (user.isPresent() && passwordEncoder().matches(credential.getPassword(), user.get().getContraseña())) {
				String token = jwtUtil.generateTokenUsuario(user.get().getNombre());
				return ResponseEntity.ok().body(Map.of("token", token));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Error en el ingreso"));

		}
	}

	@Override
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario findById(int id) {
		return usuarioRepository.findById(id).orElse(null);
	}

	@Override
	public Usuario saveUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public ResponseEntity<ResponseRest<Usuario>> actualizarUsuario(Usuario usuarioDetails) {
		ResponseRest<Usuario> response;
		try {
			Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioDetails.getId());
			if (usuarioOptional.isPresent()) {
				Usuario usuario = usuarioOptional.get();
				usuario.setClave(usuarioDetails.getClave());
				usuario.setContraseña(usuarioDetails.getContraseña());
				usuario.setNombre(usuarioDetails.getNombre());
				usuarioRepository.save(usuario);
				response = new ResponseRest<>(true, "Usuario actualizado satisfactoriamente", usuario,LocalDateTime.now(), "200");
				return ResponseEntity.ok(response);
			} else {
				response = new ResponseRest<>(false, "No se encontró el usuario", null, LocalDateTime.now(), "404");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseRest<>(false, "Ocurrió un error al actualizar el usuario: " + e.getMessage(), null,
					LocalDateTime.now(), "500");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@Override
	public ResponseEntity<?> eliminarUsuario(int id) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
		ResponseRest<Oferta> response;
		try {
			if (usuarioOptional.isPresent()) {
				usuarioRepository.deleteById(id);
				response = new ResponseRest<>(true, "Usuario eliminado satisfactoriamente", null,
						LocalDateTime.now(), "200");
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			} else {
				response = new ResponseRest<>(false, "No se pudo eliminar el usuario", null, LocalDateTime.now(), "400");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseRest<>(false, "Ocurrió un error al eliminar el usuario: " + e.getMessage(), null,
					LocalDateTime.now(), "500");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);			
		}
	}

}
