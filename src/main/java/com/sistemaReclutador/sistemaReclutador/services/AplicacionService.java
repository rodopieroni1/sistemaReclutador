package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;

@Service
public interface AplicacionService {
    boolean existsById(Integer id);
    AplicacionResponseDTO crearAplicacion(AplicacionRequest aplicacionRequest);
    ResponseEntity<ResponseRest<Aplicacion>> modificarOferta(int id, AplicacionRequest aplicacionDetails);
    List<Object[]> obtenerAplicacionesPerfil(int idPerfil);
	void cambiarEstado(Integer id, boolean estado);
	List<Aplicacion> findAllDesc();
	List<Aplicacion> findAllDescActivas();
	void deleteById(int id);
	
}
