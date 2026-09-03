package com.sistemaReclutador.sistemaReclutador.EmpresaTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.controllers.EmpresaController;
import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;

@WebMvcTest(EmpresaController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmpresaControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private EmpresaService empresaService;
	@MockBean
	private JwtUtil jwtUtil;
	@MockBean
	private PerfilRepository perfilRepository;

	@Test
	void listarEmpresas_DevuelveResponseRestConLista() throws Exception {
		Empresa empresa = new Empresa();
		empresa.setId_empresa(1L);
		empresa.setNombre("Tech Corp");

		when(empresaService.buscarPorEmpresa()).thenReturn(List.of(empresa));

		mockMvc.perform(get("/empresas")).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Lista de empresas obtenida correctamente"))
				.andExpect(jsonPath("$.data[0].nombre").value("Tech Corp"));
	}

	@Test
	void obtenerEmpresaPorId_DevuelveEmpresa() throws Exception {
		Empresa empresa = new Empresa();
		empresa.setId_empresa(1L);

		when(empresaService.findEmpresa(1L)).thenReturn(empresa);

		mockMvc.perform(get("/empresas/1")).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Empresa encontrada"))
				.andExpect(jsonPath("$.data.id_empresa").value(1));
	}

	@Test
	void crearEmpresa_Multipart_Retorna201() throws Exception {
	    Empresa empresa = new Empresa();
	    empresa.setId_empresa(1L);
	    empresa.setNombre("Nueva Empresa");

	    when(empresaService.saveEmpresa(any(EmpresaRequest.class))).thenReturn(empresa);
	    MockMultipartFile logo = new MockMultipartFile("logo", "logo.png", "image/png", "bytes".getBytes());

	    mockMvc.perform(multipart("/empresas")
	    		.file(logo)
	            .param("nombre", "Nueva Empresa")
	            .param("cuit", "20300000000")
	            .param("email", "contacto@empresa.com")
	            .param("idRubro", "1"))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.success").value(true))
	            .andExpect(jsonPath("$.errorCode").value("201"))
	            .andExpect(jsonPath("$.data.nombre").value("Nueva Empresa"));
	}

	@Test
	void eliminarEmpresa_Retorna200() throws Exception {
		doNothing().when(empresaService).deleteEmpresa(1L);

		mockMvc.perform(delete("/empresas/1")).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Empresa eliminada satisfactoriamente"));
	}
}