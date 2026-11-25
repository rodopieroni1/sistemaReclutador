
package com.sistemaReclutador.sistemaReclutador.services.impl;

import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.CvAnalisisDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarinaAIService {

    private final CvExtractorService cvExtractorService;
    private final CvAnalizadorService cvAnalizadorService;

    public CvAnalisisDTO analizarCv(String documentoUrl) throws Exception {

        // 1. Extraer el texto del CV
        String texto = cvExtractorService.extraerTexto(documentoUrl);

        System.out.println("========== TEXTO EXTRAÍDO DEL CV ==========");
        System.out.println(texto);
        System.out.println("===========================================");

        // 2. Analizar y organizar la información
        CvAnalisisDTO analisis = cvAnalizadorService.analizar(texto);

        System.out.println("========== CV ANALIZADO ==========");
        System.out.println("EXPERIENCIA: " + analisis.getExperienciasLaborales());
        System.out.println("FORMACIÓN: " + analisis.getFormacionAcademica());
        System.out.println("CERTIFICACIONES: " + analisis.getCertificaciones());
        System.out.println("HABILIDADES: " + analisis.getHabilidades());
        System.out.println("IDIOMAS: " + analisis.getIdiomas());
        System.out.println("==================================");
        
        

        // 3. Devolver el resultado
        return analisis;
    }
}
