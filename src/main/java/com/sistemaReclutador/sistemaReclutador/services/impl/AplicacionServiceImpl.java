package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
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
	public Aplicacion crearAplicacion(AplicacionRequest aplicacionRequest) {
		System.out.println("AplicationRequest" + aplicacionRequest.getIdOferta());
		System.out.println("AplicationRequest" + aplicacionRequest.getIdPerfil().getClave());
		System.out.println("AplicationRequest" + aplicacionRequest.getIdPerfil().getId_perfil());

		if (aplicacionRequest.getIdPerfil() == null || aplicacionRequest.getIdPerfil().getId_perfil() == null) {
			throw new IllegalArgumentException("El id_perfil no puede ser null");
		}

		Perfil perfil = perfilRepository.findById(aplicacionRequest.getIdPerfil().getId_perfil())
				.orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

		Oferta oferta = ofertaRepository.findById(aplicacionRequest.getIdOferta().getIdOferta())
				.orElseThrow(() -> new RuntimeException("Oferta no encontrada"));

		boolean existeAplicacion = aplicacionRepository.existsByPerfilAndOferta(perfil.getId_perfil(),
				oferta.getIdOferta());
		if (existeAplicacion) {
			Aplicacion aplicacionNula = new Aplicacion();
			aplicacionNula.setFecha(LocalDateTime.now());
			aplicacionNula.setEstadoaplicaciones(true);
			aplicacionNula.setPerfil(null);
			aplicacionNula.setOferta(null);
			return aplicacionNula;
		}

		Aplicacion aplicacionEntity = new Aplicacion();
		aplicacionEntity.setFecha(LocalDateTime.now());
		aplicacionEntity.setEstadoaplicaciones(true);
		aplicacionEntity.setPerfil(perfil);
		aplicacionEntity.setOferta(oferta);

		return aplicacionRepository.save(aplicacionEntity);
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
