package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

@Service
public class GeneradorCvService {

    public byte[] generarPlantillaDocx() throws IOException {

        try (XWPFDocument documento = new XWPFDocument();
             ByteArrayOutputStream salida = new ByteArrayOutputStream()) {

            // Título principal
            XWPFParagraph titulo = documento.createParagraph();
            titulo.setAlignment(ParagraphAlignment.CENTER);
            titulo.setSpacingAfter(200);

            XWPFRun runTitulo = titulo.createRun();
            runTitulo.setText("CURRICULUM VITAE");
            runTitulo.setBold(true);
            runTitulo.setFontSize(22);
            runTitulo.setFontFamily("Arial");
            runTitulo.setColor("243B53");

            agregarSubtitulo(documento,
                    "Completá los siguientes campos con tu información personal.");

            // Datos personales
            agregarSeccion(documento, "DATOS PERSONALES");
            agregarCampo(documento, "Nombre y apellido:");
            agregarCampo(documento, "DNI:");
            agregarCampo(documento, "Fecha de nacimiento:");
            agregarCampo(documento, "Dirección:");
            agregarCampo(documento, "Teléfono:");
            agregarCampo(documento, "Correo electrónico:");

            // Perfil profesional
            agregarSeccion(documento, "PERFIL PROFESIONAL");
            agregarCampo(documento,
                    "Describí brevemente tu perfil, objetivos y fortalezas:");
            agregarEspacio(documento, 3);

            // Experiencia laboral
            agregarSeccion(documento, "EXPERIENCIA LABORAL");

            agregarCampo(documento, "Empresa:");
            agregarCampo(documento, "Puesto:");
            agregarCampo(documento, "Fecha de inicio y finalización:");
            agregarCampo(documento, "Responsabilidades y tareas realizadas:");
            agregarEspacio(documento, 2);

            agregarCampo(documento, "Empresa:");
            agregarCampo(documento, "Puesto:");
            agregarCampo(documento, "Fecha de inicio y finalización:");
            agregarCampo(documento, "Responsabilidades y tareas realizadas:");

            // Formación académica
            agregarSeccion(documento, "FORMACIÓN ACADÉMICA");
            agregarCampo(documento, "Institución:");
            agregarCampo(documento, "Título o estudio:");
            agregarCampo(documento, "Estado (completo/en curso):");
            agregarCampo(documento, "Año:");

            // Habilidades
            agregarSeccion(documento, "HABILIDADES Y CONOCIMIENTOS");
            agregarCampo(documento, "Conocimientos informáticos:");
            agregarCampo(documento, "Habilidades personales:");
            agregarCampo(documento, "Otros conocimientos:");

            // Idiomas
            agregarSeccion(documento, "IDIOMAS");
            agregarCampo(documento, "Idioma:");
            agregarCampo(documento, "Nivel:");

            // Referencias
            agregarSeccion(documento, "REFERENCIAS");
            agregarCampo(documento, "Nombre y apellido:");
            agregarCampo(documento, "Empresa:");
            agregarCampo(documento, "Teléfono de contacto:");

            documento.write(salida);

            return salida.toByteArray();
        }
    }

    private void agregarSubtitulo(
            XWPFDocument documento, String texto) {

        XWPFParagraph parrafo = documento.createParagraph();
        parrafo.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun run = parrafo.createRun();
        run.setText(texto);
        run.setItalic(true);
        run.setFontSize(10);
        run.setFontFamily("Arial");
    }

    private void agregarSeccion(
            XWPFDocument documento, String titulo) {

        XWPFParagraph parrafo = documento.createParagraph();
        parrafo.setSpacingBefore(250);
        parrafo.setSpacingAfter(100);

        XWPFRun run = parrafo.createRun();
        run.setText(titulo);
        run.setBold(true);
        run.setFontSize(13);
        run.setFontFamily("Arial");
        run.setColor("243B53");
    }

    private void agregarCampo(
            XWPFDocument documento, String texto) {

        XWPFParagraph parrafo = documento.createParagraph();
        parrafo.setSpacingAfter(100);

        XWPFRun run = parrafo.createRun();
        run.setText(texto + " ______________________________");
        run.setFontSize(10);
        run.setFontFamily("Arial");
    }

    private void agregarEspacio(
            XWPFDocument documento, int cantidad) {

        for (int i = 0; i < cantidad; i++) {
            documento.createParagraph();
        }
    }
}
