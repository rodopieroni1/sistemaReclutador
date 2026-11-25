package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import com.sistemaReclutador.sistemaReclutador.dto.CvAnalisisDTO;

@Service
public class CvAnalizadorService {

	public CvAnalisisDTO analizar(String texto) {
		if (texto == null || texto.isBlank()) {
			throw new IllegalArgumentException("No hay texto disponible para analizar.");
		}

		CvAnalisisDTO resultado = new CvAnalisisDTO();
		resultado.setExperienciasLaborales(new ArrayList<>());
		resultado.setHabilidades(new ArrayList<>());
		resultado.setFormacionAcademica(new ArrayList<>());
		resultado.setCertificaciones(new ArrayList<>());
		resultado.setIdiomas(new ArrayList<>());

		String textoNormalizado = texto.replace("\t", "\n")
				.replaceAll("(?i)(?=EXPERIENCIA LABORAL|FORMACION ACADEMICA|CURSOS:?|PERFIL:?)", "\n");

		List<String> lineas = textoNormalizado.lines().map(String::trim).filter(linea -> !linea.isBlank()).toList();

		String seccionActual = "";

		for (String linea : lineas) {
			String encabezado = identificarSeccion(linea);
			if (!encabezado.isEmpty()) {
				seccionActual = encabezado;
				continue;
			}

			switch (seccionActual) {

			case "experiencia":
				resultado.getExperienciasLaborales().add(linea);
				break;

			case "habilidades":
				resultado.getHabilidades().add(linea);
				break;

			case "formacion":
				resultado.getFormacionAcademica().add(linea);
				break;

			case "certificaciones":
				resultado.getCertificaciones().add(linea);
				break;

			case "idiomas":
				resultado.getIdiomas().add(linea);
				break;
			}
		}

		return resultado;
	}


private String identificarSeccion(String linea) {

    String normalizada = Normalizer
            .normalize(linea, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9 ]", "")
            .replaceAll("\\s+", " ")
            .trim();

    switch (normalizada) {

        case "experiencia laboral":
        case "experiencia profesional":
        case "experiencia":
            return "experiencia";

        case "formacion academica":
        case "formacion":
        case "educacion":
        case "estudios":
            return "formacion";

        case "cursos":
        case "certificaciones":
        case "capacitaciones":
            return "certificaciones";

        case "perfil":
        case "habilidades":
        case "competencias":
        case "conocimientos":
            return "habilidades";

        case "idiomas":
        case "lenguas":
            return "idiomas";

        default:
            return "";
    }
}

}