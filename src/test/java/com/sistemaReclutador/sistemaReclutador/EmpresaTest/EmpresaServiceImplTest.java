package com.sistemaReclutador.sistemaReclutador.EmpresaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;
import com.sistemaReclutador.sistemaReclutador.services.impl.EmpresaServiceImpl;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceImplTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private RubroService rubroService;

    @InjectMocks
    private EmpresaServiceImpl empresaService;

    @TempDir
    Path tempDir;

    private EmpresaRequest request;
    private Rubro rubro;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(empresaService, "uploadDir", tempDir.toString());

        rubro = new Rubro();
        rubro.setIdRubro(1);

        request = new EmpresaRequest();
        request.setCuit(20300000000L);
        request.setEmail("contacto@empresa.com");
        request.setNombre("Empresa Test");
        request.setIdRubro(1);

        empresa = new Empresa();
        empresa.getRubro().setIdRubro(1);
        empresa.setCuit(20300000000L);
        empresa.setEmail("contacto@empresa.com");
        empresa.setNombre("Empresa Test");
        empresa.setRubro(rubro);
    }

    @Test
    void saveEmpresa_Exito() {
        when(empresaRepository.existsByCuit(request.getCuit())).thenReturn(false);
        when(empresaRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(rubroService.findRubro(1)).thenReturn(rubro);
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        Empresa resultado = empresaService.saveEmpresa(request);

        assertNotNull(resultado);
        assertEquals("Empresa Test", resultado.getNombre());
        verify(empresaRepository, times(1)).save(any(Empresa.class));
    }

    @Test
    void saveEmpresa_CuitDuplicado_LanzaExcepcion() {
        when(empresaRepository.existsByCuit(request.getCuit())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> empresaService.saveEmpresa(request));
        assertEquals("El CUIT ya está registrado.", ex.getMessage());
        verify(empresaRepository, never()).save(any());
    }

    @Test
    void saveEmpresa_ConLogoValido_GuardaLogoCorrectamente() {
        MockMultipartFile logo = new MockMultipartFile("logo", "logo.png", "image/png", "contenido".getBytes());
        request.setLogo(logo);

        when(empresaRepository.existsByCuit(request.getCuit())).thenReturn(false);
        when(empresaRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(rubroService.findRubro(1)).thenReturn(rubro);
        when(empresaRepository.save(any(Empresa.class))).thenAnswer(i -> i.getArgument(0));

        Empresa resultado = empresaService.saveEmpresa(request);

        assertNotNull(resultado.getLogo());
        assertTrue(resultado.getLogo().endsWith(".png"));
    }

    @Test
    void saveEmpresa_ConFormatoInvalido_LanzaExcepcion() {
        MockMultipartFile logo = new MockMultipartFile("logo", "archivo.pdf", "application/pdf", "contenido".getBytes());
        request.setLogo(logo);

        when(empresaRepository.existsByCuit(request.getCuit())).thenReturn(false);
        when(empresaRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(rubroService.findRubro(1)).thenReturn(rubro);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> empresaService.saveEmpresa(request));
        assertEquals("Formato de imagen no permitido. Solo se aceptan PNG, JPEG y WEBP.", ex.getMessage());
    }

    @Test
    void deleteEmpresa_Exito() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));

        empresaService.deleteEmpresa(1L);

        verify(empresaRepository, times(1)).delete(empresa);
    }

    @Test
    void deleteEmpresa_NoExiste_LanzaExcepcion() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> empresaService.deleteEmpresa(1L));
    }
}