
package com.sistemaReclutador.sistemaReclutador;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaAnalisisDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.services.impl.OfertaAnalizadorService;

@SpringBootTest
class OfertaAnalizadorServiceTest {

    @Autowired
    private OfertaAnalizadorService analizador;

    @Test
    void probarAnalisisOferta() {

        Oferta oferta = new Oferta();

        oferta.setNombreOferta("Analista administrativo");

        oferta.setDescripcionOferta("""
                Buscamos una persona para el área administrativa.

                REQUISITOS OBLIGATORIOS:
                Manejo de herramientas informáticas.
                Secundario completo.

                REQUISITOS DESEABLES:
                Experiencia en administración.

                HABILIDADES:
                Organización y trabajo en equipo.

                EXPERIENCIA REQUERIDA:
                Dos años en puestos similares.
                """);

        OfertaAnalisisDTO resultado = analizador.analizar(oferta);

        System.out.println("\n========== ANÁLISIS DE OFERTA ==========");
        System.out.println("OFERTA: " + resultado.getNombreOferta());
        System.out.println("OBLIGATORIOS: " + resultado.getRequisitosObligatorios());
        System.out.println("DESEABLES: " + resultado.getRequisitosDeseables());
        System.out.println("HABILIDADES: " + resultado.getHabilidadesRequeridas());
        System.out.println("FORMACIÓN: " + resultado.getFormacionRequerida());
        System.out.println("EXPERIENCIA: " + resultado.getExperienciaRequerida());
        System.out.println("========================================");
    }
}
