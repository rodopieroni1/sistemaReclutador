package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	@Override
	public AplicacionResponseDTO crearAplicacion(AplicacionRequest aplicacionRequest) {

		if (aplicacionRequest.getIdPerfil() == null || aplicacionRequest.getIdPerfil().getId_perfil() == null) {
			return new AplicacionResponseDTO(ResultadosAplicacion.PERFIL_INVALIDO, "Debe indicar un perfil válido.");
		}
		
		Optional<Perfil> perfilOpt = perfilRepository.findById(aplicacionRequest.getIdPerfil().getId_perfil());
		
		if (perfilOpt.isEmpty()) {
			return new AplicacionResponseDTO(ResultadosAplicacion.PERFIL_NO_ENCONTRADO,
					"El perfil indicado no existe.");
		}
		
		if (aplicacionRequest.getIdOferta() == null || aplicacionRequest.getIdOferta().getIdOferta() == null) {

			return new AplicacionResponseDTO(ResultadosAplicacion.OFERTA_INVALIDA, "Debe indicar una oferta válida.");
		}
		
		Optional<Oferta> ofertaOpt = ofertaRepository.findById(aplicacionRequest.getIdOferta().getIdOferta());
		if (ofertaOpt.isEmpty()) {
			return new AplicacionResponseDTO(ResultadosAplicacion.OFERTA_NO_ENCONTRADA,
					"La oferta indicada no existe.");
		}

		Perfil perfil = perfilOpt.get();
		Oferta oferta = ofertaOpt.get();

		Optional<Aplicacion> aplicacionOpt = aplicacionRepository.findByPerfilAndOferta(perfil, oferta);

		if (aplicacionOpt.isPresent()) {
			Aplicacion aplicacionExistenteRecuperada = aplicacionOpt.get();

			// CASO 1: Ya existía y el estado en base de datos ya es true (1)
			if (Boolean.TRUE.equals(aplicacionExistenteRecuperada.isEstadoaplicaciones())) {
				return new AplicacionResponseDTO(ResultadosAplicacion.YA_APLICO,
						"Ya aplicaste anteriormente a esta oferta.");
			}

			// CASO 2: Ya existía pero el estado estaba en false (0). La reactivamos.
			aplicacionExistenteRecuperada.setEstadoaplicaciones(true);
			aplicacionExistenteRecuperada.setFecha(LocalDateTime.now());

			// Guardamos los cambios en la base de datos
			aplicacionRepository.save(aplicacionExistenteRecuperada);
			return new AplicacionResponseDTO(ResultadosAplicacion.ACTUALIZACION_ESTADO,
					"Tu postulación fue reactivada correctamente.");
		}

		// CASO 3: Si no existía, la creamos desde cero como nueva postulación
		Aplicacion aplicacionEntity = new Aplicacion();
		aplicacionEntity.setFecha(LocalDateTime.now());
		aplicacionEntity.setEstadoaplicaciones(true);
		aplicacionEntity.setPerfil(perfil);
		aplicacionEntity.setOferta(oferta);

		aplicacionRepository.save(aplicacionEntity);
		return new AplicacionResponseDTO(ResultadosAplicacion.APLICACION_CREADA,
				"Te postulaste correctamente a la oferta.");
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
