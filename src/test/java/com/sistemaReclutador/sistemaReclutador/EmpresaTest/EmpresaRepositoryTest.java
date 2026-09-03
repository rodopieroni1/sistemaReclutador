package com.sistemaReclutador.sistemaReclutador.EmpresaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.RubroRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;

@SpringBootTest
@ActiveProfiles("dev") 
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class EmpresaRepositoryTest {

	@Autowired
    private EmpresaRepository empresaRepository;
	@Autowired
    private EmpresaService empresaService;
    @Autowired
    private RubroRepository rubroRepository;

    private Rubro rubroPersistido;
    private MockMultipartFile logoValido;
	
	@BeforeEach
    void setUp() {
        // Como la relación @ManyToOne requiere un rubro obligatorio no nulo en la BD,
        // guardamos uno real antes de cada test para cumplir la integridad referencial.
        Rubro rubro = new Rubro();
        rubro.setDescripcionRubro("Educacion");
        rubroPersistido = rubroRepository.save(rubro);
     // 2. Definición del objeto MultipartFile simulado (MockMultipartFile)
        // Parámetros: nombre del parámetro, nombre del archivo original, tipo de contenido, bytes del contenido
        logoValido = new MockMultipartFile(
            "logo", 
            "logo_empresa.png", 
            "image/png", 
            "contenido_simulado_del_logo".getBytes()
        );
    }
	@Test
    void existsByCuit_DeberiaRetornarTrue_CuandoElCuitYaExisteEnLaBaseDeDatos() {
        Empresa empresa = new Empresa();
        empresa.setNombre("Conesa");
        empresa.setCuit(33397120828L);
        empresa.setRubro(rubroPersistido);
        empresa.setEmail("pedrogmail.com");
        empresaRepository.save(empresa); 
        boolean existe = empresaRepository.existsByCuit(33397120828L);
        assertTrue(existe);
    }
	
	@Test
	void saveEmpresa_DeberiaLanzarExcepcion_CuandoEmailNoEsValido() {
	    EmpresaRequest request = moficarRequestBase("Conesa", "correoInvalido.com", 33309120828L, 4, logoValido);

	    IllegalArgumentException excepcion = assertThrows(
	        IllegalArgumentException.class, 
	        () -> empresaService.saveEmpresa(request)
	    );

	    assertEquals("El formato del correo electrónico no es válido", excepcion.getMessage());
	}

    @Test
    void saveEmpresa_DeberiaRetornarBadRequest_CuandoEmailSupera100Caracteres() {
        String emailLargo = "A".repeat(95) + "@a.com"; // 101 caracteres
        EmpresaRequest request = moficarRequestBase("Conesa", emailLargo, 33307120828L, 4, logoValido);
        ResponseEntity<ResponseRest<Empresa>> respuesta = empresaService.saveEmpresa(request);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("El correo electrónico no puede superar los 100 caracteres", respuesta.getBody().getMessage());
    }

    @Test
    void saveEmpresa_DeberiaRetornarBadRequest_CuandoCuitYaExiste() {
        Empresa empresaPrevia = new Empresa();
        empresaPrevia.setNombre("Empresa Vieja");
        empresaPrevia.setCuit(33303120828L);
        empresaPrevia.setEmail("vieja@hotmail.com");
        empresaPrevia.setRubro(rubroPersistido);
        empresaRepository.saveAndFlush(empresaPrevia);
        EmpresaRequest request = moficarRequestBase("Conesa", "piche@hotmail.com", 33303120828L, logoValido);
        ResponseEntity<ResponseRest<Empresa>> respuesta = empresaService.saveEmpresa(request);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("El Cuit ya está registrado", respuesta.getBody().getMessage());
    }

    @Test
    void saveEmpresa_DeberiaRetornarBadRequest_CuandoEmailYaExiste() {
        Empresa empresaPrevia = new Empresa();
        empresaPrevia.setNombre("Empresa Vieja");
        empresaPrevia.setCuit(11111111111L);
        empresaPrevia.setEmail("piche@hotmail.com");
        empresaPrevia.setRubro(rubroPersistido);
        empresaRepository.saveAndFlush(empresaPrevia);
        EmpresaRequest request = moficarRequestBase("Conesa", "piche@hotmail.com", 22222222222L, logoValido);
        ResponseEntity<ResponseRest<Empresa>> respuesta = empresaService.saveEmpresa(request);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("El email ya está registrado", respuesta.getBody().getMessage());
    }

  
    @Test
    void saveEmpresa_DeberiaRetornarBadRequest_CuandoCuitNoTiene11Digitos() {
        EmpresaRequest request = moficarRequestBase("Conesa", "piche@hotmail.com", 123456789L, logoValido);
        ResponseEntity<ResponseRest<Empresa>> respuesta = empresaService.saveEmpresa(request);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("El CUIT debe tener exactamente 11 dígitos", respuesta.getBody().getMessage());
    }


    @Test
    void saveEmpresa_DeberiaRetornarBadRequest_CuandoNombreDeLogoEsMuyLargo() {
        String nombreArchivoLargo = "A".repeat(242) + ".png";
        MockMultipartFile logoLargo = new MockMultipartFile("logo", nombreArchivoLargo, "image/png", "bytes".getBytes());
        EmpresaRequest request = moficarRequestBase("Conesa", "piche@hotmail.com", 33307120828L, logoLargo);
        ResponseEntity<ResponseRest<Empresa>> respuesta = empresaService.saveEmpresa(request);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("El nombre del archivo del logo es demasiado largo (máximo 245 caracteres)", respuesta.getBody().getMessage());
    }

 
    private EmpresaRequest moficarRequestBase(String nombre, String email, Long cuit, Integer idRubro, MockMultipartFile logo) {
        EmpresaRequest request = new EmpresaRequest();
        request.setNombre(nombre);
        request.setDireccion("Formosa 2020");
        request.setHistoriaEmpresa("compro y venta");
        request.setObservaciones("Ninguna");
        request.setCuit(cuit);
        request.setLogo(logo);
        request.setEmail(email);
        request.setTelefono("3854177555");
        request.setIdRubro(idRubro);
        return request;
    }
}
