package com.sistemaReclutador.sistemaReclutador.controllers;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.OfertaService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes desde el frontend
@RestController
@RequestMapping("/ofertas")
public class OfertaController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OfertaController.class);
    
    @Autowired
    private OfertaRepository ofertaRepository;
    @Autowired
    private OfertaService ofertaService;
    
    
    @GetMapping("/disponibles")
    public List<Oferta> getAllOfertas() {
        return ofertaService.findAllOfertas();
    }
    
    @GetMapping("/todas")
    public List<Oferta> getAllOfertasDesc() {
        return ofertaService.findAllOfertasActivas();
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
    	System.out.println("OFERTAAAAAA: "+ isOferta.getDescripcionOferta());
    	return isOferta;
    }

    // Crear una nueva oferta
   @PostMapping("/crear")
    public ResponseEntity<ResponseRest<Oferta>> createOferta(@RequestBody OfertaRequest ofertaRequest) {
	   return ofertaService.saveOferta(ofertaRequest);
    }   
    
   
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ResponseRest<Oferta>> updateOferta(@PathVariable Long id, @RequestBody OfertaRequest ofertaDetails) {
    	return ofertaService.updateOferta(id, ofertaDetails);
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
            @RequestParam(required = false) String descripcionEmpresa,
            @RequestParam(required = false) String descripcionRubro) {

        if (nombreOferta != null && descripcionEmpresa != null && descripcionRubro != null) {
            return ofertaRepository.buscarPorCampos(nombreOferta, descripcionEmpresa, descripcionRubro);
        }

        if (nombreOferta != null && descripcionRubro != null) {
            return ofertaRepository.buscarPorNombreYRubro(nombreOferta, descripcionRubro);
        }

        if (descripcionEmpresa != null && descripcionRubro != null) {
            return ofertaRepository.buscarPorDescripcionYRubro(descripcionEmpresa, descripcionRubro);
        }
        if (descripcionRubro != null) {
            return ofertaRepository.buscarPorRubro(descripcionRubro);
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
