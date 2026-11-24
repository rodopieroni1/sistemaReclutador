package com.sistemaReclutador.sistemaReclutador;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sistemaReclutador.sistemaReclutador.dto.CompatibilidadDTO;
import com.sistemaReclutador.sistemaReclutador.dto.CvAnalisisDTO;
import com.sistemaReclutador.sistemaReclutador.dto.OfertaAnalisisDTO;
import com.sistemaReclutador.sistemaReclutador.services.impl.CompatibilidadService;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNull;

class CompatibilidadServiceTest {

	private CompatibilidadService servicio;

	@BeforeEach
	void setUp() {
		servicio = new CompatibilidadService();
	}

	// 1. Redistribución con un solo criterio evaluable
	@Test
	void debeCalcularCompatibilidadConUnSoloCriterio() {
		double resultado = servicio.calcularPorcentaje(0.80, null, null, null, null, null);
		assertEquals(80.0, resultado, 0.01);
	}

	// 2. Reconocer un requisito presente en el CV
	@Test
	void debeReconocerUnRequisitoEncontradoEnElCv() {
		OfertaAnalisisDTO oferta = crearOferta(List.of("Java"), Collections.emptyList());
		CvAnalisisDTO cv = crearCv(List.of("Experiencia desarrollando con Java"));
		CompatibilidadDTO resultado = servicio.evaluarRequisitos(oferta, cv);
		assertTrue(resultado.getRequisitosCumplidos().stream().anyMatch(r -> r.contains("Java")));
	}

	// 3. Reconocer un requisito aunque cambie la puntuación
	@Test
	void debeReconocerRequisitoAunqueCambieLaPuntuacion() {
		OfertaAnalisisDTO oferta = crearOferta(List.of("Java"), Collections.emptyList());
		CvAnalisisDTO cv = crearCv(List.of("Experiencia desarrollando con Java."));
		CompatibilidadDTO resultado = servicio.evaluarRequisitos(oferta, cv);
		assertFalse(resultado.getRequisitosNoAcreditados().stream().anyMatch(r -> r.contains("Java")));
	}

	// 4. No confundir Java con JavaScript
	@Test
	void noDebeConfundirJavaConJavaScript() {
		OfertaAnalisisDTO oferta = crearOferta(List.of("Java"), Collections.emptyList());
		CvAnalisisDTO cv = crearCv(List.of("Experiencia desarrollando con JavaScript"));
		CompatibilidadDTO resultado = servicio.evaluarRequisitos(oferta, cv);
		assertTrue(resultado.getRequisitosNoAcreditados().stream().anyMatch(r -> r.contains("Java")));
	}

	// 5. Ignorar diferencias entre mayúsculas y minúsculas
	@Test
	void debeIgnorarMayusculasYMinusculas() {
		OfertaAnalisisDTO oferta = crearOferta(List.of("JAVA"), Collections.emptyList());
		CvAnalisisDTO cv = crearCv(List.of("Experiencia con java"));
		CompatibilidadDTO resultado = servicio.evaluarRequisitos(oferta, cv);
		assertFalse(resultado.getRequisitosNoAcreditados().stream().anyMatch(r -> r.contains("JAVA")));
	}

	// 6. Ignorar diferencias de acentuación
	@Test
	void debeIgnorarDiferenciasDeAcentuacion() {
		OfertaAnalisisDTO oferta = crearOferta(List.of("Analisis de datos"), Collections.emptyList());
		CvAnalisisDTO cv = crearCv(List.of("Experiencia en análisis de datos"));
		CompatibilidadDTO resultado = servicio.evaluarRequisitos(oferta, cv);
		assertFalse(resultado.getRequisitosNoAcreditados().stream().anyMatch(r -> r.contains("Analisis")));
	}

	// 7. Requisito ausente: no acreditado
	@Test
	void debeMarcarRequisitoAusenteComoNoAcreditado() {
		OfertaAnalisisDTO oferta = crearOferta(List.of("Spring Boot"), Collections.emptyList());
		CvAnalisisDTO cv = crearCv(List.of("Experiencia con Java"));
		CompatibilidadDTO resultado = servicio.evaluarRequisitos(oferta, cv);
		assertTrue(resultado.getRequisitosNoAcreditados().stream().anyMatch(r -> r.contains("Spring Boot")));
	}

	// 8. Separar obligatorios y deseables
	@Test
	void debeSepararRequisitosObligatoriosYDeseables() {
		OfertaAnalisisDTO oferta = crearOferta(List.of("Java"), List.of("Docker"));
		CvAnalisisDTO cv = crearCv(List.of("Experiencia con Java y Docker"));
		CompatibilidadDTO resultado = servicio.evaluarRequisitos(oferta, cv);
		assertEquals(1, resultado.getRequisitosObligatoriosCumplidos().size());
		assertEquals(1, resultado.getRequisitosDeseablesEncontrados().size());
	}

	// 9. Un obligatorio no acreditado no bloquea la evaluación
	@Test
	void obligatorioNoAcreditadoNoDebeBloquearEvaluacion() {
		OfertaAnalisisDTO oferta = crearOferta(List.of("Spring Boot"), Collections.emptyList());
		CvAnalisisDTO cv = crearCv(List.of("Experiencia con Java"));
		CompatibilidadDTO resultado = servicio.evaluarRequisitos(oferta, cv);
		assertNotNull(resultado);
		assertEquals(1, resultado.getRequisitosObligatoriosNoAcreditados().size());
	}

	// 10. CV sin información
	@Test
	void debeProcesarCvSinInformacion() {
		OfertaAnalisisDTO oferta = crearOferta(List.of("Java"), Collections.emptyList());
		CvAnalisisDTO cv = new CvAnalisisDTO();
		CompatibilidadDTO resultado = servicio.evaluarRequisitos(oferta, cv);
		assertNotNull(resultado);
		assertEquals(1, resultado.getRequisitosNoAcreditados().size());
	}

	// 11. Oferta sin requisitos
	@Test
	void debeProcesarOfertaSinRequisitos() {
		OfertaAnalisisDTO oferta = crearOferta(Collections.emptyList(), Collections.emptyList());
		CvAnalisisDTO cv = crearCv(List.of("Experiencia con Java"));
		CompatibilidadDTO resultado = servicio.evaluarRequisitos(oferta, cv);
		assertNotNull(resultado);
		assertTrue(resultado.getRequisitosCumplidos().isEmpty());
		assertTrue(resultado.getRequisitosNoAcreditados().isEmpty());
	}

	// 12. CV nulo
	@Test
	void debeProcesarCvNuloSinExcepcion() {
		OfertaAnalisisDTO oferta = crearOferta(List.of("Java"), Collections.emptyList());
		assertDoesNotThrow(() -> servicio.evaluarRequisitos(oferta, null));
	}

	// 13. Oferta nula
	@Test
	void debeProcesarOfertaNulaSinExcepcion() {
		CvAnalisisDTO cv = crearCv(List.of("Experiencia con Java"));
		assertDoesNotThrow(() -> servicio.evaluarRequisitos(null, cv));
	}

	// 14. Redistribución entre dos criterios
	@Test
	void debeRedistribuirPesosEntreDosCriterios() {
		double resultado = servicio.calcularPorcentaje(0.80, 0.60, null, null, null, null);
		assertEquals(70.0, resultado, 0.01);
	}

	// 15. Ningún criterio evaluable
	@Test
	void debeDevolverCeroCuandoNingunCriterioEsEvaluable() {
		double resultado = servicio.calcularPorcentaje(null, null, null, null, null, null);

		assertEquals(0.0, resultado, 0.01);
	}

	private OfertaAnalisisDTO crearOferta(List<String> obligatorios, List<String> deseables) {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setRequisitosObligatorios(obligatorios);
		oferta.setRequisitosDeseables(deseables);
		return oferta;
	}

	private CvAnalisisDTO crearCv(List<String> experiencias) {
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setExperienciasLaborales(experiencias);
		return cv;
	}

	@Test
	void debeCalcularPorcentajeDeHabilidadesEncontradas() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setHabilidadesRequeridas(Arrays.asList("Java", "Spring", "SQL", "Docker"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setHabilidades(Arrays.asList("Java", "Spring", "SQL"));
		Double resultado = servicio.evaluarHabilidades(oferta, cv);
		assertEquals(0.75, resultado, 0.001);
	}

	@Test
	void debeIgnorarMayusculasYMinusculasEnHabilidades() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setHabilidadesRequeridas(Arrays.asList("JAVA"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setHabilidades(Arrays.asList("java"));
		assertEquals(1.0, servicio.evaluarHabilidades(oferta, cv), 0.001);
	}

	@Test
	void noDebeConfundirHabilidadesConNombresSimilares() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setHabilidadesRequeridas(Arrays.asList("Java"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setHabilidades(Arrays.asList("JavaScript"));
		assertEquals(0.0, servicio.evaluarHabilidades(oferta, cv), 0.001);
	}

	@Test
	void debeDevolverNullCuandoNoHayHabilidadesParaEvaluar() {

		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setHabilidadesRequeridas(Arrays.asList("Java"));

		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setHabilidades(new ArrayList<>());

		assertNull(servicio.evaluarHabilidades(oferta, cv));
	}

	@Test
	void debeCalcularPorcentajeDeExperienciasEncontradas() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setExperienciaRequerida(Arrays.asList("Atención al cliente", "Ventas", "Manejo de caja"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setExperienciasLaborales(Arrays.asList("Atención al cliente", "Ventas"));
		assertEquals(0.6667, servicio.evaluarExperiencia(oferta, cv), 0.001);
	}

	@Test
	void debeIgnorarMayusculasYMinusculasEnExperiencia() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setExperienciaRequerida(Arrays.asList("ATENCION AL CLIENTE"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setExperienciasLaborales(Arrays.asList("Atención al cliente"));
		assertEquals(1.0, servicio.evaluarExperiencia(oferta, cv), 0.001);
	}

	@Test
	void noDebeConfundirExperienciasConNombresSimilares() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setExperienciaRequerida(Arrays.asList("Java"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setExperienciasLaborales(Arrays.asList("JavaScript"));
		assertEquals(0.0, servicio.evaluarExperiencia(oferta, cv), 0.001);
	}

	@Test
	void debeDevolverNullCuandoNoHayExperienciaParaEvaluar() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setExperienciaRequerida(Arrays.asList("Ventas"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setExperienciasLaborales(new ArrayList<>());
		assertNull(servicio.evaluarExperiencia(oferta, cv));
	}

	@Test
	void debeCalcularPorcentajeDeFormacionesEncontradas() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setFormacionRequerida(Arrays.asList("Analista de sistemas", "Contador", "Abogado"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setFormacionAcademica(Arrays.asList("Analista de sistemas", "Contador"));
		assertEquals(0.6667, servicio.evaluarFormacion(oferta, cv), 0.001);
	}

	@Test
	void debeIgnorarMayusculasYMinusculasEnFormacion() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setFormacionRequerida(Arrays.asList("ANALISTA DE SISTEMAS"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setFormacionAcademica(Arrays.asList("Analista de sistemas"));
		assertEquals(1.0, servicio.evaluarFormacion(oferta, cv), 0.001);
	}

	@Test
	void noDebeConfundirFormacionesConNombresSimilares() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setFormacionRequerida(Arrays.asList("Contador"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setFormacionAcademica(Arrays.asList("Contabilidad"));
		assertEquals(0.0, servicio.evaluarFormacion(oferta, cv), 0.001);
	}

	@Test
	void debeDevolverNullCuandoNoHayFormacionParaEvaluar() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setFormacionRequerida(Arrays.asList("Abogado"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setFormacionAcademica(new ArrayList<>());
		assertNull(servicio.evaluarFormacion(oferta, cv));
	}

	@Test
	void evaluarCompatibilidad_debeIntegrarLosTresCriterios() {
		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setExperienciaRequerida(List.of("Java"));
		oferta.setHabilidadesRequeridas(List.of("Spring"));
		oferta.setFormacionRequerida(List.of("Ingeniería"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setExperienciasLaborales(List.of("Java"));
		cv.setHabilidades(List.of("Spring"));
		cv.setFormacionAcademica(List.of("Ingeniería"));
		CompatibilidadDTO resultado = servicio.evaluarCompatibilidad(oferta, cv);
		assertEquals(1.0, resultado.getExperiencia());
		assertEquals(1.0, resultado.getHabilidades());
		assertEquals(1.0, resultado.getFormacion());
		assertEquals(100.0, resultado.getPorcentajeCompatibilidad());
	}

	@Test
	void evaluarCompatibilidad_debeIntegrarPuntajesYRequisitos() {

		OfertaAnalisisDTO oferta = new OfertaAnalisisDTO();
		oferta.setExperienciaRequerida(List.of("Java"));
		oferta.setHabilidadesRequeridas(List.of("Spring"));
		oferta.setFormacionRequerida(List.of("Ingeniería"));
		oferta.setRequisitosObligatorios(List.of("Java", "Inglés avanzado"));
		oferta.setRequisitosDeseables(List.of("Spring"));
		CvAnalisisDTO cv = new CvAnalisisDTO();
		cv.setExperienciasLaborales(List.of("Java"));
		cv.setHabilidades(List.of("Spring"));
		cv.setFormacionAcademica(List.of("Ingeniería"));
		CompatibilidadDTO resultado = servicio.evaluarCompatibilidad(oferta, cv);
		assertEquals(100.0, resultado.getPorcentajeCompatibilidad());
		assertTrue(resultado.getRequisitosObligatoriosCumplidos().contains("Java"));
		assertTrue(resultado.getRequisitosObligatoriosNoAcreditados().contains("Inglés avanzado"));
		assertTrue(resultado.getRequisitosDeseablesEncontrados().contains("Spring"));
	}

}
