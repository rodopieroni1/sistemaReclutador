package com.sistemaReclutador.sistemaReclutador.services;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.PerfilSignupRequest;
import com.sistemaReclutador.sistemaReclutador.dto.SignupRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.UsuarioRepository;

@Service
public class AuthServiceImpl implements AuthService {

	private final UsuarioRepository usuarioRepository;
	private final PerfilRepository perfilRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthServiceImpl(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.perfilRepository = perfilRepository;
		this.passwordEncoder = passwordEncoder;

	}
	
	@Override
	public boolean createUser(SignupRequest signupRequest) {
		if (usuarioRepository.existsByEmail(signupRequest.getEmail())) {
			return false;
		}
		Usuario usuario = new Usuario();
		BeanUtils.copyProperties(signupRequest, usuario);
		String hashPassword = passwordEncoder.encode(signupRequest.getPassword());
		usuario.setContraseña(hashPassword);
		usuario.setClave("null");
		usuario.setNombre(signupRequest.getEmail());
		usuario.setTipoUsuario("Administrador");
		usuarioRepository.save(usuario);
		return true;
	}
	
	
	@Override
	public boolean createUserPerfil(PerfilSignupRequest perfilSignupRequest) {
		if (perfilRepository.existsByEmail(perfilSignupRequest.getEmail())) {
			return false;
		}
		Perfil perfil = new Perfil();
		BeanUtils.copyProperties(perfilSignupRequest, perfil);
		String hashPassword = passwordEncoder.encode(perfilSignupRequest.getContraseña());
		perfil.setContraseña(hashPassword);
		perfilRepository.save(perfil);
		return true;
	}

}
