package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaAnalisisDTO;
import com.sistemaReclutador.sistemaReclutador.dto.RequisitoAnalisisDTO;
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

        resultado.setModalidad(
            oferta.getModalidad() != null
                ? oferta.getModalidad().toString()
                : null
        );

        resultado.setLocalidad(oferta.getLocalidadTrabajo());
        resultado.setProvincia(oferta.getProvinciaTrabajo());
        resultado.setPais(oferta.getPaisTrabajo());

        resultado.setRequisitosObligatorios(new ArrayList<>());
        resultado.setRequisitosDeseables(new ArrayList<>());
        resultado.setHabilidadesRequeridas(new ArrayList<>());
        resultado.setFormacionRequerida(new ArrayList<>());
        resultado.setExperienciaRequerida(new ArrayList<>());

        resultado.setRequisitosObligatoriosCategorizados(new ArrayList<>());
        resultado.setRequisitosDeseablesCategorizados(new ArrayList<>());

        clasificarDescripcion(resultado);

        return resultado;
    }

    private void clasificarDescripcion(OfertaAnalisisDTO resultado) {

        String descripcion = resultado.getDescripcion();

        if (descripcion == null || descripcion.isBlank()) {
            return;
        }

        List<String> lineas = descripcion
            .replaceAll("(?i)<br\\s*/?>", "\n")
            .replaceAll("(?i)</(p|li|ul|ol|div|h[1-6])>", "\n")
            .replaceAll("<[^>]*>", "\n")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&bull;", "•")
            .lines()
            .map(String::trim)
            .filter(linea -> !linea.isBlank())
            .toList();

        String condicionActual = "obligatorios";
        String categoriaActual = null;
        boolean dentroDeRequisitos = false;

        for (String linea : lineas) {

            String normalizada = normalizar(linea);

            // Ignorar los datos generales de la oferta.
            if (esDatoGeneral(normalizada)) {
                dentroDeRequisitos = false;
                categoriaActual = null;
                continue;
            }

            // La descripción general no es un requisito.
            if (normalizada.equals("descripcion de la oferta")) {
                dentroDeRequisitos = false;
                categoriaActual = null;
                continue;
            }

            // Detectar encabezados de condición.
            if (normalizada.equals("requisitos obligatorios")
                    || normalizada.equals("requisitos excluyentes")) {

                condicionActual = "obligatorios";
                categoriaActual = null;
                dentroDeRequisitos = true;
                continue;
            }

            if (normalizada.equals("requisitos deseables")
                    || normalizada.equals("requisitos no excluyentes")) {

                condicionActual = "deseables";
                categoriaActual = null;
                dentroDeRequisitos = true;
                continue;
            }

            // Detectar encabezados de categoría.
            String categoria = identificarCategoria(normalizada);

            if (categoria != null) {

                categoriaActual = categoria;
                dentroDeRequisitos = true;

                // Los encabezados "requeridos" implican
                // condición obligatoria por defecto.
                condicionActual = "obligatorios";

                continue;
            }

            // No analizar información que no pertenece
            // a una sección de requisitos.
            if (!dentroDeRequisitos) {
                continue;
            }

            // Cada requisito puede indicar su propia condición.
            String condicionDetectada = identificarCondicion(normalizada);

            String condicion = condicionDetectada != null
                ? condicionDetectada
                : condicionActual;

            // Si estamos en una sección genérica,
            // inferimos la categoría a partir del requisito.
            String categoriaFinal = categoriaActual != null
                ? categoriaActual
                : inferirCategoria(normalizada);

            agregarRequisito(
                resultado,
                limpiarRequisito(linea),
                categoriaFinal,
                condicion
            );
        }
    }

    private boolean esDatoGeneral(String texto) {

        return texto.equals("nombre de la oferta")
            || texto.equals("empresa")
            || texto.equals("localidad")
            || texto.equals("modalidad");
    }

    private String identificarCondicion(String texto) {

        if (texto.contains("se valorara")
                || texto.contains("se valoraran")
                || texto.contains("deseable")
                || texto.contains("no excluyente")
                || texto.contains("no excluyentes")
                || texto.contains("valorado")
                || texto.contains("valorada")) {

            return "deseables";
        }

        if (texto.contains("obligatorio")
                || texto.contains("excluyente")
                || texto.contains("indispensable")) {

            return "obligatorios";
        }

        return null;
    }

    private String identificarCategoria(String texto) {

        if (texto.equals("formacion requerida")
                || texto.equals("formacion academica")
                || texto.equals("educacion")
                || texto.equals("estudios")) {

            return "FORMACION";
        }

        if (texto.equals("experiencia requerida")
                || texto.equals("experiencia laboral")
                || texto.equals("experiencia profesional")) {

            return "EXPERIENCIA";
        }

        if (texto.equals("habilidades requeridas")
                || texto.equals("habilidades")
                || texto.equals("competencias")) {

            return "HABILIDADES";
        }

        if (texto.equals("cursos")
                || texto.equals("certificaciones")
                || texto.equals("capacitaciones")) {

            return "CERTIFICACIONES";
        }

        if (texto.equals("idiomas")
                || texto.equals("lenguas")) {

            return "IDIOMAS";
        }

        return null;
    }

    private String inferirCategoria(String texto) {

        if (texto.contains("titulo")
                || texto.contains("universitario")
                || texto.contains("secundario")
                || texto.contains("terciario")
                || texto.contains("licenciatura")
                || texto.contains("tecnicatura")
                || texto.contains("formacion academica")
                || texto.contains("abogado")
                || texto.contains("procurador")) {

            return "FORMACION";
        }

        if (texto.contains("experiencia")
                || texto.contains("anos de experiencia")
                || texto.contains("antiguedad")) {

            return "EXPERIENCIA";
        }

        if (texto.contains("curso")
                || texto.contains("certificacion")
                || texto.contains("capacitacion")
                || texto.contains("seminario")
                || texto.contains("taller")) {

            return "CERTIFICACIONES";
        }

        if (texto.contains("idioma")
                || texto.contains("ingles")
                || texto.contains("portugues")
                || texto.contains("frances")) {

            return "IDIOMAS";
        }

        // Los conocimientos, capacidades y competencias
        // se clasifican como habilidades.
        return "HABILIDADES";
    }

    private void agregarRequisito(
        OfertaAnalisisDTO resultado,
        String descripcion,
        String categoria,
        String condicion
    ) {

        if (descripcion == null || descripcion.isBlank()) {
            return;
        }

        RequisitoAnalisisDTO requisito = new RequisitoAnalisisDTO();
        requisito.setCategoria(categoria);
        requisito.setDescripcion(descripcion);

        List<RequisitoAnalisisDTO> listaCategorizada;
        List<String> listaSimple;

        if ("deseables".equals(condicion)) {

            listaCategorizada = resultado.getRequisitosDeseablesCategorizados();
            listaSimple = resultado.getRequisitosDeseables();

        } else {

            listaCategorizada = resultado.getRequisitosObligatoriosCategorizados();
            listaSimple = resultado.getRequisitosObligatorios();
        }

        boolean repetido = listaCategorizada.stream().anyMatch(r ->
            r.getCategoria().equals(categoria)
                && normalizar(r.getDescripcion()).equals(normalizar(descripcion))
        );

        if (repetido) {
            return;
        }

        listaCategorizada.add(requisito);
        listaSimple.add(descripcion);

        switch (categoria) {

            case "EXPERIENCIA":
                resultado.getExperienciaRequerida().add(descripcion);
                break;

            case "HABILIDADES":
                resultado.getHabilidadesRequeridas().add(descripcion);
                break;

            case "FORMACION":
                resultado.getFormacionRequerida().add(descripcion);
                break;
        }
    }

    private String limpiarRequisito(String texto) {

        return texto
            .replaceFirst("^[•\\-–*]+\\s*", "")
            .trim();
    }

    private String normalizar(String texto) {

        return Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9 ]", " ")
            .replaceAll("\\s+", " ")
            .trim();
    }
}