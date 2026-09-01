package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.exceptions.ResourceNotFoundException;
import com.sistemaReclutador.sistemaReclutador.repositories.RubroRepository;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RubroServiceImpl implements RubroService {

	private final RubroRepository rubroRepository;

	@Transactional
	@Override
	public Rubro crearRubro(RubroRequest request) {
		Rubro rubro = new Rubro();
		rubro.setDescripcionRubro(request.getDescripcionRubro().trim());
		return rubroRepository.save(rubro);
	}

	@Transactional
	@Override
	public Rubro actualizarRubro(Integer id, RubroRequest request) {
		Rubro rubro = obtenerRubroPorId(id);
		rubro.setDescripcionRubro(request.getDescripcionRubro().trim());
		return rubroRepository.save(rubro);
	}

	@Override
	@Transactional(readOnly = true)
	public Rubro obtenerRubroPorId(Integer id) {
		return rubroRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Rubro no encontrado con ID: " + id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Rubro> listarRubros() {
		return rubroRepository.findAll();
	}

	@Transactional
	@Override
	public void eliminarRubro(Integer id) {
		if (!rubroRepository.existsById(id)) {
			throw new ResourceNotFoundException("No se encontró el rubro con ID: " + id);
		}
		rubroRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Rubro findRubroEmpresa(Integer idRubro) {
		return obtenerRubroPorId(idRubro);
	}

	@Override
	@Transactional(readOnly = true)
	public Rubro findRubro(Integer idRubro) {
		return obtenerRubroPorId(idRubro);
	}
}