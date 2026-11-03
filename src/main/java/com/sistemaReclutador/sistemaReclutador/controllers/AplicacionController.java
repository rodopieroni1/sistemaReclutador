package com.sistemaReclutador.sistemaReclutador.controllers;

import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionesMiasResponse;
import com.sistemaReclutador.sistemaReclutador.dto.CambiarEstadoRequestDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.services.AplicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aplicaciones")
@RequiredArgsConstructor
@Validated
public class AplicacionController {

    private final AplicacionService aplicacionService;

    @GetMapping
    public ResponseEntity<List<Aplicacion>> getAllAplicaciones() {
        return ResponseEntity.ok(aplicacionService.findAllDesc());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Aplicacion>> getAllAplicacionesActivas() {
        return ResponseEntity.ok(aplicacionService.findAllDescActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boolean> getAplicacionById(@PathVariable Long id) {
        return ResponseEntity.ok(aplicacionService.existsById(id));
    }

    @GetMapping("/perfil/{idPerfil}")
    public ResponseEntity<List<AplicacionesMiasResponse>> obtenerAsignaciones(
            @PathVariable Integer idPerfil,
            @RequestParam(defaultValue = "standardMappingStrategy") String format) {
        return ResponseEntity.ok(aplicacionService.obtenerAplicacionesPerfil(idPerfil, format));
    }
    
    @PostMapping
    public ResponseEntity<AplicacionResponseDTO> createAplicacion(@RequestBody AplicacionRequest aplicacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aplicacionService.crearAplicacion(aplicacion));
    }

    @PatchMapping("/estado/{idPost}")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long idPost, 
            @RequestBody CambiarEstadoRequestDTO request) {
        aplicacionService.cambiarEstado(idPost, request.estado());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAplicacion(@PathVariable Long id) {
        aplicacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

 
}