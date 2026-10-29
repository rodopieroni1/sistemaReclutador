package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;
import com.sistemaReclutador.sistemaReclutador.dto.*;
import com.sistemaReclutador.sistemaReclutador.entities.*;
import com.sistemaReclutador.sistemaReclutador.exceptions.ResourceNotFoundException;
import com.sistemaReclutador.sistemaReclutador.factory.AplicacionFactory;
import com.sistemaReclutador.sistemaReclutador.mensajeria.NotificacionProductor;
import com.sistemaReclutador.sistemaReclutador.repositories.AplicacionRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.AplicacionService;
import com.sistemaReclutador.sistemaReclutador.strategies.AplicacionesMiasStrategy;
import com.sistemaReclutador.sistemaReclutador.validators.ValidacionAplicacionHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AplicacionServiceImpl implements AplicacionService {

	private final AplicacionRepository aplicacionRepository;
	private final PerfilRepository perfilRepository;
	private final OfertaRepository ofertaRepository;
	private final Map<String, AplicacionesMiasStrategy> mappingStrategies;
	private final List<ValidacionAplicacionHandler> validadores;
	private final AplicacionFactory aplicacionFactory;
	private final NotificacionProductor notificacionProductor;

	@Transactional
	@Override
	public AplicacionResponseDTO crearAplicacion(AplicacionRequest aplicacionRequest) {
		for (ValidacionAplicacionHandler validador : validadores) {
			Optional<AplicacionResponseDTO> error = validador.validar(aplicacionRequest);
			if (error.isPresent()) {
				return error.get();
			}
		}
		Perfil perfil = perfilRepository.findById(aplicacionRequest.getIdPerfil().getIdPerfil())
				.orElseThrow(() -> new ResourceNotFoundException(
						"No se encontró el perfil con ID: " + aplicacionRequest.getIdPerfil().getIdPerfil()));

		Oferta oferta = ofertaRepository.findById(aplicacionRequest.getIdOferta().getIdOferta())
				.orElseThrow(() -> new ResourceNotFoundException(
						"No se encontró la oferta con ID: " + aplicacionRequest.getIdOferta().getIdOferta()));
		// Validar nuevamente el estado en el backend
		if (!Boolean.TRUE.equals(oferta.isEstadoOferta())) {
			return new AplicacionResponseDTO(ResultadosAplicacion.OFERTA_INACTIVA,
					"Esta oferta ya no se encuentra activa y no admite nuevas postulaciones.");
		}
		return aplicacionRepository.findByPerfilAndOferta(perfil.getIdPerfil(), oferta.getIdOferta())
				.map(this::procesarExistente).orElseGet(() -> crearNueva(perfil, oferta));
	}

	private AplicacionResponseDTO procesarExistente(Aplicacion aplicacion) {
		if (Boolean.TRUE.equals(aplicacion.isEstadoAplicaciones())) {
			return new AplicacionResponseDTO(ResultadosAplicacion.YA_APLICO,
					"Ya aplicaste anteriormente a esta oferta.");
		}
		aplicacion.reactivar();
		aplicacionRepository.save(aplicacion);
		return new AplicacionResponseDTO(ResultadosAplicacion.ACTUALIZACION_ESTADO,
				"Tu postulación fue reactivada correctamente.");
	}

	private AplicacionResponseDTO crearNueva(Perfil perfil, Oferta oferta) {
		Aplicacion nueva = aplicacionFactory.crear(perfil, oferta);
		aplicacionRepository.save(nueva);
		if (oferta.getUsuario() == null || oferta.getUsuario().getEmail() == null
				|| oferta.getUsuario().getEmail().isBlank()) {
			log.warn("No se puede notificar: la oferta {} no tiene un reclutador con email.", oferta.getIdOferta());
		} else {
			String destinatario = oferta.getUsuario().getEmail();
			String mensaje = "Se recibió una nueva postulación.\n" + "Oferta: " + oferta.getNombreOferta() + "\n"
					+ "Postulante: " + perfil.getNombre();
			//notificacionProductor.enviarMensaje(destinatario, mensaje);
		}
		return new AplicacionResponseDTO(ResultadosAplicacion.APLICACION_CREADA,
				"Te postulaste correctamente a la oferta.");
	}

	@Transactional(readOnly = true)
	@Override
	public boolean existsById(Long id) {
		return aplicacionRepository.existsById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<AplicacionesMiasResponse> obtenerAplicacionesPerfil(int idPerfil, String format) {
		List<Object[]> resultado = aplicacionRepository.obtenerAplicacionesPerfil(idPerfil);
		AplicacionesMiasStrategy strategy = mappingStrategies.getOrDefault(format,
				mappingStrategies.get("standardMappingStrategy"));
		return resultado.stream().map(strategy::map).toList();
	}

	@Transactional
	@Override
	public void cambiarEstado(Long id, boolean estado) {
		Aplicacion aplicacion = aplicacionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró la aplicación con ID: " + id));

		aplicacion.setEstadoAplicaciones(estado);
		aplicacionRepository.save(aplicacion);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Aplicacion> findAllDesc() {
		return aplicacionRepository.findAllDesc();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Aplicacion> findAllDescActivas() {
		return aplicacionRepository.findAllDescActivas();
	}

	@Transactional(readOnly = true)
	@Override
	public List<String> findByPerfilId(Integer id) {
		return aplicacionRepository.obtenerNombreAplicacionesPerfil(id);
	}

	@Transactional
	@Override
	public void deleteById(Long id) {
		if (!aplicacionRepository.existsById(id)) {
			throw new ResourceNotFoundException("No se encontró la aplicación con ID: " + id);
		}
		aplicacionRepository.deleteById(id);
	}
}