
package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaAnalisisDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;

@Service
public class OfertaAnalizadorService {

	public OfertaAnalisisDTO analizar(Oferta oferta) {

		if (oferta == null) {
			throw new IllegalArgumentException("La oferta no puede ser nula.");
		}

		OfertaAnalisisDTO resultado = new OfertaAnalisisDTO();

		resultado.setNombreOferta(oferta.getNombreOferta());
		resultado.setDescripcion(oferta.getDescripcionOferta());
		resultado.setModalidad(oferta.getModalidad() != null ? oferta.getModalidad().toString() : null);

		resultado.setLocalidad(oferta.getLocalidadTrabajo());
		resultado.setProvincia(oferta.getProvinciaTrabajo());
		resultado.setPais(oferta.getPaisTrabajo());

		resultado.setRequisitosObligatorios(new ArrayList<>());
		resultado.setRequisitosDeseables(new ArrayList<>());
		resultado.setHabilidadesRequeridas(new ArrayList<>());
		resultado.setFormacionRequerida(new ArrayList<>());
		resultado.setExperienciaRequerida(new ArrayList<>());

		clasificarDescripcion(resultado);

		return resultado;
	}

	private void clasificarDescripcion(OfertaAnalisisDTO resultado) {

		String descripcion = resultado.getDescripcion();

		if (descripcion == null || descripcion.isBlank()) {
			return;
		}

		List<String> lineas = descripcion.replaceAll("<[^>]*>", "\n").replace("&nbsp;", " ").replace("&amp;", "&")
				.lines().map(String::trim).filter(linea -> !linea.isBlank()).toList();

		String seccionActual = "";

		for (String linea : lineas) {

			String encabezado = identificarSeccion(linea);

			if (!encabezado.isEmpty()) {
				seccionActual = encabezado;
				continue;
			}

			switch (seccionActual) {

			case "obligatorios":
				resultado.getRequisitosObligatorios().add(linea);

				if (esFormacion(linea)) {
					resultado.getFormacionRequerida().add(linea);
				}

				break;

			case "deseables":
				resultado.getRequisitosDeseables().add(linea);
				break;

			case "habilidades":
				resultado.getHabilidadesRequeridas().add(linea);
				break;

			case "formacion":
				resultado.getFormacionRequerida().add(linea);
				break;

			case "experiencia":
				resultado.getExperienciaRequerida().add(linea);
				break;
			}
		}
	}

	private String identificarSeccion(String linea) {

		String normalizada = Normalizer.normalize(linea, Normalizer.Form.NFD).replaceAll("\\p{M}", "")
				.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9 ]", "").trim();

		if (normalizada.matches(".*(requisitos obligatorios|excluyentes).*")) {
			return "obligatorios";
		}

		if (normalizada.matches(".*(requisitos deseables|no excluyentes|valorados).*")) {
			return "deseables";
		}

		if (normalizada.matches(".*(habilidades|competencias).*")) {
			return "habilidades";
		}

		if (normalizada.matches(".*(formacion academica|educacion).*")) {
			return "formacion";
		}

		if (normalizada.matches(".*(experiencia laboral|experiencia requerida).*")) {
			return "experiencia";
		}

		return "";
	}

	private boolean esFormacion(String linea) {

		String texto = Normalizer.normalize(linea, Normalizer.Form.NFD).replaceAll("\\p{M}", "")
				.toLowerCase(Locale.ROOT);

		return texto.matches(
				".*(secundario|terciario|universitario|grado|licenciatura|titulo|tecnicatura|posgrado|maestria|doctorado).*");
	}
}
