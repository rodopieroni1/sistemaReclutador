package com.sistemaReclutador.sistemaReclutador.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.SignupRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;
import com.sistemaReclutador.sistemaReclutador.repositories.UsuarioRepository;

@Service
public class AuthServiceImpl implements AuthService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AuthServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;

	}

	@Override
	public boolean createUser(SignupRequest signupRequest) {

		if (usuarioRepository.existsByEmail(signupRequest.getEmail())) {
			return false;
		}

		Usuario usuario = new Usuario();
		BeanUtils.copyProperties(signupRequest, usuario);
		String hashPassword = passwordEncoder.encode(signupRequest.getContraseña());
		usuario.setContraseña(hashPassword);
		usuarioRepository.save(usuario);
		return true;
	}

}
