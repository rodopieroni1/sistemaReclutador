package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.RubroRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;

@Service
public class RubroServiceImpl implements RubroService {

	@Autowired
	private RubroRepository rubroRepository;

	private void validarCamposRubro(RubroRequest request) {
		if (request.getDescripcionRubro() == null || request.getDescripcionRubro().trim().isEmpty()) {
			throw new IllegalArgumentException("La descripción del rubro es obligatoria y no puede estar vacía.");
		}

		if (request.getDescripcionRubro().length() > 100) {
			throw new IllegalArgumentException("La descripción del rubro no puede superar los 100 caracteres.");
		}
	}

	@Override
	public ResponseEntity<ResponseRest<Rubro>> crearRubro(RubroRequest request) {
		try {
			validarCamposRubro(request);
			Rubro rubro = new Rubro();
			rubro.setDescripcionRubro(request.getDescripcionRubro().trim());
			Rubro rubroCreate = rubroRepository.save(rubro);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseRest<>(true,
					"Rubro creado satisfactoriamente", rubroCreate, LocalDateTime.now(), "200"));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseRest<>(false, e.getMessage(), null, LocalDateTime.now(), "400"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseRest<>(false,
					"Error al crear el rubro: " + e.getMessage(), null, LocalDateTime.now(), "500"));
		}
	}

	@Override
	public ResponseEntity<ResponseRest<Rubro>> actualizarRubro(int id, RubroRequest request) {
	    try {
	        validarCamposRubro(request);
	        Rubro rubro = obtenerRubroPorId(id);
	        rubro.setDescripcionRubro(request.getDescripcionRubro().trim());
	        Rubro rubroActualizado = rubroRepository.save(rubro);
	        return ResponseEntity.ok(
	            new ResponseRest<>(true, "Rubro modificado satisfactoriamente", rubroActualizado, LocalDateTime.now(), "200")
	        );
	        
	    } catch (IllegalArgumentException e) {
	        System.err.println("Error de validación en actualización: " + e.getMessage());
	        return ResponseEntity.badRequest().body(
	            new ResponseRest<>(false, e.getMessage(), null, LocalDateTime.now(), "400")
	        );
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().body(
	            new ResponseRest<>(false, "Error interno: " + e.getMessage(), null, LocalDateTime.now(), "500")
	        );
	    }
	}


	@Override
	public Rubro obtenerRubroPorId(int id) {
		try {
			return rubroRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Rubro no encontrado con ID: " + id));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error al obtener el rubro: " + e.getMessage());
		}
	}

	@Override
	public List<Rubro> listarRubros() {
		try {
			return rubroRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error al listar los rubros: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> eliminarRubro(int id) {
		try {
			rubroRepository.deleteById(id);
			return ResponseEntity.ok("Rubro eliminado correctamente");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("No se puede eliminar el rubro porque tiene empresas asociadas.");
		}
	}

	@Override
	public Rubro findRubroEmpresa(Long idRubro) {
		try {
			return rubroRepository.findById(idRubro).orElseThrow(() -> new RuntimeException("Rubro no encontrado"));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error al buscar el rubro: " + e.getMessage());
		}
	}

	@Override
	public Rubro findRubro(Long intValue) {
		try {
			return rubroRepository.findById(intValue).orElseThrow(() -> new RuntimeException("Rubro no encontrado"));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error al buscar el rubro: " + e.getMessage());
		}
	}
}
