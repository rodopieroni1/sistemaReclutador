package com.sistemaReclutador.sistemaReclutador.AplicacionesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.exceptions.ResourceNotFoundException;
import com.sistemaReclutador.sistemaReclutador.repositories.AplicacionRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.impl.AplicacionServiceImpl;
import com.sistemaReclutador.sistemaReclutador.strategies.AplicacionesMiasStrategy;
import com.sistemaReclutador.sistemaReclutador.validators.ValidacionAplicacionHandler;

@ExtendWith(MockitoExtension.class)
public class AplicacionServiceTests {

	@Mock
	private AplicacionRepository aplicacionRepository;

	@Mock
	private PerfilRepository perfilRepository;

	@Mock
	private OfertaRepository ofertaRepository;

	@Mock
	private Map<String, AplicacionesMiasStrategy> mappingStrategies;

	@Mock
	private ValidacionAplicacionHandler validador;

	private AplicacionServiceImpl aplicacionService;

	@BeforeEach
	void setUp() {

		aplicacionService = new AplicacionServiceImpl(aplicacionRepository, perfilRepository, ofertaRepository,
				mappingStrategies, List.of(validador));
	}

// CREAR NUEVA APLICACION

	@Test
	void crearAplicacion_CuandoNoExiste_DebeCrearNueva() {

		AplicacionRequest request = armarRequest();

		Perfil perfil = request.getIdPerfil();
		Oferta oferta = request.getIdOferta();
		when(validador.validar(request)).thenReturn(Optional.empty());
		when(perfilRepository.findById(perfil.getId_perfil())).thenReturn(Optional.of(perfil));
		when(ofertaRepository.findById(oferta.getIdOferta())).thenReturn(Optional.of(oferta));
		when(aplicacionRepository.findByPerfilAndOferta(perfil.getId_perfil(), oferta.getIdOferta()))
				.thenReturn(Optional.empty());
		when(aplicacionRepository.save(any(Aplicacion.class))).thenAnswer(invocation -> invocation.getArgument(0));
		AplicacionResponseDTO respuesta = aplicacionService.crearAplicacion(request);
		assertEquals(ResultadosAplicacion.APLICACION_CREADA, respuesta.getStatus());
		assertEquals("Te postulaste correctamente a la oferta.", respuesta.getMensaje());
		verify(aplicacionRepository).save(any(Aplicacion.class));
	}

// APLICACION EXISTENTE Y ACTIVA

	@Test
	void crearAplicacion_CuandoYaExisteActiva_DebeRetornarYaAplico() {
		AplicacionRequest request = armarRequest();
		Perfil perfil = request.getIdPerfil();
		Oferta oferta = request.getIdOferta();
		Aplicacion existente = new Aplicacion();
		existente.setEstadoaplicaciones(true);
		when(validador.validar(request)).thenReturn(Optional.empty());
		when(perfilRepository.findById(perfil.getId_perfil())).thenReturn(Optional.of(perfil));
		when(ofertaRepository.findById(oferta.getIdOferta())).thenReturn(Optional.of(oferta));
		when(aplicacionRepository.findByPerfilAndOferta(perfil.getId_perfil(), oferta.getIdOferta()))
				.thenReturn(Optional.of(existente));
		AplicacionResponseDTO respuesta = aplicacionService.crearAplicacion(request);
		assertEquals(ResultadosAplicacion.YA_APLICO, respuesta.getStatus());

		assertEquals("Ya aplicaste anteriormente a esta oferta.", respuesta.getMensaje());

		verify(aplicacionRepository, never()).save(any(Aplicacion.class));
	}

// APLICACION EXISTENTE E INACTIVA

	@Test
	void crearAplicacion_CuandoExisteInactiva_DebeReactivarla() {

		AplicacionRequest request = armarRequest();

		Perfil perfil = request.getIdPerfil();
		Oferta oferta = request.getIdOferta();
		Aplicacion existente = new Aplicacion();
		existente.setEstadoaplicaciones(false);
		when(validador.validar(request)).thenReturn(Optional.empty());
		when(perfilRepository.findById(perfil.getId_perfil())).thenReturn(Optional.of(perfil));
		when(ofertaRepository.findById(oferta.getIdOferta())).thenReturn(Optional.of(oferta));
		when(aplicacionRepository.findByPerfilAndOferta(perfil.getId_perfil(), oferta.getIdOferta()))
				.thenReturn(Optional.of(existente));
		when(aplicacionRepository.save(any(Aplicacion.class))).thenAnswer(invocation -> invocation.getArgument(0));
		AplicacionResponseDTO respuesta = aplicacionService.crearAplicacion(request);
		assertEquals(ResultadosAplicacion.ACTUALIZACION_ESTADO, respuesta.getStatus());
		assertEquals("Tu postulación fue reactivada correctamente.", respuesta.getMensaje());
		assertTrue(existente.isEstadoaplicaciones());
		verify(aplicacionRepository).save(existente);
	}

// PERFIL INEXISTENTE

	@Test
	void crearAplicacion_CuandoPerfilNoExiste_DebeLanzarExcepcion() {

		AplicacionRequest request = armarRequest();
		Integer idPerfil = request.getIdPerfil().getId_perfil();
		when(validador.validar(request)).thenReturn(Optional.empty());
		when(perfilRepository.findById(idPerfil)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> aplicacionService.crearAplicacion(request));
		verify(perfilRepository).findById(idPerfil);
		verifyNoInteractions(ofertaRepository);
		verifyNoInteractions(aplicacionRepository);
	}

// OFERTA INEXISTENTE

	@Test
	void crearAplicacion_CuandoOfertaNoExiste_DebeLanzarExcepcion() {

		AplicacionRequest request = armarRequest();
		Perfil perfil = request.getIdPerfil();
		Long idOferta = request.getIdOferta().getIdOferta();
		when(validador.validar(request)).thenReturn(Optional.empty());
		when(perfilRepository.findById(perfil.getId_perfil())).thenReturn(Optional.of(perfil));
		when(ofertaRepository.findById(idOferta)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> aplicacionService.crearAplicacion(request));
		verify(perfilRepository).findById(perfil.getId_perfil());
		verify(ofertaRepository).findById(idOferta);
		verifyNoInteractions(aplicacionRepository);
	}

// VALIDADOR DEVUELVE ERROR

	@Test
	void crearAplicacion_CuandoValidadorDevuelveError_DebeRetornarError() {

		AplicacionRequest request = armarRequest();
		AplicacionResponseDTO error = new AplicacionResponseDTO(ResultadosAplicacion.YA_APLICO,
				"Ya aplicaste anteriormente a esta oferta.");
		when(validador.validar(request)).thenReturn(Optional.of(error));
		AplicacionResponseDTO respuesta = aplicacionService.crearAplicacion(request);
		assertEquals(ResultadosAplicacion.YA_APLICO, respuesta.getStatus());
		assertEquals("Ya aplicaste anteriormente a esta oferta.", respuesta.getMensaje());
		verify(validador).validar(request);
		verifyNoInteractions(perfilRepository);
		verifyNoInteractions(ofertaRepository);
		verifyNoInteractions(aplicacionRepository);
	}

// REQUEST DE PRUEBA

	private AplicacionRequest armarRequest() {

		AplicacionRequest request = new AplicacionRequest();

		Perfil perfil = new Perfil();
		Oferta oferta = new Oferta();

		perfil.setId_perfil(22);
		perfil.setNombre("Juan Pérez TEST");
		perfil.setClave("password123TEST");
		perfil.setDni("35123456TEST");
		perfil.setEmail("juan.test@gmail.com");
		perfil.setDireccion("Av. Siempreviva 742");
		perfil.setDocumentoUrl("http://ejemplo.com/documento");
		perfil.setFotoUrl("http://ejemplo.com/foto");

		oferta.setIdOferta(2L);
		oferta.setNombreOferta("Desarrollador Java Senior TEST");
		oferta.setDescripcionOferta("Oferta de prueba para test unitario.");
		oferta.setFotoOferta("foto.png");
		oferta.setEstadoOferta(true);

		request.setIdPerfil(perfil);
		request.setIdOferta(oferta);

		return request;
	}

}
