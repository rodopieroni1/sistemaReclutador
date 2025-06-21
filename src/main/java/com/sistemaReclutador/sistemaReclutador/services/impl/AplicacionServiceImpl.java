package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.repositories.AplicacionRepository;
import com.sistemaReclutador.sistemaReclutador.services.AplicacionService;

@Service
public class AplicacionServiceImpl implements AplicacionService {
	@Autowired
	AplicacionRepository aplicacionRepository;
	
	@Override
	public Aplicacion crearAplicacion(AplicacionRequest aplicacionRequest) {
		 boolean existeAplicacion = aplicacionRepository.existsByPerfilAndOferta(
			        aplicacionRequest.getIdPerfil().getId_perfil(), aplicacionRequest.getIdOferta().getIdOferta() );
		 // Si existe, retorna un objeto Aplicacion con valores null
		    if (existeAplicacion) {
		        Aplicacion aplicacionNula = new Aplicacion();
		        aplicacionNula.setFecha(LocalDateTime.now());
		        aplicacionNula.setEstadoaplicaciones(true);
		        aplicacionNula.setPerfil(null);
		        aplicacionNula.setOferta(null);
		        return aplicacionNula;
		    }
		    System.out.println("aplicacionRequest.getIdPerfil().getId_perfil()"+aplicacionRequest.getIdPerfil().getId_perfil());
			 Aplicacion aplicacionEntity = convertDtoToEntity(aplicacionRequest);
	        return aplicacionRepository.save(aplicacionEntity);
	}
	
	 public Aplicacion convertDtoToEntity(AplicacionRequest dto) {
	        Aplicacion aplicacion = new Aplicacion();
	        aplicacion.setFecha(LocalDateTime.now());
	        aplicacion.setEstadoaplicaciones(true);
	        aplicacion.setPerfil(dto.getIdPerfil());  // Si Perfil es una entidad válida
	        aplicacion.setOferta(dto.getIdOferta());  // Si Oferta es una entidad válida
	        return aplicacion;
	    }

}
