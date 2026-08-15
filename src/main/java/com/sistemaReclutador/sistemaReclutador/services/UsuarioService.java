package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;

public interface UsuarioService {
	ResponseEntity<?> login(LoginRequest credential);
	List<Usuario> findAll();
	Usuario findById(int id);
	Usuario saveUsuario(Usuario usuario);
	ResponseEntity<ResponseRest<Usuario>> actualizarUsuario(Usuario usuarioDetails);
	ResponseEntity<?> eliminarUsuario(int id);
}
