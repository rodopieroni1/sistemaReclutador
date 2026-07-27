package com.sistemaReclutador.sistemaReclutador.EmpresaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.RubroRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;
import com.sistemaReclutador.sistemaReclutador.services.impl.EmpresaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmpresaServiceTest {

	@Mock
	private EmpresaRepository empresaRepository;
	@Mock
	private RubroRepository rubroRepository;
	@Mock
	private RubroService rubroService;
	@InjectMocks
	private EmpresaServiceImpl empresaService;

	private Rubro requestValidoRubro;
	private EmpresaRequest requestValido;

	@BeforeEach
	void setUp() {
		MockMultipartFile logoSimulado = new MockMultipartFile("logo", "logopng.png", "image/png",
				"contenido_de_imagen_ficticio".getBytes());

		requestValidoRubro = new Rubro();
		requestValidoRubro.setIdRubro(1);
		requestValidoRubro.setDescripcionRubro("Educacion");

		requestValido = new EmpresaRequest();
		requestValido.setIdempresa(1);
		requestValido.setNombre("Conesa");
		requestValido.setDireccion("Formosa 2020");
		requestValido.setHistoriaEmpresa("empresa dedicada a la compra y venta textil");
		requestValido.setObservaciones("Nadaaaaa");
		requestValido.setCuit(31111120828L);
		requestValido.setLogo(logoSimulado);
		requestValido.setEmail("piche@hotmail.com");
		requestValido.setTelefono("3854177555");
		requestValido.setIdRubro(1L);
	}

	@Test
	void crearEmpresa_DeberiaGuardarExitosamente() {
		Rubro rubroGuardado = new Rubro();
		rubroGuardado.setIdRubro(1);
		rubroGuardado.setDescripcionRubro("Textil");
		when(rubroService.findRubro(1L)).thenReturn(rubroGuardado);
		Empresa empresaGuardada = new Empresa();
		empresaGuardada.setId_empresa(1L);
		empresaGuardada.setNombre("Conesa");
		empresaGuardada.setDireccion("Formosa 2020");
		empresaGuardada.setHistoriaEmpresa("empresa dedicada a la compra y venta textil");
		empresaGuardada.setObservaciones("Nadaaaaa");
		empresaGuardada.setCuit(30256871912L);
		empresaGuardada.setLogo("logopng.png");
		empresaGuardada.setEmail("piche@hotmail.com");
		empresaGuardada.setTelefono("3854177555");
		empresaGuardada.setRubro(rubroGuardado);
		when(empresaRepository.save(any(Empresa.class))).thenReturn(empresaGuardada);
        org.springframework.test.util.ReflectionTestUtils.setField(empresaService, "uploadDir", "uploads/");
		ResponseEntity<ResponseRest<Empresa>> respuesta = empresaService.saveEmpresa(requestValido);

		assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
		assertTrue(respuesta.getBody().isSuccess());
		assertEquals("Empresa creada satisfactoriamente", respuesta.getBody().getMessage());
		assertEquals("200", respuesta.getBody().getErrorCode());
		assertNotNull(respuesta.getBody().getData());

		// Verifica que el repositorio realmente guardó los datos una vez
		verify(empresaRepository, times(1)).save(any(Empresa.class));
		// Verifica que se consultó la existencia del rubro asignado una vez
		verify(rubroService, times(1)).findRubro(1L);
	}
	
    @Test
    void crearEmpresa_CuandoCuitYaExiste_DeberiaRetornarBadRequest() {
        // 1. Mockeamos el Rubro para que la conversión del DTO (convertirDtoAEntidad) funcione sin romperse
        Rubro rubroSimulado = new Rubro();
        rubroSimulado.setIdRubro(1);
        rubroSimulado.setDescripcionRubro("Textil");
        when(rubroService.findRubro(1L)).thenReturn(rubroSimulado);

        // 2. Simulamos tu validación real: obligamos a existsByCuit a devolver true
        when(empresaRepository.existsByCuit(requestValido.getCuit())).thenReturn(true);

        // Inyectamos la propiedad del directorio de subida requerida por el entorno
        org.springframework.test.util.ReflectionTestUtils.setField(empresaService, "uploadDir", "uploads/");

        // Ejecución
        ResponseEntity<ResponseRest<Empresa>> respuesta = empresaService.saveEmpresa(requestValido);

        // 3. Verificaciones de QA basadas exactamente en el ResponseRest de tu backend
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertFalse(respuesta.getBody().isSuccess());
        assertEquals("El Cuit ya está registrado", respuesta.getBody().getMessage()); // Match con tu String de Java
        assertEquals("400", respuesta.getBody().getErrorCode());

        // Verificamos que se llamó a la validación una vez
        verify(empresaRepository, times(1)).existsByCuit(requestValido.getCuit());
        // Verificación de seguridad de QA: El repositorio nunca debió intentar el .save() debido al duplicado
        verify(empresaRepository, never()).save(any(Empresa.class));
    }



}
