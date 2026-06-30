package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;

@Service
public interface AplicacionService {
    boolean existsById(Integer id);
    AplicacionResponseDTO crearAplicacion(AplicacionRequest aplicacionRequest);
    List<Object[]> obtenerAplicacionesPerfil(int idPerfil);
	void cambiarEstado(Integer id, boolean estado);
	
}
