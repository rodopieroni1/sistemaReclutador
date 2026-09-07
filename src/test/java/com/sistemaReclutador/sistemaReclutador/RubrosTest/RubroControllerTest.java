package com.sistemaReclutador.sistemaReclutador.RubrosTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sistemaReclutador.sistemaReclutador.controllers.RubroController;
import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
public class RubroControllerTest {

    private MockMvc mockMvc; 

    @Mock
    private RubroService rubroService; 

    @InjectMocks
    private RubroController rubroController; 

    private ObjectMapper objectMapper;
    private RubroRequest requestValido;
    private Rubro rubroSimulado;

    @BeforeEach
    void setUp() {
        // Configuramos el validador explícitamente para que @Valid funcione en standaloneSetup
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(rubroController)
                .setValidator(validator)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        requestValido = new RubroRequest();
        requestValido.setDescripcionRubro("Administración");

        rubroSimulado = new Rubro();
        rubroSimulado.setIdRubro(1);
        rubroSimulado.setDescripcionRubro("Administración");
    }

    @Test
    void crearRubro_DeberiaRetornarStatusCreated() throws Exception {
        // CORRECCIÓN: El servicio devuelve solo Rubro
        when(rubroService.crearRubro(any(RubroRequest.class))).thenReturn(rubroSimulado);

        mockMvc.perform(post("/rubro/crear") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rubro creado satisfactoriamente"))
                .andExpect(jsonPath("$.data.descripcionRubro").value("Administración"));
    }

    @Test
    void actualizarRubro_DeberiaRetornarStatusOk() throws Exception {
        // CORRECCIÓN: El servicio devuelve solo Rubro
        when(rubroService.actualizarRubro(eq(1), any(RubroRequest.class))).thenReturn(rubroSimulado);

        mockMvc.perform(put("/rubro/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rubro modificado satisfactoriamente"));
    }

    @Test
    void crearRubro_CuandoFallaValidacion_DeberiaRetornarBadRequest() throws Exception {
        requestValido.setDescripcionRubro(""); 
        mockMvc.perform(post("/rubro/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isBadRequest());
    }
}