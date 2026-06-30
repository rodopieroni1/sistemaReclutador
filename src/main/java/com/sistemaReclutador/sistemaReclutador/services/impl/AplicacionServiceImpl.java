package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.repositories.AplicacionRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.AplicacionService;

@Service
public class AplicacionServiceImpl implements AplicacionService {
	@Autowired
	AplicacionRepository aplicacionRepository;
	@Autowired
	PerfilRepository perfilRepository;
	@Autowired
	OfertaRepository ofertaRepository;

	private static final Logger log = LoggerFactory.getLogger(AplicacionServiceImpl.class);

	@Override
	public AplicacionResponseDTO crearAplicacion(AplicacionRequest aplicacionRequest) {

	    if (aplicacionRequest.getIdPerfil() == null || aplicacionRequest.getIdPerfil().getId_perfil() == null) {
	        throw new IllegalArgumentException("El id_perfil no puede ser null");
	    }

	    Perfil perfil = perfilRepository.findById(aplicacionRequest.getIdPerfil().getId_perfil())
	            .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

	    Oferta oferta = ofertaRepository.findById(aplicacionRequest.getIdOferta().getIdOferta())
	            .orElseThrow(() -> new RuntimeException("Oferta no encontrada"));

	    System.out.println("ServiceIMPL:::::: " + oferta.getDescripcionOferta());
	    Optional<Aplicacion> aplicacionOpt = aplicacionRepository.findByPerfilAndOferta(perfil, oferta);
	    System.out.println("ServiceIMPL:::::: " + oferta.getDescripcionOferta());
	   
	    if (aplicacionOpt.isPresent()) {
	        Aplicacion aplicacionExistenteRecuperada = aplicacionOpt.get();
	        
	        // CASO 1: Ya existía y el estado en base de datos ya es true (1)
	        if (Boolean.TRUE.equals(aplicacionExistenteRecuperada.isEstadoaplicaciones())) {
	            return new AplicacionResponseDTO(ResultadosAplicacion.YA_APLICO, aplicacionExistenteRecuperada);
	        }
	        
	        // CASO 2: Ya existía pero el estado estaba en false (0). La reactivamos.
	        aplicacionExistenteRecuperada.setEstadoaplicaciones(true);
	        aplicacionExistenteRecuperada.setFecha(LocalDateTime.now()); 
	        
	        // Guardamos los cambios en la base de datos
	        Aplicacion actualizada = aplicacionRepository.save(aplicacionExistenteRecuperada); 
	        return new AplicacionResponseDTO(ResultadosAplicacion.ACTUALIZACION_ESTADO, actualizada);
	    }

	    // CASO 3: Si no existía, la creamos desde cero como nueva postulación
	    Aplicacion aplicacionEntity = new Aplicacion();
	    aplicacionEntity.setFecha(LocalDateTime.now());
	    aplicacionEntity.setEstadoaplicaciones(true);
	    aplicacionEntity.setPerfil(perfil);
	    aplicacionEntity.setOferta(oferta);

	    Aplicacion nueva = aplicacionRepository.save(aplicacionEntity);
	    return new AplicacionResponseDTO(ResultadosAplicacion.APLICACION_CREADA, nueva);
	}


	@Override
	public boolean existsById(Integer id) {
		return aplicacionRepository.existsById(id);
	}

	@Override
	public List<Object[]> obtenerAplicacionesPerfil(int idPerfil) {
		List<Object[]> listadoAplicaciones = aplicacionRepository.obtenerAplicacionesPerfil(idPerfil);
		if (listadoAplicaciones != null) {
			log.info("Aqui" + listadoAplicaciones);
			return listadoAplicaciones;
		} else {
			return null;
		}
	}

	@Override
	public void cambiarEstado(Integer id, boolean estado) {
		Optional<Aplicacion> aplicacionActualizado = aplicacionRepository.findById(id);
		if (aplicacionActualizado.isPresent()) {
			Aplicacion aplicacion = aplicacionActualizado.get();
			aplicacion.setEstadoaplicaciones(estado);
			aplicacionRepository.save(aplicacion);
		}
	}
}
