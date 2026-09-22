package com.sistemaReclutador.sistemaReclutador.EmpresaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;
import com.sistemaReclutador.sistemaReclutador.services.impl.EmpresaServiceImpl;
import com.sistemaReclutador.sistemaReclutador.strategies.EmpresaValidationStrategy;
import com.sistemaReclutador.sistemaReclutador.strategies.FileStorageStrategy;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceImplTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private RubroService rubroService;

    @Mock
    private FileStorageStrategy fileStorageStrategy;

    @Mock
    private EmpresaValidationStrategy validationStrategy;

    private EmpresaServiceImpl empresaService;

    private EmpresaRequest request;
    private Rubro rubro;
    private Empresa empresa;

    @BeforeEach
    void setUp() {

        empresaService = new EmpresaServiceImpl(
                empresaRepository,
                rubroService,
                fileStorageStrategy,
                List.of(validationStrategy)
        );

        rubro = new Rubro();
        rubro.setIdRubro(1);

        request = new EmpresaRequest();
        request.setCuit(20300000000L);
        request.setEmail("contacto@empresa.com");
        request.setNombre("Empresa Test");
        request.setIdRubro(1);

        empresa = new Empresa();
        empresa.setCuit(20300000000L);
        empresa.setEmail("contacto@empresa.com");
        empresa.setNombre("Empresa Test");
        empresa.setRubro(rubro);
    }

    @Test
    void saveEmpresa_Exito() {

        when(empresaRepository.existsByCuit(request.getCuit()))
                .thenReturn(false);

        when(empresaRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(rubroService.findRubro(1))
                .thenReturn(rubro);

        when(empresaRepository.save(any(Empresa.class)))
                .thenReturn(empresa);

        Empresa resultado = empresaService.saveEmpresa(request);

        assertNotNull(resultado);
        assertEquals("Empresa Test", resultado.getNombre());

        verify(empresaRepository, times(1))
                .save(any(Empresa.class));
    }

    @Test
    void saveEmpresa_CuitDuplicado_LanzaExcepcion() {

        when(empresaRepository.existsByCuit(request.getCuit()))
                .thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> empresaService.saveEmpresa(request)
        );

        assertEquals(
                "El CUIT ya esta registrado.",
                ex.getMessage()
        );

        verify(empresaRepository, never())
                .save(any());
    }

    @Test
    void saveEmpresa_ConLogoValido_GuardaLogoCorrectamente() {

        MockMultipartFile logo = new MockMultipartFile(
                "logo",
                "logo.png",
                "image/png",
                "contenido".getBytes()
        );

        request.setLogo(logo);

        when(empresaRepository.existsByCuit(request.getCuit()))
                .thenReturn(false);

        when(empresaRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(rubroService.findRubro(1))
                .thenReturn(rubro);

        // Mockeamos el almacenamiento del archivo
        when(fileStorageStrategy.storeFile(logo, "logos"))
                .thenReturn("/uploads/logos/logo-generado.png");

        when(empresaRepository.save(any(Empresa.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Empresa resultado = empresaService.saveEmpresa(request);

        assertNotNull(resultado);
        assertNotNull(resultado.getLogo());

        assertEquals(
                "/uploads/logos/logo-generado.png",
                resultado.getLogo()
        );

        verify(fileStorageStrategy, times(1))
                .storeFile(logo, "logos");

        verify(empresaRepository, times(1))
                .save(any(Empresa.class));
    }

    @Test
    void saveEmpresa_ConFormatoInvalido_LanzaExcepcion() {

        MockMultipartFile logo = new MockMultipartFile(
                "logo",
                "archivo.pdf",
                "application/pdf",
                "contenido".getBytes()
        );

        request.setLogo(logo);

        when(empresaRepository.existsByCuit(request.getCuit()))
                .thenReturn(false);

        when(empresaRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(rubroService.findRubro(1))
                .thenReturn(rubro);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> empresaService.saveEmpresa(request)
        );

        assertEquals(
                "Formato de imagen no permitido. Solo se aceptan PNG, JPEG y WEBP.",
                ex.getMessage()
        );

        // No debería intentar guardar el archivo
        verify(fileStorageStrategy, never())
                .storeFile(any(), any());

        verify(empresaRepository, never())
                .save(any());
    }

    @Test
    void deleteEmpresa_Exito() {

        when(empresaRepository.findById(1L))
                .thenReturn(Optional.of(empresa));

        empresaService.deleteEmpresa(1L);

        verify(empresaRepository, times(1))
                .delete(empresa);
    }

    @Test
    void deleteEmpresa_NoExiste_LanzaExcepcion() {

        when(empresaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> empresaService.deleteEmpresa(1L)
        );

        verify(empresaRepository, never())
                .delete(any());
    }
}