package com.sistemaReclutador.sistemaReclutador.OfertaTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.controllers.OfertaController;
import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.dto.OfertaUpdateRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
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
	@MockBean
    private PerfilRepository perfilRepository;

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
	void createOferta_DebeRetornarResponse() throws Exception {
	    OfertaRequest ofertarequest = new OfertaRequest();
	    ofertarequest.setNombreOferta("Desarrollador Backend");
	    ofertarequest.setDescripcionOferta("Experiencia en Spring Boot");
	    ofertarequest.setEstadoOferta(true);
	    ofertarequest.setFotoOferta("foto.png");
	    ofertarequest.setIdEmpresa(1L);
	    Oferta ofertaSimulada = new Oferta();
	    ofertaSimulada.setIdOferta(100L);
	    ofertaSimulada.setNombreOferta(ofertarequest.getNombreOferta());
	    Mockito.when(ofertaService.saveOferta(any(OfertaRequest.class))).thenReturn(ofertaSimulada);
	    mockMvc.perform(multipart("/ofertas/crear")
	            .param("nombreOferta", "Desarrollador Backend")
	            .param("descripcionOferta", "Experiencia en Spring Boot")
	            .param("estadoOferta", "true")
	            .param("fotoOferta", "foto.png")
	            .param("idEmpresa", "1"))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.success").value(true))
	            .andExpect(jsonPath("$.message").value("Oferta creada satisfactoriamente"))
	            .andExpect(jsonPath("$.data.nombreOferta").value("Desarrollador Backend"))
	            .andExpect(jsonPath("$.data.idOferta").value(100));
	}

	
	@Test
	void updateOferta_DebeRetornarResponse() throws Exception {
	    Long idOferta = 100L;
	    Oferta ofertaSimulada = new Oferta();
	    ofertaSimulada.setIdOferta(idOferta);
	    ofertaSimulada.setNombreOferta("Desarrollador Senior Backend");

	    Mockito.when(ofertaService.updateOferta(eq(idOferta), any(OfertaUpdateRequest.class)))
	            .thenReturn(ofertaSimulada);

	    MockMultipartHttpServletRequestBuilder builder = 
	            MockMvcRequestBuilders.multipart("/ofertas/actualizar/{id}", idOferta);
	    
	    builder.with(request -> {
	        request.setMethod("PUT");
	        return request;
	    });

	    mockMvc.perform(builder
	            .param("nombreOferta", "Desarrollador Senior Backend")
	            .param("descripcionOferta", "Experiencia avanzada en Java")
	            .param("estadoOferta", "false")
	            // Agrega aquí los campos obligatorios faltantes (ejemplos comunes):
	            .param("idEmpresa", "1")
	            .param("idRubro", "1")
	            .param("salario", "150000"))
	            .andDo(print()) // Te mostrará en la consola el error exacto de validación si vuelve a dar 400
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.success").value(true))
	            .andExpect(jsonPath("$.message").value("Oferta actualizada satisfactoriamente"))
	            .andExpect(jsonPath("$.data.idOferta").value(100))
	            .andExpect(jsonPath("$.data.nombreOferta").value("Desarrollador Senior Backend"));
	}
	
	@Test
	void eliminarOferta_DebeRetornarResponse() throws Exception {
	    Long idOferta = 100L;
	    Mockito.doNothing().when(ofertaService).eliminarOferta(idOferta);
	    mockMvc.perform(delete("/ofertas/eliminar/{id}", idOferta)
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.success").value(true))
	            .andExpect(jsonPath("$.message").value("Oferta eliminada satisfactoriamente"));
	}
	
}
