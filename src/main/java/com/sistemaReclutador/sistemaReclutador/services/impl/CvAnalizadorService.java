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
            throw new IllegalArgumentException(
                    "No hay texto disponible para analizar.");
        }
    
        CvAnalisisDTO resultado = new CvAnalisisDTO();
        resultado.setExperienciasLaborales(new ArrayList<>());
        resultado.setHabilidades(new ArrayList<>());
        resultado.setFormacionAcademica(new ArrayList<>());
        resultado.setCertificaciones(new ArrayList<>());
        resultado.setIdiomas(new ArrayList<>());

        List<String> lineas = texto.lines()
                .map(String::trim)
                .filter(linea -> !linea.isBlank())
                .toList();

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
                .trim();

        if (normalizada.matches(
                ".*(experiencia laboral|experiencia profesional|experiencia).*")) {
            return "experiencia";
        }

        if (normalizada.matches(
                ".*(habilidades|competencias|conocimientos).*")) {
            return "habilidades";
        }

        if (normalizada.matches(
                ".*(formacion academica|formacion|educacion|estudios|titulo academico|titulos|nivel educativo|antecedentes academicos).*")) {
            return "formacion";
        }

        if (normalizada.matches(
                ".*(certificaciones|cursos|capacitaciones).*")) {
            return "certificaciones";
        }

        if (normalizada.matches(
                ".*(idiomas|lenguas).*")) {
            return "idiomas";
        }

        return "";
    }
}