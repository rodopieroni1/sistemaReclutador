package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionesMiasResponse;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;

@Service
public interface AplicacionService {
    boolean existsById(Integer id);
    AplicacionResponseDTO crearAplicacion(AplicacionRequest aplicacionRequest);
    List<AplicacionesMiasResponse> obtenerAplicacionesPerfil(int idPerfil, String format);
	void cambiarEstado(Integer id, boolean estado);
	List<Aplicacion> findAllDesc();
	List<Aplicacion> findAllDescActivas();
	void deleteById(int id);
	
}
