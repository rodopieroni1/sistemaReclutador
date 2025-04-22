package com.sistemaReclutador.sistemaReclutador.controllers;

import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.repositories.AplicacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes desde el frontend
@RestController
@RequestMapping("/aplicaciones")
public class AplicacionController {

    @Autowired
    private AplicacionRepository aplicacionRepository;

    @GetMapping
    public List<Aplicacion> getAllAplicaciones() {
    	System.out.println("pasa: "+ aplicacionRepository.findAllDesc());
        return aplicacionRepository.findAllDesc();
    }

    @GetMapping("/{id}")
    public Optional<Aplicacion> getAplicacionById(@PathVariable int id) {
        return aplicacionRepository.findById(id);
    }

    @PostMapping
    public Aplicacion createAplicacion(@RequestBody Aplicacion aplicacion) {
        return aplicacionRepository.save(aplicacion);
    }

    @PutMapping("/{id}")
    public Aplicacion updateAplicacion(@PathVariable int id, @RequestBody Aplicacion aplicacionDetails) {
   	 Optional<Aplicacion> aplicacionActualizado = aplicacionRepository.findById(id);
	        if (aplicacionActualizado.isPresent()) {
	        	Aplicacion aplicacion = aplicacionActualizado.get();
	        	aplicacion.setOferta(aplicacionDetails.getOferta());
	        	//aplicacion.setPerfil(aplicacionDetails.getPerfil());
	        	aplicacion.setFecha(aplicacionDetails.getFecha());
	        	return aplicacionRepository.save(aplicacion);
	        }
	        return null; // Devuelve null si el usuario no existe
    }

    @DeleteMapping("/{id}")
    public void deleteAplicacion(@PathVariable int id) {
        aplicacionRepository.deleteById(id);
    }
}

