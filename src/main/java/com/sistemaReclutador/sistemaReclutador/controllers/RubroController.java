package com.sistemaReclutador.sistemaReclutador.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rubro")
@RequiredArgsConstructor
@Validated
public class RubroController {

	private final RubroService rubroService;

	@GetMapping
	public ResponseEntity<List<Rubro>> getAllRubros() {
		return ResponseEntity.ok(rubroService.listarRubros());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Rubro> getRubroById(@PathVariable Integer id) {
		return ResponseEntity.ok(rubroService.obtenerRubroPorId(id));
	}

	@PostMapping("/crear")
	public ResponseEntity<ResponseRest<Rubro>> createRubro(@Valid @RequestBody RubroRequest rubroRequest) {
		Rubro nuevoRubro = rubroService.crearRubro(rubroRequest);
		ResponseRest<Rubro> response = new ResponseRest<>(true, "Rubro creado satisfactoriamente", nuevoRubro, LocalDateTime.now(), "201");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/actualizar/{id}")
	public ResponseEntity<ResponseRest<Rubro>> updateRubro(@PathVariable Integer id, @Valid @RequestBody RubroRequest rubroRequest) {
		Rubro rubroActualizado = rubroService.actualizarRubro(id, rubroRequest);
		ResponseRest<Rubro> response = new ResponseRest<>(true, "Rubro modificado satisfactoriamente", rubroActualizado, LocalDateTime.now(), "200");
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<ResponseRest<Void>> deleteRubro(@PathVariable Integer id) {
		rubroService.eliminarRubro(id);
		ResponseRest<Void> response = new ResponseRest<>(true, "Rubro eliminado satisfactoriamente", null, LocalDateTime.now(), "200");
		return ResponseEntity.ok(response);
	}
}