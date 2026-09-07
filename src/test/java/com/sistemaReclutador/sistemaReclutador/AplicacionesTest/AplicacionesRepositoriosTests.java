package com.sistemaReclutador.sistemaReclutador.AplicacionesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.services.AplicacionService;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class AplicacionesRepositoriosTests {

	@Autowired
	private AplicacionService aplicacionService;
	
	@Test
	void crearAplicacion_exito() {
		String valor = "APLICO";
		AplicacionRequest aplicacionRequest = armarRequest(valor);
		AplicacionResponseDTO respuesta = aplicacionService.crearAplicacion(aplicacionRequest);
		assertEquals(ResultadosAplicacion.APLICACION_CREADA, respuesta.getStatus());
	}

	@Test
	void crearAplicacion_yaExiste() {
		String valor = "NOAPLICO";
		AplicacionRequest aplicacionRequest = armarRequest(valor);
		AplicacionResponseDTO respuesta = aplicacionService.crearAplicacion(aplicacionRequest);
		assertEquals(ResultadosAplicacion.YA_APLICO, respuesta.getStatus());
	}

	private AplicacionRequest armarRequest(String valor) {
		AplicacionRequest aplicacion = new AplicacionRequest();

		Perfil perfil = new Perfil();
		Oferta oferta = new Oferta();
		if (valor == "APLICO") {
			perfil.setId_perfil(4);
		} else {
			perfil.setId_perfil(2);
		}
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
