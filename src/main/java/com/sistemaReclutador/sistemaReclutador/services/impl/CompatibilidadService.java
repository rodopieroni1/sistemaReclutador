package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.util.List;
import com.sistemaReclutador.sistemaReclutador.dto.CompatibilidadDTO;
import java.util.ArrayList;
import java.text.Normalizer;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaAnalisisDTO;
import com.sistemaReclutador.sistemaReclutador.dto.RequisitoAnalisisDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.dto.CvAnalisisDTO;

@Service
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

		if (oferta != null) {

			List<RequisitoAnalisisDTO> obligatoriosCategorizados = oferta.getRequisitosObligatoriosCategorizados();

			List<RequisitoAnalisisDTO> deseablesCategorizados = oferta.getRequisitosDeseablesCategorizados();

			// Obligatorios
			if (obligatoriosCategorizados != null && !obligatoriosCategorizados.isEmpty()) {

				evaluarListaCategorizada("Obligatorio", obligatoriosCategorizados, cv, cumplidos, noAcreditados,
						obligatoriosCumplidos, obligatoriosNoAcreditados);

			} else {

				evaluarLista("Obligatorio", oferta.getRequisitosObligatorios(), obtenerTodaInformacionCv(cv), cumplidos,
						noAcreditados, obligatoriosCumplidos, obligatoriosNoAcreditados);
			}

			// Deseables
			if (deseablesCategorizados != null && !deseablesCategorizados.isEmpty()) {

				evaluarListaCategorizada("Deseable", deseablesCategorizados, cv, cumplidos, noAcreditados,
						deseablesEncontrados, deseablesNoEncontrados);

			} else {

				evaluarLista("Deseable", oferta.getRequisitosDeseables(), obtenerTodaInformacionCv(cv), cumplidos,
						noAcreditados, deseablesEncontrados, deseablesNoEncontrados);
			}
		}

		resultado.setRequisitosCumplidos(cumplidos);
		resultado.setRequisitosNoAcreditados(noAcreditados);
		resultado.setRequisitosObligatoriosCumplidos(obligatoriosCumplidos);
		resultado.setRequisitosObligatoriosNoAcreditados(obligatoriosNoAcreditados);
		resultado.setRequisitosDeseablesEncontrados(deseablesEncontrados);
		resultado.setRequisitosDeseablesNoEncontrados(deseablesNoEncontrados);

		return resultado;
	}

	private List<String> obtenerTodaInformacionCv(CvAnalisisDTO cv) {

		List<String> informacion = new ArrayList<>();

		if (cv == null) {
			return informacion;
		}

		agregarInformacion(informacion, cv.getExperienciasLaborales());
		agregarInformacion(informacion, cv.getHabilidades());
		agregarInformacion(informacion, cv.getFormacionAcademica());
		agregarInformacion(informacion, cv.getCertificaciones());
		agregarInformacion(informacion, cv.getIdiomas());

		return informacion;
	}

	private void evaluarListaCategorizada(String tipo, List<RequisitoAnalisisDTO> requisitos, CvAnalisisDTO cv,
			List<String> cumplidos, List<String> noAcreditados, List<String> listaCumplidos,
			List<String> listaNoAcreditados) {
		if (requisitos == null) {
			return;
		}
		for (RequisitoAnalisisDTO requisito : requisitos) {
			if (requisito == null || requisito.getDescripcion() == null || requisito.getDescripcion().isBlank()) {
				continue;
			}
			List<String> informacionCv = obtenerInformacionPorCategoria(requisito.getCategoria(), cv);
			String descripcion = requisito.getDescripcion();
			boolean encontrado = informacionCv.stream().filter(dato -> dato != null).map(this::normalizar)
					.anyMatch(dato -> (" " + dato + " ").contains(" " + normalizar(descripcion) + " "));
			if (encontrado) {
				cumplidos.add(tipo + ": " + descripcion);
				listaCumplidos.add(descripcion);
			} else {
				noAcreditados.add(tipo + ": " + descripcion);
				listaNoAcreditados.add(descripcion);
			}
		}
	}

	private List<String> obtenerInformacionPorCategoria(String categoria, CvAnalisisDTO cv) {

		if (cv == null || categoria == null) {
			return new ArrayList<>();
		}
		switch (normalizar(categoria)) {
		case "experiencia":
			return cv.getExperienciasLaborales() != null ? cv.getExperienciasLaborales() : new ArrayList<>();
		case "habilidades":
			return cv.getHabilidades() != null ? cv.getHabilidades() : new ArrayList<>();
		case "formacion":
			return cv.getFormacionAcademica() != null ? cv.getFormacionAcademica() : new ArrayList<>();
		case "certificaciones":
			return cv.getCertificaciones() != null ? cv.getCertificaciones() : new ArrayList<>();
		case "idiomas":
			return cv.getIdiomas() != null ? cv.getIdiomas() : new ArrayList<>();
		default:
			throw new IllegalArgumentException("Categoría de requisito no válida: " + categoria);
		}
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
			String requisito = normalizar(formacion);
			boolean encontrada = formacionValida.stream().anyMatch(f -> {
				boolean coincidenciaLiteral = (" " + f + " ").contains(" " + requisito + " ");
				boolean tituloJuridico = (requisito.contains("abogado") || requisito.contains("procurador"))
						&& (f.matches(".*\\babogada?\\b.*") || f.matches(".*\\bprocuradora?\\b.*"));
				return coincidenciaLiteral || tituloJuridico;
			});
			if (encontrada) {
				encontradas++;
			}
		}
		return (double) encontradas / requeridasValidas.size();
	}

	public CompatibilidadDTO analizarCompatibilidad(OfertaAnalisisDTO oferta, CvAnalisisDTO cv) {

		return analizarCompatibilidad(oferta, cv, null);
	}

	public Double evaluarUbicacion(Perfil perfil, OfertaAnalisisDTO oferta) {

		if (perfil == null || oferta == null) {
			return null;
		}

		// Una oferta remota no depende de la ubicación del postulante.
		if ("REMOTA".equalsIgnoreCase(oferta.getModalidad())) {
			return null;
		}

		String localidadPerfil = perfil.getLocalidad();
		String provinciaPerfil = perfil.getProvincia();
		String paisPerfil = perfil.getPais();

		String localidadOferta = oferta.getLocalidad();
		String provinciaOferta = oferta.getProvincia();
		String paisOferta = oferta.getPais();

		if (paisPerfil == null || paisPerfil.isBlank() || paisOferta == null || paisOferta.isBlank()) {
			return null;
		}

		if (!normalizar(paisPerfil).equals(normalizar(paisOferta))) {
			return 0.0;
		}

		if (provinciaPerfil != null && !provinciaPerfil.isBlank() && provinciaOferta != null
				&& !provinciaOferta.isBlank()) {

			if (!normalizar(provinciaPerfil).equals(normalizar(provinciaOferta))) {
				return 0.40;
			}

			if (localidadPerfil != null && !localidadPerfil.isBlank() && localidadOferta != null
					&& !localidadOferta.isBlank() && normalizar(localidadPerfil).equals(normalizar(localidadOferta))) {
				return 1.0;
			}

			return 0.70;
		}

		return 0.40;
	}

	public CompatibilidadDTO analizarCompatibilidad(OfertaAnalisisDTO oferta, CvAnalisisDTO cv, Perfil perfil) {

		Double experiencia = evaluarExperiencia(oferta, cv);
		Double habilidades = evaluarHabilidades(oferta, cv);
		Double formacion = evaluarFormacion(oferta, cv);
		Double ubicacion = evaluarUbicacion(perfil, oferta);

		CompatibilidadDTO resultado = construirResultado(experiencia, habilidades, formacion, ubicacion, null, null);

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
