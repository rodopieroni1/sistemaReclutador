package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sistemaReclutador.sistemaReclutador.dto.AnalisisCompatibilidadRequestDTO;
import com.sistemaReclutador.sistemaReclutador.dto.CompatibilidadDTO;
import com.sistemaReclutador.sistemaReclutador.services.impl.CompatibilidadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/carina-ai")
@RequiredArgsConstructor
public class CompatibilidadController {

    private final CompatibilidadService compatibilidadService;

    @PostMapping("/analizar-compatibilidad")
    public ResponseEntity<CompatibilidadDTO> analizarCompatibilidad(
            @RequestBody AnalisisCompatibilidadRequestDTO request) {

        CompatibilidadDTO resultado = compatibilidadService.analizarCompatibilidad(
                request.getOferta(),
                request.getCv()
        );

        return ResponseEntity.ok(resultado);
    }
}