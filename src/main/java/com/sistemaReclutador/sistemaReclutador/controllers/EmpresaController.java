package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

	@PostMapping("/crear")
	public ResponseEntity<ResponseRest<Empresa>> crearEmpresa(@RequestBody EmpresaRequest empresaRequest) {
		return empresaService.saveEmpresa(empresaRequest);
	}

	@PutMapping("/actualizar/{id}")
	public ResponseEntity<ResponseRest<Empresa>> updateOferta(@PathVariable Long id,
			@RequestBody EmpresaRequest empresaDetails) {
		return empresaService.updateEmpresa(id, empresaDetails);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<ResponseRest<Empresa>> eliminarEmpresa(@PathVariable Long id) {
		return empresaService.deleteEmpresa(id);

	}
}
