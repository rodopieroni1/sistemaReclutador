
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

        // 2. Analizar y organizar la información
        CvAnalisisDTO analisis = cvAnalizadorService.analizar(texto);

        // 3. Devolver el resultado
        return analisis;
    }
}
