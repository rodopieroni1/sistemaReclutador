package com.sistemaReclutador.sistemaReclutador.controllers;

import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes desde el frontend
@RestController
@RequestMapping("/rubro")
public class RubroController {

    @Autowired
    private RubroService rubroService;

    @GetMapping
    public List<Rubro> getAllRubros() {
        return rubroService.listarRubros();
    }

    @GetMapping("/{id}")
    public Rubro getRubroById(@PathVariable int id) {
        return rubroService.obtenerRubroPorId(id);
    }

    @PostMapping("/crear")
    public  ResponseEntity<Rubro> createRubro(@RequestBody RubroRequest rubro) {
         rubroService.crearRubro(rubro);
         return null;
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Rubro> updateRubro(@PathVariable int id, @RequestBody RubroRequest rubro) {
        return rubroService.actualizarRubro(id, rubro);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Rubro> deleteRubro(@PathVariable int id) {
        return rubroService.eliminarRubro(id);
    }
}
