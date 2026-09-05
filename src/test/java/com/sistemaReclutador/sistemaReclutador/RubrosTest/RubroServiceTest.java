package com.sistemaReclutador.sistemaReclutador.RubrosTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.exceptions.ResourceNotFoundException;
import com.sistemaReclutador.sistemaReclutador.repositories.RubroRepository;
import com.sistemaReclutador.sistemaReclutador.services.impl.RubroServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RubroServiceTest {

    @Mock
    private RubroRepository rubroRepository;

    @InjectMocks
    private RubroServiceImpl rubroService;

    private RubroRequest requestValido;
    private Rubro rubroGuardado;

    @BeforeEach
    void setUp() {
        requestValido = new RubroRequest();
        requestValido.setDescripcionRubro("Tecnología e Informática");

        rubroGuardado = new Rubro();
        rubroGuardado.setIdRubro(1);
        rubroGuardado.setDescripcionRubro("Tecnología e Informática");
    }

    @Test
    void crearRubro_DeberiaGuardarExitosamente() {
        // Arrange
        when(rubroRepository.save(any(Rubro.class))).thenReturn(rubroGuardado);

        // Act (crearRubro retorna directamente el objeto Rubro)
        Rubro resultado = rubroService.crearRubro(requestValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdRubro());
        assertEquals("Tecnología e Informática", resultado.getDescripcionRubro());
        verify(rubroRepository, times(1)).save(any(Rubro.class));
    }

    @Test
    void actualizarRubro_CuandoExiste_DeberiaActualizarYGuardar() {
        // Arrange
        when(rubroRepository.findById(1)).thenReturn(Optional.of(rubroGuardado));
        when(rubroRepository.save(any(Rubro.class))).thenReturn(rubroGuardado);

        // Act
        Rubro resultado = rubroService.actualizarRubro(1, requestValido);

        // Assert
        assertNotNull(resultado);
        assertEquals("Tecnología e Informática", resultado.getDescripcionRubro());
        verify(rubroRepository, times(1)).findById(1);
        verify(rubroRepository, times(1)).save(any(Rubro.class));
    }

    @Test
    void obtenerRubroPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(rubroRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            rubroService.obtenerRubroPorId(99);
        });
        verify(rubroRepository, times(1)).findById(99);
    }

    @Test
    void eliminarRubro_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(rubroRepository.existsById(99)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            rubroService.eliminarRubro(99);
        });
        verify(rubroRepository, never()).deleteById(anyInt());
    }
}