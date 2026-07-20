package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.dto.LoginRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;

public interface PerfilService {

	ResponseEntity<?> loginUsuario(LoginRequest credential);
	ResponseEntity<?> olvideContraseña(Map<String, String> body);
	ResponseEntity<?> resetearContraseña(Map<String, String> body);
	ResponseEntity<?> eliminarPerfil(int id);
	ResponseEntity<String> actualizarPerfil(int id, String nombre, String dni, String direccion, String email, String clave, MultipartFile foto, MultipartFile uploadcv);
	ResponseEntity<String> guardarPerfil(String nombre, String dni, String direccion, String email, String clave, String password, MultipartFile foto, MultipartFile uploadcv);
	ResponseEntity<Map<String, Boolean>> verificarEmailYDni(Map<String, String> datos);
	ResponseEntity<Perfil> obtenerPerfilPorName(String name);
	Perfil findById(int id);
	ResponseEntity<List<Perfil>> listarPerfiles();


}
