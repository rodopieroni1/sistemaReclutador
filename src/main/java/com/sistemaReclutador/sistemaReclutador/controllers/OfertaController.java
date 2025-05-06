package com.sistemaReclutador.sistemaReclutador.controllers;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes desde el frontend
@RestController
@RequestMapping("/ofertas")
public class OfertaController {

    @Autowired
    private OfertaRepository ofertaRepository;
    // Obtener todas las ofertas
    @GetMapping("/todas")
    public List<Oferta> getAllOfertasDesc() {
        return ofertaRepository.findAllDesc();
    }
    
    @GetMapping
    public List<Oferta> getAllOfertasEmpresa() {
        return ofertaRepository.findEmpresaByOferta();
    }

    // Obtener una oferta por ID
    @GetMapping("/existeId/{id}")
    public Oferta obtenerOferta(@PathVariable Long id) {
    	Oferta isOferta;
    	System.out.println("LISTADO: ");

    	isOferta=ofertaRepository.findOferta(id);
    	System.out.println("descripcion: "+ isOferta.getDescripcionOferta());
    	System.out.println("foto: "+ isOferta.getFotoOferta());

    	System.out.println("idOferta: "+ isOferta.getIdOferta());

    	System.out.println("empresa: "+ isOferta.getEmpresa().getId_empresa());

    	return isOferta;
    }
    
                 

    
    // Crear una nueva oferta
   @PostMapping("/crear")
    public ResponseEntity<Map<String, String>> createOferta(@RequestBody OfertaRequest ofertaRequest) {
    	Oferta oferta = convertirDtoAEntidad(ofertaRequest);
    	boolean isUserCreate = ofertaRepository.save(oferta) != null;
		 Map<String, String> response = new HashMap<>();
    	if (ofertaRequest.getDescripcionOferta() != null){        
 		if(isUserCreate) {
 		    response.put("message", "Oferta creada satisfactoriamente");
 		    return ResponseEntity.status(HttpStatus.CREATED).body(response);
 		}else {
 		    response.put("message", "No se pudo Crear la Oferta");
 		    return ResponseEntity.status(HttpStatus.CREATED).body(response);		
 		}
    	}else {
 		    response.put("message", "No se pudo Crear la Oferta");
 		    return ResponseEntity.status(HttpStatus.CREATED).body(response);		
 		}
    	
    }
    
    
    
    public Oferta convertirDtoAEntidad(OfertaRequest dto) {
    	Oferta oferta = new Oferta();
    	oferta.setDescripcionOferta(dto.getDescripcionOferta());
    	oferta.setEmpresa(dto.getIdEmpresa());
    	oferta.setFotoOferta(dto.getFotoOferta());
    	return oferta;
    }

    // Actualizar una oferta existente
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Oferta> updateOferta(@PathVariable Long id, @RequestBody OfertaRequest ofertaDetails) {
    	 boolean existe = ofertaRepository.findByIdOferta(id);
         if(existe) {
         	Oferta oferta = (ofertaRepository.findById(id)).get();
         	oferta.setDescripcionOferta(ofertaDetails.getDescripcionOferta());
         	oferta.setEmpresa(ofertaDetails.getIdEmpresa()); // Asignar la entidad Empresa
         	oferta.setIdOferta(ofertaDetails.getIdOferta());
         	oferta.setFotoOferta(ofertaDetails.getFotoOferta());
            Oferta updatedOferta = ofertaRepository.save(oferta);
            return ResponseEntity.ok().body(updatedOferta);
         }else{
         	ResponseEntity.notFound().build();
         	return null;
         	}      
    }
    
    @DeleteMapping("/eliminar/{id}")
    public void eliminarOferta(@PathVariable Long id) {
    	ofertaRepository.deleteById(id);
    	
    }
}
