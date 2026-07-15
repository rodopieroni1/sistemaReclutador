package com.sistemaReclutador.sistemaReclutador.controllers;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.OfertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/ofertas")
public class OfertaController {
	
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
		return ofertaService.findEmpresaByOferta();
	}

	@GetMapping("/existeId/{id}")
	public Oferta obtenerOferta(@PathVariable Long id) {
		return ofertaService.obtenerOferta(id);
	}

	@PostMapping("/crear")
	public ResponseEntity<ResponseRest<Oferta>> createOferta(@RequestBody OfertaRequest ofertaRequest) {
		return ofertaService.saveOferta(ofertaRequest);
	}

	@PutMapping(value = "/actualizar/{id}", consumes = { "multipart/form-data" })
	public ResponseEntity<ResponseRest<Oferta>> updateOferta(@PathVariable Long id,
			@RequestParam("nombreOferta") String nombreOferta,
			@RequestParam("descripcionOferta") String descripcionOferta,
			@RequestParam("estadoOferta") boolean estadoOferta, @RequestParam("idEmpresa") Long idEmpresa,
			@RequestParam("fotoOferta") String fotoOferta,
			@RequestParam(value = "fotoArchivo", required = false) MultipartFile fotoArchivo) {
		return ofertaService.updateOferta(id, nombreOferta, descripcionOferta, estadoOferta, idEmpresa, fotoOferta,
				fotoArchivo);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<ResponseRest<Oferta>> eliminarOferta(@PathVariable Long id) {
		return ofertaService.eliminarOferta(id);
	}

	@GetMapping("/buscar")
	public List<Oferta> buscarOferta(@RequestParam(required = false) String nombreOferta,
			@RequestParam(required = false) String descripcionEmpresa,
			@RequestParam(required = false) String descripcionRubro) {
		return ofertaService.buscarPorCampo(nombreOferta, descripcionEmpresa, descripcionRubro);
	}

}
