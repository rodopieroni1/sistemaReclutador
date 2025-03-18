package com.sistemaReclutador.sistemaReclutador.controllers;

import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ofertas")
public class OfertaController {

    @Autowired
    private OfertaRepository ofertaRepository;

    // Obtener todas las ofertas
    @GetMapping
    public List<Oferta> getAllOfertas() {
        return ofertaRepository.findAll();
    }

    // Obtener una oferta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Oferta> getOfertaById(@PathVariable int id) {
        return ofertaRepository.findById(id)
                .map(oferta -> ResponseEntity.ok().body(oferta))
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva oferta
    @PostMapping
    public Oferta createOferta(@RequestBody Oferta oferta) {
        return ofertaRepository.save(oferta);
    }

    // Actualizar una oferta existente
    @PutMapping("/{id}")
    public ResponseEntity<Oferta> updateOferta(@PathVariable int id, @RequestBody Oferta ofertaDetails) {
        return ofertaRepository.findById(id)
                .map(oferta -> {
                    oferta.setDescripcionOferta(ofertaDetails.getDescripcionOferta());
                    //oferta.setEmpresa(ofertaDetails.getEmpresa());
                    Oferta updatedOferta = ofertaRepository.save(oferta);
                    return ResponseEntity.ok().body(updatedOferta);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOferta(@PathVariable int id) {
        return ofertaRepository.findById(id)
                .map(oferta -> {ofertaRepository.delete(oferta);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
