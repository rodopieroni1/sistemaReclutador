package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CvExtractorService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String extraerTexto(String documentoUrl) throws IOException {

        if (documentoUrl == null || documentoUrl.isBlank()) {
            throw new IllegalArgumentException("El postulante no tiene un CV cargado.");
        }

        String prefijo = "/uploads/documentos/";

        if (!documentoUrl.startsWith(prefijo)) {
            throw new IllegalArgumentException("La ruta del CV no es válida.");
        }

        String nombreArchivo = documentoUrl.substring(prefijo.length());

        Path directorio = Paths.get(uploadDir)
                .resolve("documentos")
                .toAbsolutePath()
                .normalize();

        Path archivo = directorio.resolve(nombreArchivo).normalize();

        if (!archivo.startsWith(directorio)) {
            throw new IllegalArgumentException("Ruta de archivo no permitida.");
        }

        if (!Files.exists(archivo) || !Files.isRegularFile(archivo)) {
            throw new IOException("No se encontró el archivo del CV.");
        }

        String extension = obtenerExtension(nombreArchivo);

        switch (extension) {
            case "pdf":
                return extraerPdf(archivo.toFile());

            case "doc":
                return extraerDoc(archivo.toFile());

            case "docx":
                return extraerDocx(archivo.toFile());

            default:
                throw new IllegalArgumentException(
                        "Formato de CV no soportado: " + extension);
        }
    }

    private String extraerPdf(File archivo) throws IOException {

        try (PDDocument documento = Loader.loadPDF(archivo)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(documento);
        }
    }

    private String extraerDoc(File archivo) throws IOException {

        try (FileInputStream input = new FileInputStream(archivo);
             HWPFDocument documento = new HWPFDocument(input);
             WordExtractor extractor = new WordExtractor(documento)) {

            return extractor.getText();
        }
    }

    private String extraerDocx(File archivo) throws IOException {

        try (FileInputStream input = new FileInputStream(archivo);
             XWPFDocument documento = new XWPFDocument(input);
             XWPFWordExtractor extractor = new XWPFWordExtractor(documento)) {

            return extractor.getText();
        }
    }

    private String obtenerExtension(String nombreArchivo) {

        int punto = nombreArchivo.lastIndexOf('.');

        if (punto < 0 || punto == nombreArchivo.length() - 1) {
            throw new IllegalArgumentException(
                    "El archivo no tiene una extensión válida.");
        }

        return nombreArchivo.substring(punto + 1)
                .toLowerCase(Locale.ROOT);
    }
}
