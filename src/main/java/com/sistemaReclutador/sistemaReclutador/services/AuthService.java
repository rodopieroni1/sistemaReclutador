package com.sistemaReclutador.sistemaReclutador.services;

import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.SignupRequest;

@Service
public interface AuthService {
	
	public boolean createUser(SignupRequest signupRequest);

}
