package com.sistemaReclutador.sistemaReclutador.controllers;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import org.slf4j.LoggerFactory;
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
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OfertaController.class);
    
    @Autowired
    private OfertaRepository ofertaRepository;
    // Obtener todas las ofertas
    @GetMapping("/todas")
    public List<Oferta> getAllOfertasDesc() {
    	List<Oferta> isOferta= ofertaRepository.findAllDesc();
        logger.info("Ofertas obtenidas: {}", isOferta);
        return isOferta;
    }
    
    @GetMapping
    public List<Oferta> getAllOfertasEmpresa() {
        return ofertaRepository.findEmpresaByOferta();
    }

    // Obtener una oferta por ID
    @GetMapping("/existeId/{id}")
    public Oferta obtenerOferta(@PathVariable Long id) {
    	Oferta isOferta;
    	isOferta=ofertaRepository.findOferta(id);
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
    	oferta.setNombreOferta(dto.getNombreOferta());
    	oferta.setDescripcionOferta(dto.getDescripcionOferta());
    	oferta.setEmpresa(dto.getIdEmpresa());
    	oferta.setFotoOferta(dto.getFotoOferta());
    	return oferta;
    }

    // Actualizar una oferta existente
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Oferta> updateOferta(@PathVariable Long id, @RequestBody OfertaRequest ofertaDetails) {
    	 boolean existe = ofertaRepository.findByIdOferta(id);
    	 System.out.println("Pasando pasando"+ ofertaDetails);
         if(existe) {
         	Oferta oferta = (ofertaRepository.findById(id)).get();
         	oferta.setDescripcionOferta(ofertaDetails.getDescripcionOferta());
         	oferta.setEmpresa(ofertaDetails.getIdEmpresa()); // Asignar la entidad Empresa
         	oferta.setFotoOferta(ofertaDetails.getFotoOferta());
         	oferta.setEstadoOferta(ofertaDetails.isEstadoOferta());
         	oferta.setNombreOferta(ofertaDetails.getNombreOferta());
            Oferta updatedOferta = ofertaRepository.save(oferta);
       	 System.out.println("IF"+ updatedOferta.getClass().descriptorString());

            return ResponseEntity.ok().body(updatedOferta);
         }else{
         	ResponseEntity.notFound().build();
       	 System.out.println("Else");

         	return null;
         	}      
    }
    
    @DeleteMapping("/eliminar/{id}")
    public void eliminarOferta(@PathVariable Long id) {
     	Oferta oferta = (ofertaRepository.findById(id)).get();
     	oferta.setEstadoOferta(true);
     	ofertaRepository.save(oferta);
    }
    
    @GetMapping("/buscar")
    public List<Oferta> buscarOferta(
            @RequestParam(required = false) String nombreOferta,
            @RequestParam(required = false) String descripcionEmpresa) {

        if (nombreOferta != null && descripcionEmpresa != null) {
            return ofertaRepository.buscarPorCampos(nombreOferta, descripcionEmpresa);
        }
        if (nombreOferta != null) {
            return ofertaRepository.buscarPorNombreOferta(nombreOferta);
        }
        if (descripcionEmpresa != null) {
            return ofertaRepository.buscarPorDescripcionEmpresa(descripcionEmpresa);
        }
        return ofertaRepository.findAll();
    }


}
