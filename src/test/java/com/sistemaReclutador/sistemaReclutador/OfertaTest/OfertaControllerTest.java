package com.sistemaReclutador.sistemaReclutador.OfertaTest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.controllers.OfertaController;
import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.OfertaService;

@WebMvcTest(controllers = OfertaController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class,
		SecurityFilterAutoConfiguration.class })
public class OfertaControllerTest {

	@MockBean
	private OfertaService ofertaService;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private JwtUtil jwtUtil;
	@MockBean
	private UserDetailsService userDetailsService;
	@Autowired
	private ObjectMapper objectMapper;

	private Rubro requestRubro;
	private Empresa requestEmpresa;

	@BeforeEach
	void setUp() {
		Empresa empresa = new Empresa();

		Oferta oferta1 = new Oferta();
		oferta1.setNombreOferta("Desarrollador Java Senior");
		oferta1.setDescripcionOferta("Búsqueda para cubrir puesto de backend con Spring Boot.");
		oferta1.setEstadoOferta(true);
		oferta1.setFotoOferta("url_foto_java.png");
		oferta1.setEmpresa(empresa);

		Oferta oferta2 = new Oferta();
		oferta2.setNombreOferta("Diseñador UX/UI");
		oferta2.setDescripcionOferta("Búsqueda orientada a perfiles con experiencia en Figma.");
		oferta2.setEstadoOferta(false);
		oferta2.setFotoOferta("url_foto_ux.png");
		oferta2.setEmpresa(empresa);
	}

	@Test
	void getAllOfertas_DebeRetornarLista() throws Exception {
		Mockito.when(ofertaService.findAllOfertas()).thenReturn(List.of(new Oferta()));
		mockMvc.perform(get("/ofertas/disponibles")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	void getAllOfertasDesc_DebeRetornarLista() throws Exception {
		Mockito.when(ofertaService.findAllOfertasActivas()).thenReturn(List.of(new Oferta()));
		mockMvc.perform(get("/ofertas/todas")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	void createOferta_DebeRetornarResponse() throws Exception {
		requestRubro = new Rubro();
		requestRubro.setIdRubro(1);
		requestRubro.setDescripcionRubro("Educacion");

		requestEmpresa = new Empresa();
		requestEmpresa.setId_empresa(1L);
		requestEmpresa.setNombre("Conesa");
		requestEmpresa.setDireccion("Formosa 2020");
		requestEmpresa.setHistoriaEmpresa("empresa dedicada a la compra y venta textil");
		requestEmpresa.setObservaciones("Nadaaaaa");
		requestEmpresa.setCuit(31111120828L);
		requestEmpresa.setLogo("logoSimulado");
		requestEmpresa.setEmail("piche@hotmail.com");
		requestEmpresa.setTelefono("3854177555");
		requestEmpresa.setRubro(requestRubro);

		OfertaRequest ofertarequest = new OfertaRequest();
		ofertarequest.setNombreOferta("Desarrollador Backend");
		ofertarequest.setDescripcionOferta("Experiencia en Spring Boot");
		ofertarequest.setEstadoOferta(true);
		ofertarequest.setFotoOferta("foto.png");
		ofertarequest.setIdEmpresa(requestEmpresa);

		Oferta ofertaSimulada = new Oferta();
		ofertaSimulada.setIdOferta(100L);
		ofertaSimulada.setNombreOferta(ofertarequest.getNombreOferta());

		ResponseRest<Oferta> responseRest = new ResponseRest<>(true, "Oferta creada con éxito", ofertaSimulada,
				LocalDateTime.now(), "200");
		ResponseEntity<ResponseRest<Oferta>> responseEntity = new ResponseEntity<>(responseRest, HttpStatus.CREATED);
		
		Mockito.when(ofertaService.saveOferta(any(OfertaRequest.class))).thenReturn(responseEntity);
		mockMvc.perform(post("/ofertas/crear").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ofertarequest))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Oferta creada con éxito"))
				.andExpect(jsonPath("$.data.nombreOferta").value("Desarrollador Backend"))
				.andExpect(jsonPath("$.data.idOferta").value(100));

	}

	
	@Test
	void actualizarOferta_DebeRetornarResponse() throws Exception {
		Oferta ofertaSimulada = new Oferta();
		ofertaSimulada.setIdOferta(100L);
		ofertaSimulada.setNombreOferta("ofertarequest.getNombreOferta()");

		ResponseRest<Oferta> responseRest = new ResponseRest<>(true, "Oferta actualizada satisfactoriamente",
				ofertaSimulada, LocalDateTime.now(), "200");
		ResponseEntity<ResponseRest<Oferta>> responseEntity = new ResponseEntity<>(responseRest, HttpStatus.CREATED);
		Mockito.when(ofertaService.updateOferta(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
				.thenReturn(responseEntity);

		MockMultipartFile archivoMock = new MockMultipartFile("fotoArchivo",
				"foto.png",
				MediaType.IMAGE_PNG_VALUE,
				"contenido-de-imagen".getBytes()
		);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/ofertas/actualizar/100").file(archivoMock).param("id", "100")
				.param("nombreOferta", "Desarrollador Backend").param("descripcionOferta", "Experiencia en Spring Boot")
				.param("estadoOferta", "true").param("idEmpresa", "1").param("fotoOferta", "foto.png").with(request -> {
					request.setMethod("PUT");
					return request;
				})).andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Oferta actualizada satisfactoriamente"))
				.andExpect(jsonPath("$.data.idOferta").value(100));

	}
	
	@Test
	void eliminarOferta_DebeRetornarResponse() throws Exception {
	    // 1. ARRANGE (Preparación)
		Oferta ofertaSimulada = new Oferta();
		ofertaSimulada.setIdOferta(100L);
		ofertaSimulada.setNombreOferta("ofertarequest.getNombreOferta()");
	    // Construimos la respuesta que debería devolver el servicio
		ResponseRest<Oferta> responseRest = new ResponseRest<>(true, "Oferta eliminada satisfactoriamente",
				ofertaSimulada, LocalDateTime.now(), "200");
	    ResponseEntity<ResponseRest<Oferta>> responseEntity = new ResponseEntity<>(responseRest, HttpStatus.OK);
	    // Indicarle a Mockito qué hacer cuando el controlador llame al servicio
	    Mockito.when(ofertaService.eliminarOferta(100L)).thenReturn(responseEntity);
	    // 2. ACT & ASSERT (Acción y Verificación)
	    mockMvc.perform(delete("/ofertas/eliminar/100")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.success").value(true))
	            .andExpect(jsonPath("$.message").value("Oferta eliminada satisfactoriamente"))
	            .andExpect(jsonPath("$.data.idOferta").value(100));
		
	}

	
}
