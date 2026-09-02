package com.sistemaReclutador.sistemaReclutador.AplicacionesTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemaReclutador.sistemaReclutador.config.JwtUtil;
import com.sistemaReclutador.sistemaReclutador.controllers.AplicacionController;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionesMiasResponse;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.AplicacionService;

@WebMvcTest(
controllers = AplicacionController.class,
excludeAutoConfiguration = {
SecurityAutoConfiguration.class,
SecurityFilterAutoConfiguration.class
}
)
public class AplicacionesControllerTests {

@MockBean
private AplicacionService aplicacionService;

@Autowired
private MockMvc mockMvc;

@MockBean
private JwtUtil jwtUtil;

@MockBean
private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

@Autowired
private ObjectMapper objectMapper;

@MockBean
private PerfilRepository perfilRepository;


// GET /aplicaciones

@Test
void getAllAplicaciones_DebeRetornarLista() throws Exception {

    when(aplicacionService.findAllDesc())
            .thenReturn(List.of(new Aplicacion()));

    mockMvc.perform(get("/aplicaciones"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
}


@Test
void getAllAplicaciones_DebeRetornarListaVacia() throws Exception {

    when(aplicacionService.findAllDesc())
            .thenReturn(List.of());

    mockMvc.perform(get("/aplicaciones"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
}


// GET /aplicaciones/activas

@Test
void getAllAplicacionesActivas_DebeRetornarLista() throws Exception {

    when(aplicacionService.findAllDescActivas())
            .thenReturn(List.of(new Aplicacion()));

    mockMvc.perform(get("/aplicaciones/activas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
}


@Test
void getAllAplicacionesActivas_DebeRetornarListaVacia() throws Exception {

    when(aplicacionService.findAllDescActivas())
            .thenReturn(List.of());

    mockMvc.perform(get("/aplicaciones/activas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
}


// GET /aplicaciones/{id}

@Test
void getAplicacionById_DebeRetornarTrue() throws Exception {

    when(aplicacionService.existsById(1))
            .thenReturn(true);

    mockMvc.perform(get("/aplicaciones/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(true));

    verify(aplicacionService, times(1))
            .existsById(1);
}


@Test
void getAplicacionById_DebeRetornarFalse() throws Exception {

    when(aplicacionService.existsById(999))
            .thenReturn(false);

    mockMvc.perform(get("/aplicaciones/999"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(false));

    verify(aplicacionService, times(1))
            .existsById(999);
}


// PATCH /aplicaciones/estado/{idPost}

@Test
void cambiarEstado_DebeRetornarNoContent() throws Exception {

    Map<String, Boolean> body = Map.of("estado", true);

    mockMvc.perform(
            patch("/aplicaciones/estado/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
    )
    .andExpect(status().isNoContent());

    verify(aplicacionService, times(1))
            .cambiarEstado(1, true);
}


@Test
void cambiarEstado_DebePermitirEstadoFalse() throws Exception {

    Map<String, Boolean> body = Map.of("estado", false);

    mockMvc.perform(
            patch("/aplicaciones/estado/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
    )
    .andExpect(status().isNoContent());

    verify(aplicacionService, times(1))
            .cambiarEstado(1, false);
}


// DELETE /aplicaciones/{id}

@Test
void deleteAplicacion_DebeRetornarNoContent() throws Exception {

    doNothing().when(aplicacionService)
            .deleteById(1);

    mockMvc.perform(delete("/aplicaciones/1"))
            .andExpect(status().isNoContent());

    verify(aplicacionService, times(1))
            .deleteById(1);
}


// GET /aplicaciones/perfil/{idPerfil}

@Test
void obtenerAsignaciones_DebeRetornarOk() throws Exception {

    AplicacionesMiasResponse respuesta = mock(AplicacionesMiasResponse.class);

    when(aplicacionService.obtenerAplicacionesPerfil(
            24,
            "standardMappingStrategy"
    )).thenReturn(List.of(respuesta));

    mockMvc.perform(get("/aplicaciones/perfil/24"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));

    verify(aplicacionService, times(1))
            .obtenerAplicacionesPerfil(
                24,
                "standardMappingStrategy"
            );
}


@Test
void obtenerAsignaciones_DebeRetornarListaVacia() throws Exception {

    when(aplicacionService.obtenerAplicacionesPerfil(
            24,
            "standardMappingStrategy"
    )).thenReturn(List.of());

    mockMvc.perform(get("/aplicaciones/perfil/24"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
}


@Test
void obtenerAsignaciones_DebeAceptarOtroFormato() throws Exception {

    AplicacionesMiasResponse respuesta = mock(AplicacionesMiasResponse.class);

    when(aplicacionService.obtenerAplicacionesPerfil(
            24,
            "otroFormato"
    )).thenReturn(List.of(respuesta));

    mockMvc.perform(
            get("/aplicaciones/perfil/24")
                .param("format", "otroFormato")
    )
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.length()").value(1));

    verify(aplicacionService, times(1))
            .obtenerAplicacionesPerfil(
                24,
                "otroFormato"
            );
}


}