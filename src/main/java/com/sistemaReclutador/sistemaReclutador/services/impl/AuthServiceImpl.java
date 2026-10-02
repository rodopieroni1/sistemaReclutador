package com.sistemaReclutador.sistemaReclutador.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.Enum.TipoUsuario;
import com.sistemaReclutador.sistemaReclutador.dto.PerfilSignupRequest;
import com.sistemaReclutador.sistemaReclutador.dto.SignupRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;
import com.sistemaReclutador.sistemaReclutador.exceptions.ClaveYaRegistradaException;
import com.sistemaReclutador.sistemaReclutador.exceptions.EmailYaRegistradoException;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.UsuarioRepository;
import com.sistemaReclutador.sistemaReclutador.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private final UsuarioRepository usuarioRepository;
	private final PerfilRepository perfilRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthServiceImpl(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository,
			PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.perfilRepository = perfilRepository;
		this.passwordEncoder = passwordEncoder;

	}

	@Override
	public boolean createUser(SignupRequest signupRequest) {

	    if (usuarioRepository.existsByEmail(signupRequest.getEmail())) {
	        throw new EmailYaRegistradoException("El email ya está registrado");
	    }

	    if (usuarioRepository.existsByClave(signupRequest.getClave())) {
	        throw new ClaveYaRegistradaException("La clave ya está en uso");
	    }

	    Usuario usuario = new Usuario();

	    usuario.setClave(signupRequest.getClave());
	    usuario.setNombre(signupRequest.getNombre());
	    usuario.setEmail(signupRequest.getEmail());

	    String hashPassword =
	            passwordEncoder.encode(signupRequest.getPassword());

	    usuario.setContraseña(hashPassword);
	    usuario.setTipoUsuario(TipoUsuario.RECLUTADOR);

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
		String hashPassword = passwordEncoder.encode(perfilSignupRequest.getPassword());
		perfil.setPassword(hashPassword);
		perfilRepository.save(perfil);
		return true;
	}

}
