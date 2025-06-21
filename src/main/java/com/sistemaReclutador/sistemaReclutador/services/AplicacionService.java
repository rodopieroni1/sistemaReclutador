package com.sistemaReclutador.sistemaReclutador.services;

import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;

@Service
public interface AplicacionService {
	
	Aplicacion crearAplicacion(AplicacionRequest aplicacionRequest);

}
