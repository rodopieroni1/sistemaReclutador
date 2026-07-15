package com.sistemaReclutador.sistemaReclutador.controllers;

import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
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

@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes desde el frontend
@RestController
@RequestMapping("/aplicaciones")
public class AplicacionController {
	boolean active = true;

	@Autowired
	private AplicacionService aplicacionService;
	

	@GetMapping
	public List<Aplicacion> getAllAplicaciones() {
		return aplicacionService.findAllDesc();
	}
	
	@GetMapping("/activas")
	public List<Aplicacion> getAllAplicacionesActivas() {
		return aplicacionService.findAllDescActivas();
	}

	@GetMapping("/{id}")
	public boolean getAplicacionById(@PathVariable Integer id) {
		return aplicacionService.existsById(id);
	}

	@PostMapping
	public AplicacionResponseDTO createAplicacion(@RequestBody AplicacionRequest aplicacion) {
		return aplicacionService.crearAplicacion(aplicacion);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateAplicacion(@PathVariable int id, @RequestBody AplicacionRequest aplicacionDetails) {
		aplicacionService.modificarOferta(id, aplicacionDetails);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/estado/{idPost}")
	public ResponseEntity<?> cambiarEstado(@PathVariable Integer idPost, @RequestBody Map<String, Boolean> body) {
		boolean estado = body.get("estado");
		aplicacionService.cambiarEstado(idPost, estado);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public void deleteAplicacion(@PathVariable int id) {
		aplicacionService.deleteById(id);
	}

	@GetMapping("/perfil/{idPerfil}")
	public List<AplicacionesMiasResponse> obtenerAsignaciones(@PathVariable int idPerfil) {
		List<Object[]> resultado = aplicacionService.obtenerAplicacionesPerfil(idPerfil);
		return resultado.stream().map(obj -> new AplicacionesMiasResponse((Integer) obj[0], // idaplicacion
				(String) obj[1], // puesto / nombreOferta
				(String) obj[2], // empresa
				(LocalDateTime) obj[3], // fecha
				(Boolean) obj[4], // estado
				(String) obj[5], // descripcionOferta
				(String) obj[6], // fotoOferta
				(String) obj[7], // email
				(String) obj[8], // telefono
				(String) obj[9], // direccion
				(Long) obj[10] // idOferta
		)).toList();
	}

}
