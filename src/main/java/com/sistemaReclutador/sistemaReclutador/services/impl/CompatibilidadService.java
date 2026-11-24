package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.util.List;
import com.sistemaReclutador.sistemaReclutador.dto.CompatibilidadDTO;
import java.util.ArrayList;
import java.text.Normalizer;
import java.util.Locale;
import com.sistemaReclutador.sistemaReclutador.dto.OfertaAnalisisDTO;
import com.sistemaReclutador.sistemaReclutador.dto.CvAnalisisDTO;

public class CompatibilidadService {

	public CompatibilidadDTO construirResultado(Double experiencia, Double habilidades, Double formacion,
			Double ubicacion, Double modalidad, Double preferencias) {

		CompatibilidadDTO resultado = new CompatibilidadDTO();
		List<String> fortalezas = new ArrayList<>();
		List<String> aspectosAMejorar = new ArrayList<>();

		resultado.setExperiencia(experiencia);
		resultado.setHabilidades(habilidades);
		resultado.setFormacion(formacion);
		resultado.setUbicacion(ubicacion);
		resultado.setModalidad(modalidad);
		resultado.setPreferencias(preferencias);

		clasificarCriterio("Experiencia", experiencia, fortalezas, aspectosAMejorar);
		clasificarCriterio("Habilidades", habilidades, fortalezas, aspectosAMejorar);
		clasificarCriterio("Formación", formacion, fortalezas, aspectosAMejorar);
		clasificarCriterio("Ubicación", ubicacion, fortalezas, aspectosAMejorar);
		clasificarCriterio("Modalidad", modalidad, fortalezas, aspectosAMejorar);

		clasificarCriterio("Preferencias", preferencias, fortalezas, aspectosAMejorar);
		resultado.setPorcentajeCompatibilidad(
				calcularPorcentaje(experiencia, habilidades, formacion, ubicacion, modalidad, preferencias));

		resultado.setFortalezas(fortalezas);
		resultado.setAspectosAMejorar(aspectosAMejorar);
		resultado.setRequisitosCumplidos(new ArrayList<>());
		resultado.setRequisitosNoAcreditados(new ArrayList<>());

		return resultado;
	}

	public CompatibilidadDTO evaluarCompatibilidad(OfertaAnalisisDTO oferta, CvAnalisisDTO cv) {

		Double experiencia = evaluarExperiencia(oferta, cv);
		Double habilidades = evaluarHabilidades(oferta, cv);
		Double formacion = evaluarFormacion(oferta, cv);

		CompatibilidadDTO resultado = construirResultado(experiencia, habilidades, formacion, null, null, null);

		CompatibilidadDTO requisitos = evaluarRequisitos(oferta, cv);
		resultado.setRequisitosCumplidos(requisitos.getRequisitosCumplidos());
		resultado.setRequisitosNoAcreditados(requisitos.getRequisitosNoAcreditados());
		resultado.setRequisitosObligatoriosCumplidos(requisitos.getRequisitosObligatoriosCumplidos());
		resultado.setRequisitosObligatoriosNoAcreditados(requisitos.getRequisitosObligatoriosNoAcreditados());
		resultado.setRequisitosDeseablesEncontrados(requisitos.getRequisitosDeseablesEncontrados());
		resultado.setRequisitosDeseablesNoEncontrados(requisitos.getRequisitosDeseablesNoEncontrados());

		return resultado;
	}

	public double calcularPorcentaje(Double experiencia, Double habilidades, Double formacion, Double ubicacion,
			Double modalidad, Double preferencias) {

		validarPuntaje(experiencia);
		validarPuntaje(habilidades);
		validarPuntaje(formacion);
		validarPuntaje(ubicacion);
		validarPuntaje(modalidad);
		validarPuntaje(preferencias);
		double total = 0;
		double pesoTotal = 0;
		if (experiencia != null) {
			total += experiencia * 0.25;
			pesoTotal += 0.25;
		}
		if (habilidades != null) {
			total += habilidades * 0.25;
			pesoTotal += 0.25;
		}
		if (formacion != null) {
			total += formacion * 0.15;
			pesoTotal += 0.15;
		}
		if (ubicacion != null) {
			total += ubicacion * 0.15;
			pesoTotal += 0.15;
		}
		if (modalidad != null) {
			total += modalidad * 0.10;
			pesoTotal += 0.10;
		}
		if (preferencias != null) {
			total += preferencias * 0.10;
			pesoTotal += 0.10;
		}
		if (pesoTotal == 0) {
			return 0;
		}
		return Math.round((total / pesoTotal) * 10000.0) / 100.0;
	}

	private void validarPuntaje(Double puntaje) {

		if (puntaje != null && (puntaje < 0 || puntaje > 1)) {
			throw new IllegalArgumentException("El puntaje debe estar entre 0 y 1");
		}
	}

	private void clasificarCriterio(String criterio, Double puntaje, List<String> fortalezas,
			List<String> aspectosAMejorar) {

		if (puntaje == null) {
			return;
		}

		if (puntaje >= 0.70) {
			fortalezas.add(criterio);
		} else if (puntaje < 0.50) {
			aspectosAMejorar.add(criterio);
		}
	}

	public CompatibilidadDTO evaluarRequisitos(OfertaAnalisisDTO oferta, CvAnalisisDTO cv) {
		CompatibilidadDTO resultado = new CompatibilidadDTO();
		List<String> cumplidos = new ArrayList<>();
		List<String> noAcreditados = new ArrayList<>();
		List<String> obligatoriosCumplidos = new ArrayList<>();
		List<String> obligatoriosNoAcreditados = new ArrayList<>();
		List<String> deseablesEncontrados = new ArrayList<>();
		List<String> deseablesNoEncontrados = new ArrayList<>();
		List<String> informacionCv = new ArrayList<>();
		if (cv != null) {
			agregarInformacion(informacionCv, cv.getExperienciasLaborales());
			agregarInformacion(informacionCv, cv.getHabilidades());
			agregarInformacion(informacionCv, cv.getFormacionAcademica());
			agregarInformacion(informacionCv, cv.getCertificaciones());
			agregarInformacion(informacionCv, cv.getIdiomas());
		}
		if (oferta != null) {
			evaluarLista("Obligatorio", oferta.getRequisitosObligatorios(), informacionCv, cumplidos, noAcreditados,
					obligatoriosCumplidos, obligatoriosNoAcreditados);

			evaluarLista("Deseable", oferta.getRequisitosDeseables(), informacionCv, cumplidos, noAcreditados,
					deseablesEncontrados, deseablesNoEncontrados);
		}
		resultado.setRequisitosCumplidos(cumplidos);
		resultado.setRequisitosNoAcreditados(noAcreditados);
		resultado.setRequisitosObligatoriosCumplidos(obligatoriosCumplidos);
		resultado.setRequisitosObligatoriosNoAcreditados(obligatoriosNoAcreditados);
		resultado.setRequisitosDeseablesEncontrados(deseablesEncontrados);
		resultado.setRequisitosDeseablesNoEncontrados(deseablesNoEncontrados);
		return resultado;
	}

	private void agregarInformacion(List<String> destino, List<String> origen) {
		if (origen != null) {
			destino.addAll(origen);
		}
	}

	private void evaluarLista(String tipo, List<String> requisitos, List<String> informacionCv, List<String> cumplidos,
			List<String> noAcreditados, List<String> listaCumplidos, List<String> listaNoAcreditados) {
		if (requisitos == null) {
			return;
		}
		for (String requisito : requisitos) {
			if (requisito == null || requisito.isBlank()) {
				continue;
			}
			boolean encontrado = informacionCv.stream().filter(dato -> dato != null).map(this::normalizar)
					.anyMatch(dato -> (" " + dato + " ").contains(" " + normalizar(requisito) + " "));
			String descripcion = tipo + ": " + requisito;
			if (encontrado) {
				cumplidos.add(descripcion);
				listaCumplidos.add(requisito);
			} else {
				noAcreditados.add(descripcion);
				listaNoAcreditados.add(requisito);
			}
		}
	}

	public Double evaluarHabilidades(OfertaAnalisisDTO oferta, CvAnalisisDTO cv) {
		if (oferta == null || cv == null) {
			return null;
		}
		List<String> habilidadesRequeridas = oferta.getHabilidadesRequeridas();
		List<String> habilidadesCv = cv.getHabilidades();

		if (habilidadesRequeridas == null || habilidadesCv == null) {
			return null;
		}
		List<String> requeridasValidas = habilidadesRequeridas.stream().filter(h -> h != null && !h.isBlank()).toList();
		List<String> habilidadesValidas = habilidadesCv.stream().filter(h -> h != null && !h.isBlank())
				.map(this::normalizar).toList();
		if (requeridasValidas.isEmpty() || habilidadesValidas.isEmpty()) {
			return null;
		}
		int encontradas = 0;
		for (String habilidad : requeridasValidas) {
			String habilidadNormalizada = normalizar(habilidad);
			boolean encontrada = habilidadesValidas.stream()
					.anyMatch(h -> (" " + h + " ").contains(" " + habilidadNormalizada + " "));
			if (encontrada) {
				encontradas++;
			}
		}
		return (double) encontradas / requeridasValidas.size();
	}

	public Double evaluarExperiencia(OfertaAnalisisDTO oferta, CvAnalisisDTO cv) {
		if (oferta == null || cv == null) {
			return null;
		}
		List<String> experienciaRequerida = oferta.getExperienciaRequerida();
		List<String> experienciasCv = cv.getExperienciasLaborales();
		if (experienciaRequerida == null || experienciasCv == null) {
			return null;
		}
		List<String> requeridasValidas = experienciaRequerida.stream().filter(e -> e != null && !e.isBlank()).toList();
		List<String> experienciasValidas = experienciasCv.stream().filter(e -> e != null && !e.isBlank())
				.map(this::normalizar).toList();
		if (requeridasValidas.isEmpty() || experienciasValidas.isEmpty()) {
			return null;
		}
		int encontradas = 0;
		for (String experiencia : requeridasValidas) {
			String experienciaNormalizada = normalizar(experiencia);
			boolean encontrada = experienciasValidas.stream()
					.anyMatch(e -> (" " + e + " ").contains(" " + experienciaNormalizada + " "));
			if (encontrada) {
				encontradas++;
			}
		}
		return (double) encontradas / requeridasValidas.size();
	}

	public Double evaluarFormacion(OfertaAnalisisDTO oferta, CvAnalisisDTO cv) {
		if (oferta == null || cv == null) {
			return null;
		}
		List<String> formacionRequerida = oferta.getFormacionRequerida();
		List<String> formacionCv = cv.getFormacionAcademica();
		if (formacionRequerida == null || formacionCv == null) {
			return null;
		}
		List<String> requeridasValidas = formacionRequerida.stream().filter(f -> f != null && !f.isBlank()).toList();
		List<String> formacionValida = formacionCv.stream().filter(f -> f != null && !f.isBlank()).map(this::normalizar)
				.toList();
		if (requeridasValidas.isEmpty() || formacionValida.isEmpty()) {
			return null;
		}
		int encontradas = 0;
		for (String formacion : requeridasValidas) {
			String formacionNormalizada = normalizar(formacion);
			boolean encontrada = formacionValida.stream()
					.anyMatch(f -> (" " + f + " ").contains(" " + formacionNormalizada + " "));
			if (encontrada) {
				encontradas++;
			}
		}
		return (double) encontradas / requeridasValidas.size();
	}

	public CompatibilidadDTO analizarCompatibilidad(OfertaAnalisisDTO oferta, CvAnalisisDTO cv) {
		Double experiencia = evaluarExperiencia(oferta, cv);
		Double habilidades = evaluarHabilidades(oferta, cv);
		Double formacion = evaluarFormacion(oferta, cv);
		// Ubicación, modalidad y preferencias quedan pendientes
		// porque todavía no tenemos esos datos en el CV.
		CompatibilidadDTO resultado = construirResultado(experiencia, habilidades, formacion, null, null, null);
		// Incorporamos la evaluación de requisitos obligatorios y deseables.
		CompatibilidadDTO requisitos = evaluarRequisitos(oferta, cv);
		resultado.setRequisitosCumplidos(requisitos.getRequisitosCumplidos());
		resultado.setRequisitosNoAcreditados(requisitos.getRequisitosNoAcreditados());
		resultado.setRequisitosObligatoriosCumplidos(requisitos.getRequisitosObligatoriosCumplidos());
		resultado.setRequisitosObligatoriosNoAcreditados(requisitos.getRequisitosObligatoriosNoAcreditados());
		resultado.setRequisitosDeseablesEncontrados(requisitos.getRequisitosDeseablesEncontrados());
		resultado.setRequisitosDeseablesNoEncontrados(requisitos.getRequisitosDeseablesNoEncontrados());
		return resultado;
	}

	private String normalizar(String texto) {
		return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT)
				.replaceAll("[^\\p{L}\\p{N}\\s]", " ").replaceAll("\\s+", " ").trim();
	}
}
