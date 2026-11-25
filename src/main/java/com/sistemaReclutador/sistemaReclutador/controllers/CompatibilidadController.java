package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.sistemaReclutador.sistemaReclutador.dto.AnalisisCompatibilidadRequestDTO;
import com.sistemaReclutador.sistemaReclutador.dto.CompatibilidadDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import com.sistemaReclutador.sistemaReclutador.services.impl.CarinaAIService;
import com.sistemaReclutador.sistemaReclutador.services.impl.CompatibilidadService;
import com.sistemaReclutador.sistemaReclutador.services.impl.OfertaAnalizadorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/carina-ai")
@RequiredArgsConstructor
public class CompatibilidadController {

    private final CompatibilidadService compatibilidadService;
    private final CarinaAIService carinaAIService;
    private final OfertaAnalizadorService ofertaAnalizadorService;
    private final PerfilRepository perfilRepository;
    private final OfertaRepository ofertaRepository;

    // Endpoint existente: se conserva
    @PostMapping("/analizar-compatibilidad")
    public ResponseEntity<CompatibilidadDTO> analizarCompatibilidad(
            @RequestBody AnalisisCompatibilidadRequestDTO request) {

        CompatibilidadDTO resultado = compatibilidadService.analizarCompatibilidad(
                request.getOferta(),
                request.getCv()
        );

        return ResponseEntity.ok(resultado);
    }

    // Nuevo endpoint: analiza la compatibilidad del postulante autenticado
    @PostMapping("/ofertas/{idOferta}/mi-compatibilidad")
    public ResponseEntity<CompatibilidadDTO> analizarMiCompatibilidad(
            @PathVariable Long idOferta,
            Authentication authentication) throws Exception {

        // 1. Obtener el postulante autenticado mediante su clave
        Perfil perfil = perfilRepository.findByClave(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró el perfil del postulante"));

        // 2. Verificar que tenga un CV cargado
        if (perfil.getDocumentoUrl() == null || perfil.getDocumentoUrl().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Primero tenés que cargar o crear tu CV");
        }

        // 3. Buscar la oferta
        Oferta oferta = ofertaRepository.findById(idOferta)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró la oferta"));

        // 4. Verificar que la oferta esté activa
        if (!oferta.isEstadoOferta()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "La oferta no está disponible");
        }

        // 5. Analizar el CV guardado del postulante
        var cvAnalizado = carinaAIService.analizarCv(perfil.getDocumentoUrl());

        // 6. Analizar la oferta
        var ofertaAnalizada = ofertaAnalizadorService.analizar(oferta);
        
        System.out.println("========== OFERTA ANALIZADA ==========");
        System.out.println("EXPERIENCIA: " + ofertaAnalizada.getExperienciaRequerida());
        System.out.println("HABILIDADES: " + ofertaAnalizada.getHabilidadesRequeridas());
        System.out.println("FORMACIÓN: " + ofertaAnalizada.getFormacionRequerida());
        System.out.println("OBLIGATORIOS: " + ofertaAnalizada.getRequisitosObligatoriosCategorizados());
        System.out.println("DESEABLES: " + ofertaAnalizada.getRequisitosDeseablesCategorizados());
        System.out.println("======================================");

        // 7. Calcular la compatibilidad
        CompatibilidadDTO resultado = compatibilidadService.analizarCompatibilidad(
                ofertaAnalizada,
                cvAnalizado,
                perfil
        );
        return ResponseEntity.ok(resultado);
    }
}