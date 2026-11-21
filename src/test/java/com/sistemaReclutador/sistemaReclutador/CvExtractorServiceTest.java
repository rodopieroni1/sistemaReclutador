
package com.sistemaReclutador.sistemaReclutador;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistemaReclutador.sistemaReclutador.dto.CvAnalisisDTO;
import com.sistemaReclutador.sistemaReclutador.services.impl.CarinaAIService;

@SpringBootTest
class CvExtractorServiceTest {

    @Autowired
    private CarinaAIService carinaAIService;

    @Test
    void probarAnalisisCompletoCv() throws Exception {

        String ruta = "/uploads/documentos/CV_ClaudiaEsperguin__1___1___1___1_.docx";

        CvAnalisisDTO resultado = carinaAIService.analizarCv(ruta);

        System.out.println("\n========== ANÁLISIS CARINA AI ==========");

        System.out.println("\nEXPERIENCIA:");
        resultado.getExperienciasLaborales()
                .forEach(System.out::println);

        System.out.println("\nHABILIDADES:");
        resultado.getHabilidades()
                .forEach(System.out::println);

        System.out.println("\nFORMACIÓN:");
        resultado.getFormacionAcademica()
                .forEach(System.out::println);

        System.out.println("\nCERTIFICACIONES:");
        resultado.getCertificaciones()
                .forEach(System.out::println);

        System.out.println("\nIDIOMAS:");
        resultado.getIdiomas()
                .forEach(System.out::println);

        System.out.println("\n========== FIN DEL ANÁLISIS ==========");
    }
}
