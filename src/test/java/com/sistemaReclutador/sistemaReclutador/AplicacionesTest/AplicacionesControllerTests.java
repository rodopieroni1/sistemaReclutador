package com.sistemaReclutador.sistemaReclutador.AplicacionesTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.controllers.AplicacionController;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.AplicacionService;

@WebMvcTest(controllers = AplicacionController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class,
		SecurityFilterAutoConfiguration.class })
public class AplicacionesControllerTests {

	
	@MockBean
	private AplicacionService aplicacionService;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private JwtUtil jwtUtil;
	@MockBean
	private UserDetailsService userDetailsService;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private PerfilRepository perfilRepository;

	//si busco una lista y trae algo esta bien, si trae cero esta mal
	@Test
	void getAllAplicacionesActivas_DebeRetornarLista() throws Exception {
		Mockito.when(aplicacionService.findAllDescActivas()).thenReturn(List.of(new Aplicacion()));
		mockMvc.perform(get("/aplicaciones/activas")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	void cambiarEstado_DebeRetornarOk() throws Exception {
		Map<String, Boolean> body = Map.of("estado", true);
		mockMvc.perform(patch("/aplicaciones/estado/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(body))).andExpect(status().isOk());
		Mockito.verify(aplicacionService, Mockito.times(1)).cambiarEstado(1, true);
	}

	@Test
	void obtenerAsignaciones_DebeRetornarOk() throws Exception {
		Object[] filaMock = new Object[] { 24, // idaplicacion (Integer)
				"Puesto de Prueba", // puesto / nombreOferta (String)
				"Empresa S.A.", // empresa (String)
				java.time.LocalDateTime.now(), // fecha (LocalDateTime)
				true, // estado (Boolean)
				"Descripción", // descripcionOferta (String)
				"foto.png", // fotoOferta (String)
				"test@empresa.com", // email (String)
				"123456789", // telefono (String)
				"Calle Falsa 123", // direccion (String)
				100L // idOferta (Long)
		};
		List<Object[]> resultadoMock = new java.util.ArrayList<>();
		resultadoMock.add(filaMock);
		
		Mockito.when(aplicacionService.obtenerAplicacionesPerfil(24)).thenReturn(resultadoMock);
		mockMvc.perform(get("/aplicaciones/perfil/24")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
	}

}
