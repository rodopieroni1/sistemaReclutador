package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionesMiasResponse;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.exceptions.ResourceNotFoundException;
import com.sistemaReclutador.sistemaReclutador.repositories.AplicacionRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.AplicacionService;
import com.sistemaReclutador.sistemaReclutador.strategies.AplicacionesMiasStrategy;
import com.sistemaReclutador.sistemaReclutador.validators.ValidacionAplicacionHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AplicacionServiceImpl implements AplicacionService {

	private final AplicacionRepository aplicacionRepository;
	private final PerfilRepository perfilRepository;
	private final OfertaRepository ofertaRepository;
	private final Map<String, AplicacionesMiasStrategy> mappingStrategies;
	private final List<ValidacionAplicacionHandler> validadores;

	@Transactional
	@Override
	public AplicacionResponseDTO crearAplicacion(AplicacionRequest aplicacionRequest) {
	    
	    for (ValidacionAplicacionHandler validador : validadores) {
	        Optional<AplicacionResponseDTO> error = validador.validar(aplicacionRequest);
	        if (error.isPresent()) {
	            return error.get();
	        }
	    }
	    
	    Perfil perfil = perfilRepository.findById(aplicacionRequest.getIdPerfil().getId_perfil())
	            .orElseThrow(() -> new ResourceNotFoundException("No se encontró el perfil con ID: " + aplicacionRequest.getIdPerfil().getId_perfil()));
	            
	    Oferta oferta = ofertaRepository.findById(aplicacionRequest.getIdOferta().getIdOferta())
	            .orElseThrow(() -> new ResourceNotFoundException("No se encontró la oferta con ID: " + aplicacionRequest.getIdOferta().getIdOferta()));

	    return aplicacionRepository.findByPerfilAndOferta(perfil.getId_perfil(), oferta.getIdOferta())
	            .map(this::procesarExistente)
	            .orElseGet(() -> crearNueva(perfil, oferta));
	}

	private AplicacionResponseDTO procesarExistente(Aplicacion aplicacion) {
        if (Boolean.TRUE.equals(aplicacion.isEstadoaplicaciones())) {
            return new AplicacionResponseDTO(ResultadosAplicacion.YA_APLICO, "Ya aplicaste anteriormente a esta oferta.");
        }
        aplicacion.reactivar();
        aplicacionRepository.save(aplicacion);
        return new AplicacionResponseDTO(ResultadosAplicacion.ACTUALIZACION_ESTADO, "Tu postulación fue reactivada correctamente.");
    }
	
	private AplicacionResponseDTO crearNueva(Perfil perfil, Oferta oferta) {
        Aplicacion nueva = new Aplicacion();
        nueva.setFecha(LocalDateTime.now());
        nueva.setEstadoaplicaciones(true);
        nueva.setPerfil(perfil);
        nueva.setOferta(oferta);

        aplicacionRepository.save(nueva);
        return new AplicacionResponseDTO(ResultadosAplicacion.APLICACION_CREADA, "Te postulaste correctamente a la oferta.");
    }
	
	@Transactional(readOnly = true)
	@Override
	public boolean existsById(Integer id) {
		return aplicacionRepository.existsById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<AplicacionesMiasResponse> obtenerAplicacionesPerfil(int idPerfil, String format) {
	    List<Object[]> resultado = aplicacionRepository.obtenerAplicacionesPerfil(idPerfil);
	    AplicacionesMiasStrategy strategy = mappingStrategies.getOrDefault(
	            format, 
	            mappingStrategies.get("standardMappingStrategy")
	    );
	    return resultado.stream()
	            .map(strategy::map)
	            .toList();
	}

	@Transactional
	@Override
	public void cambiarEstado(Integer id, boolean estado) {
	    Aplicacion aplicacion = aplicacionRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No se encontró la aplicación con ID: " + id));
	    
	    aplicacion.setEstadoaplicaciones(estado);
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

	@Transactional
    @Override
    public void deleteById(int id) {
        if (!aplicacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró la aplicación con ID: " + id);
        }
        aplicacionRepository.deleteById(id);
    }
}