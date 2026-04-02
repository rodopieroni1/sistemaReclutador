package com.sistemaReclutador.sistemaReclutador.controllers;

import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionesMiasResponse;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.repositories.AplicacionRepository;
import com.sistemaReclutador.sistemaReclutador.services.AplicacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes desde el frontend
@RestController
@RequestMapping("/aplicaciones")
public class AplicacionController {
	boolean active = true;

    @Autowired
    private AplicacionService aplicacionService;
    @Autowired
    private AplicacionRepository aplicacionRepository;

    @GetMapping
    public List<Aplicacion> getAllAplicaciones() {
        return aplicacionRepository.findAllDesc();
    }

    @GetMapping("/{id}")
    public boolean getAplicacionById(@PathVariable Integer id) {
    	boolean existeAplicacion = aplicacionService.existsById(id);
    	return existeAplicacion;
    }

    @PostMapping
    public Aplicacion createAplicacion(@RequestBody AplicacionRequest aplicacion) {
       return aplicacionService.crearAplicacion(aplicacion);
    }

    @PutMapping("/{id}")
    public Aplicacion updateAplicacion(@PathVariable int id, @RequestBody Aplicacion aplicacionDetails) {
   	 Optional<Aplicacion> aplicacionActualizado = aplicacionRepository.findById(id);
	        if (aplicacionActualizado.isPresent()) {
	        	Aplicacion aplicacion = aplicacionActualizado.get();
	        	aplicacion.setOferta(aplicacionDetails.getOferta());
	        	aplicacion.setPerfil(aplicacionDetails.getPerfil());
	            aplicacion.setEstadoaplicaciones(aplicacionDetails.isEstadoaplicaciones());
	        	aplicacion.setFecha(aplicacionDetails.getFecha());
	        	return aplicacionRepository.save(aplicacion);
	        }
	        return null; // Devuelve null si el usuario no existe
    }
    
    @PatchMapping("/estado/{idPost}")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestBody Map<String, Boolean> body) {
        boolean estado = body.get("estado");
        aplicacionService.cambiarEstado(id, estado);
        return ResponseEntity.ok().build();
    }
    
    
    @DeleteMapping("/{id}")
    public void deleteAplicacion(@PathVariable int id) {
        aplicacionRepository.deleteById(id);
    }
    
    @GetMapping("/perfil/{idPerfil}")
    public List<AplicacionesMiasResponse> obtenerAsignaciones(@PathVariable int idPerfil) {
    	List<Object[]>resultado = aplicacionService.obtenerAplicacionesPerfil(idPerfil);
    	
    	return resultado.stream()
    			.map(obj-> new AplicacionesMiasResponse(
    					(Integer) obj[0],
    					(String) obj[1],
    					(String) obj[2],
    					(LocalDateTime) obj[3],
    					(Boolean) obj[4]
    					))
    			.toList();
    }
}

