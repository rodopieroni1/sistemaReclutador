package com.sistemaReclutador.sistemaReclutador.AplicacionesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.repositories.AplicacionRepository;
import com.sistemaReclutador.sistemaReclutador.services.AplicacionService;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class AplicacionesRepositoriosTests {

	@Autowired
	private AplicacionService aplicacionService;
	@Autowired
	private AplicacionRepository aplicationRepository;

	@Test
	void crearAplicacion_test() {
		AplicacionRequest aplicacionRequest = armarRequest();
		AplicacionResponseDTO aplicacionRespuesta = aplicacionService.crearAplicacion(aplicacionRequest);

		assertEquals(ResultadosAplicacion.APLICACION_CREADA, aplicacionRespuesta.getStatus());
		assertEquals("Te postulaste correctamente a la oferta.", aplicacionRespuesta.getMensaje());
	}

	@Test
	void crearAplicacion_existeByIdPerfil() {		
	    AplicacionRequest aplicacionRequest = armarRequest();

	    Optional<Aplicacion> aplicacionOpt =
	        aplicationRepository.findByPerfilAndOferta(
	            aplicacionRequest.getIdPerfil().getId_perfil(),
	            aplicacionRequest.getIdOferta().getIdOferta()
	        );

	    assertEquals(aplicacionOpt.isPresent(), true);
	    assertTrue(aplicacionOpt.isPresent(), "La aplicación debería existir en la base de datos");
	}
	

	private AplicacionRequest armarRequest() {
		AplicacionRequest aplicacion = new AplicacionRequest();

		Perfil perfil = new Perfil();
		Oferta oferta = new Oferta();

		perfil.setId_perfil(2);
		perfil.setNombre("Juan PérezAR");
		perfil.setClave("password123AR");
		perfil.setDni("35123456AR");
		perfil.setEmail("juan.perez@gmail.comAR");
		perfil.setDireccion("Av. Siempreviva 742AR");
		perfil.setDocumentoUrl("http://ejemplo.comAR");
		perfil.setFotoUrl("http://ejemplo.comAR");

		oferta.setIdOferta(10L);
		oferta.setNombreOferta("Desarrollador Java SeniorAR");
		oferta.setDescripcionOferta(
				"Búsqueda orientada a profesionales con más de 5 años de experiencia en Spring Boot.AR");
		oferta.setFotoOferta("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAR");
		oferta.setEstadoOferta(true);

		aplicacion.setFechaAplicacion(LocalDateTime.of(2026, java.time.Month.JULY, 23, 10, 30, 0));
		aplicacion.setEstadoaplicaciones(false);
		aplicacion.setIdPerfil(perfil);
		aplicacion.setIdOferta(oferta);
		return aplicacion;
	}

}
