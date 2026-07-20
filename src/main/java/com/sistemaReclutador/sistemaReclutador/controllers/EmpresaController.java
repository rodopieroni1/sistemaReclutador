package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/empresas")
public class EmpresaController {

	@Autowired
	private EmpresaService empresaService;

	@GetMapping
	public Iterable<Empresa> listarEmpresas() {
		return empresaService.buscarPorEmpresa();
	}

	@GetMapping("/existe/{cuit}")
	public boolean obtenerEmpresaPorId(@PathVariable Long cuit) {
		return empresaService.existsByCuit(cuit);
	}

	@GetMapping("/existeId/{id}")
	public Empresa obtenerEmpresa(@PathVariable Long id) {
		return empresaService.findEmpresa(id);
	}

	@PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseRest<Empresa>> crearEmpresa(
	        @ModelAttribute EmpresaRequest empresaRequest) {
	    return empresaService.saveEmpresa(empresaRequest);
	}

	@PutMapping(value="/actualizar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<ResponseRest<Empresa>> actualizarEmpresa( @PathVariable Long id, @ModelAttribute EmpresaRequest request) {
		    return empresaService.updateEmpresa(id, request);
		}
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<ResponseRest<Empresa>> eliminarEmpresa(@PathVariable Long id) {
		return empresaService.deleteEmpresa(id);

	}
}
