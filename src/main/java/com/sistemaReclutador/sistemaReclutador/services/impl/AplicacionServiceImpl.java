package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
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

		Optional<Aplicacion> aplicacionOpt = aplicacionRepository.findByPerfilAndOferta(perfil, oferta);

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
	public ResponseEntity<ResponseRest<Aplicacion>> modificarOferta(int id, AplicacionRequest aplicacionDetails) {
		Optional<Aplicacion> aplicacionActualizado = aplicacionRepository.findById(id);
		ResponseRest<Aplicacion> response;
		if (aplicacionActualizado.isPresent()) {
			Aplicacion aplicacion = aplicacionActualizado.get();
        	aplicacion.setOferta(aplicacionDetails.getIdOferta() );
        	aplicacion.setPerfil(aplicacionDetails.getIdPerfil());
            aplicacion.setEstadoaplicaciones(aplicacionDetails.isEstadoaplicaciones());
        	aplicacion.setFecha(aplicacionDetails.getFechaAplicacion());
        	aplicacionRepository.save(aplicacion);
        	
        	response = new ResponseRest<Aplicacion>(true, "Aplicacion Acutalizada satisfactoriamente", aplicacion,
					LocalDateTime.now(), "200");
        	return ResponseEntity.status(HttpStatus.CREATED).body(response);
        	}
        	 else {
            response = new ResponseRest<Aplicacion>(false, "No se pudo Actualizar la Oferta", null, LocalDateTime.now(),
     					"400");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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

	@Override
	public List<Aplicacion> findAllDesc() {
	    try {
	        List<Aplicacion> aplicaciones = aplicacionRepository.findAllDesc();

	        if (aplicaciones == null || aplicaciones.isEmpty()) {
	            return null;
	        }

	        return aplicaciones;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public List<Aplicacion> findAllDescActivas() {
	    try {
	        List<Aplicacion> aplicaciones = aplicacionRepository.findAllDescActivas();

	        if (aplicaciones == null || aplicaciones.isEmpty()) {
	            return null;
	        }

	        return aplicaciones;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public void deleteById(int id) {
	    try {

	        if (!aplicacionRepository.existsById(id)) {
	            return;
	        }

	        aplicacionRepository.deleteById(id);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
