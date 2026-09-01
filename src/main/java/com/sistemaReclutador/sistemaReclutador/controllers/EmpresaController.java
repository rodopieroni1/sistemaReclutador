package com.sistemaReclutador.sistemaReclutador.controllers;

import java.time.LocalDateTime;

import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
@Validated
public class EmpresaController {

	private final EmpresaService empresaService;

	@GetMapping
	public ResponseEntity<Iterable<Empresa>> listarEmpresas() {
		return ResponseEntity.ok(empresaService.buscarPorEmpresa());
	}

	@GetMapping("/existe/{cuit}")
	public ResponseEntity<Boolean> existePorCuit(@PathVariable Long cuit) {
		return ResponseEntity.ok(empresaService.existsByCuit(cuit));
	}

	@GetMapping("/existeId/{id}")
	public ResponseEntity<Empresa> obtenerEmpresaPorId(@PathVariable Long id) {
		return ResponseEntity.ok(empresaService.findEmpresa(id));
	}

	@PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseRest<Empresa>> crearEmpresa(@Valid @ModelAttribute EmpresaRequest empresaRequest) {
		Empresa empresaCreada = empresaService.saveEmpresa(empresaRequest);
		ResponseRest<Empresa> response = new ResponseRest<>(true, "Empresa creada satisfactoriamente", empresaCreada, LocalDateTime.now(), "201");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping(value = "/actualizar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseRest<Empresa>> actualizarEmpresa(@PathVariable Long id, @Valid @ModelAttribute EmpresaRequest request) {
		Empresa empresaActualizada = empresaService.updateEmpresa(id, request);
		ResponseRest<Empresa> response = new ResponseRest<>(true, "Empresa actualizada satisfactoriamente", empresaActualizada, LocalDateTime.now(), "200");
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<ResponseRest<Void>> eliminarEmpresa(@PathVariable Long id) {
		empresaService.deleteEmpresa(id);
		ResponseRest<Void> response = new ResponseRest<>(true, "Empresa eliminada satisfactoriamente", null, LocalDateTime.now(), "200");
		return ResponseEntity.ok(response);
	}
}