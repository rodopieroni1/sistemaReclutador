package com.sistemaReclutador.sistemaReclutador.RubrosTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sistemaReclutador.sistemaReclutador.controllers.RubroController;
import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class) // 💡 Cambiamos a Mockito puro (Sin levantar Spring)
public class RubroControllerTest {

    private MockMvc mockMvc; 

    @Mock
    private RubroService rubroService; // Simula el servicio

    @InjectMocks
    private RubroController rubroController; // Inyecta el servicio simulado en tu controlador real

    private ObjectMapper objectMapper;
    private RubroRequest requestValido;
    private Rubro rubroSimulado;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rubroController).build();
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
        ResponseRest<Rubro> responseRest = new ResponseRest<>(true, "Rubro creado satisfactoriamente", rubroSimulado, LocalDateTime.now(), "200");
        ResponseEntity<ResponseRest<Rubro>> responseEntity = new ResponseEntity<>(responseRest, HttpStatus.CREATED);
        when(rubroService.crearRubro(any(RubroRequest.class))).thenReturn(responseEntity);
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
        ResponseRest<Rubro> responseRest = new ResponseRest<>(true, "Rubro modificado satisfactoriamente", rubroSimulado, LocalDateTime.now(), "200");
        ResponseEntity<ResponseRest<Rubro>> responseEntity = new ResponseEntity<>(responseRest, HttpStatus.OK);
        
        when(rubroService.actualizarRubro(eq(1), any(RubroRequest.class))).thenReturn(responseEntity);

        mockMvc.perform(put("/rubro/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rubro modificado satisfactoriamente"));
    }

    @Test
    void crearRubro_CuandoFallaValidacion_DeberiaRetornarBadRequest() throws Exception {
        ResponseRest<Rubro> responseRest = new ResponseRest<>(false, "La descripción del rubro es obligatoria y no puede estar vacía.", null, LocalDateTime.now(), "400");
        ResponseEntity<ResponseRest<Rubro>> responseEntity = new ResponseEntity<>(responseRest, HttpStatus.BAD_REQUEST);
        
        when(rubroService.crearRubro(any(RubroRequest.class))).thenReturn(responseEntity);

        requestValido.setDescripcionRubro(""); 

        mockMvc.perform(post("/rubro/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("La descripción del rubro es obligatoria y no puede estar vacía."));
    }
}
