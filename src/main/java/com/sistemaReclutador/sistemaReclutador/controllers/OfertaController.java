package com.sistemaReclutador.sistemaReclutador.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.OfertaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ofertas")
@RequiredArgsConstructor
public class OfertaController {

	private final OfertaService ofertaService;

	@GetMapping("/disponibles")
	public ResponseEntity<List<Oferta>> getAllOfertas() {
		return ResponseEntity.ok(ofertaService.findAllOfertas());
	}

	@GetMapping("/todas/activas")
	public ResponseEntity<List<Oferta>> getAllOfertasDesc() {
		return ResponseEntity.ok(ofertaService.findAllOfertasActivas());
	}

	@GetMapping
	public ResponseEntity<List<Oferta>> getAllOfertasEmpresa() {
		return ResponseEntity.ok(ofertaService.findEmpresaByOferta());
	}

	@GetMapping("/existeId/{id}")
	public ResponseEntity<Oferta> obtenerOferta(@PathVariable Long id) {
		return ResponseEntity.ok(ofertaService.obtenerOferta(id));
	}


	@PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseRest<Oferta>> createOferta(@Valid @ModelAttribute OfertaRequest ofertaRequest) {
	    Oferta ofertaCreada = ofertaService.saveOferta(ofertaRequest);
	    ResponseRest<Oferta> response = new ResponseRest<>(
	        true, 
	        "Oferta creada satisfactoriamente", 
	        ofertaCreada, 
	        LocalDateTime.now(), 
	        "201"
	    );
	    
	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	@PutMapping(value = "/actualizar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseRest<Oferta>> updateOferta(
			@PathVariable Long id,
			@RequestParam("nombreOferta") String nombreOferta,
			@RequestParam("descripcionOferta") String descripcionOferta,
			@RequestParam("estadoOferta") boolean estadoOferta,
			@RequestParam("idEmpresa") Long idEmpresa,
			@RequestParam(value = "fotoOferta", required = false) String fotoOferta,
			@RequestParam(value = "fotoArchivo", required = false) MultipartFile fotoArchivo) {

		Oferta ofertaActualizada = ofertaService.updateOferta(id, nombreOferta, descripcionOferta, estadoOferta, idEmpresa, fotoOferta, fotoArchivo);
		ResponseRest<Oferta> response = new ResponseRest<>(true, "Oferta actualizada satisfactoriamente", ofertaActualizada, LocalDateTime.now(), "200");
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<ResponseRest<Void>> eliminarOferta(@PathVariable Long id) {
		ofertaService.eliminarOferta(id);
		ResponseRest<Void> response = new ResponseRest<>(true, "Oferta eliminada satisfactoriamente", null, LocalDateTime.now(), "200");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/buscar")
	public ResponseEntity<List<Oferta>> buscarOferta(
			@RequestParam(required = false) String nombreOferta,
			@RequestParam(required = false) String descripcionEmpresa,
			@RequestParam(required = false) String descripcionRubro) {
		return ResponseEntity.ok(ofertaService.buscarPorCampo(nombreOferta, descripcionEmpresa, descripcionRubro));
	}
}