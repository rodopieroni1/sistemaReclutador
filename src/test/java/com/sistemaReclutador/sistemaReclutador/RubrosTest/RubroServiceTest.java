package com.sistemaReclutador.sistemaReclutador.RubrosTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.RubroRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.impl.RubroServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RubroServiceTest {

    @Mock
    private RubroRepository rubroRepository; // Simula el repositorio
    @InjectMocks
    private RubroServiceImpl rubroService; // Inyecta el mock en tu servicio real

    private RubroRequest requestValido;

    @BeforeEach
    void setUp() {
        requestValido = new RubroRequest();
        requestValido.setDescripcionRubro("Tecnología e Informática");
    }
    
    @Test
    void crearRubro_DeberiaGuardarExitosamente() {
        // Configuración del Mock: Simulamos lo que devuelve la base de datos
        Rubro rubroGuardado = new Rubro();
        rubroGuardado.setIdRubro(1);
        rubroGuardado.setDescripcionRubro("Tecnología e Informática");
        when(rubroRepository.save(any(Rubro.class))).thenReturn(rubroGuardado);
        // Ejecución del método a probar
        ResponseEntity<ResponseRest<Rubro>> respuesta = rubroService.crearRubro(requestValido);
        // Verificaciones (Asserts)
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().isSuccess());
        assertEquals("Rubro creado satisfactoriamente", respuesta.getBody().getMessage());
        assertEquals("200", respuesta.getBody().getErrorCode());
        assertNotNull(respuesta.getBody().getData());
        
        // Verifica que el repositorio realmente se llamó una vez
        verify(rubroRepository, times(1)).save(any(Rubro.class));
    }
    
    @Test
    void crearRubro_CuandoDescripcionEstaVacia_DeberiaRetornarBadRequest() {
        // Configuramos el request con un espacio en blanco
        RubroRequest requestVacio = new RubroRequest();
        requestVacio.setDescripcionRubro("   "); 
        // Ejecución
        ResponseEntity<ResponseRest<Rubro>> respuesta = rubroService.crearRubro(requestVacio);
        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertFalse(respuesta.getBody().isSuccess());
        assertEquals("La descripción del rubro es obligatoria y no puede estar vacía.", respuesta.getBody().getMessage());
        assertEquals("400", respuesta.getBody().getErrorCode());
        verify(rubroRepository, never()).save(any(Rubro.class));//se verifica que nunca se guardo en bbdd
    }
    
    
    @Test
    void crearRubro_CuandoSupera100Caracteres_DeberiaRetornarBadRequest() {
        String textoLargo = "A".repeat(105);//ribro de 105 carcateres
        RubroRequest requestLargo = new RubroRequest();
        requestLargo.setDescripcionRubro(textoLargo);
        ResponseEntity<ResponseRest<Rubro>> respuesta = rubroService.crearRubro(requestLargo);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertFalse(respuesta.getBody().isSuccess());
        assertEquals("La descripción del rubro no puede superar los 100 caracteres.", respuesta.getBody().getMessage());
        verify(rubroRepository, never()).save(any(Rubro.class));
    }

    @Test
    void crearRubro_CuandoFallaLaBaseDeDatos_DeberiaRetornarInternalServerError() {
        when(rubroRepository.save(any(Rubro.class))).thenThrow(new RuntimeException("Conexión perdida"));
        ResponseEntity<ResponseRest<Rubro>> respuesta = rubroService.crearRubro(requestValido);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertFalse(respuesta.getBody().isSuccess());
        assertTrue(respuesta.getBody().getMessage().contains("Error al crear el rubro:"));
        assertEquals("500", respuesta.getBody().getErrorCode());
    }

}
