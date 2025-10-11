package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.RubroRepository;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;

@Service
public class RubroServiceImpl implements RubroService {

    @Autowired
    private RubroRepository rubroRepository;

 

    @Override
    public Rubro obtenerRubroPorId(int id) {
        return rubroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rubro no encontrado con ID: " + id));
    }

    @Override
    public List<Rubro> listarRubros() {
        return rubroRepository.findAll();
    }

    @Override
    public ResponseEntity<Rubro> actualizarRubro(int id, RubroRequest request) {
        Rubro rubro = obtenerRubroPorId(id);
        rubro.setDescripcionRubro(request.getDescripcionRubro());
        rubroRepository.save(rubro);
        return null;
    }

    @Override
    public ResponseEntity<Rubro> eliminarRubro(int id) {
        rubroRepository.deleteById(id);
		return null;
    }

	@Override
	public Rubro crearRubro(RubroRequest request) {
		Rubro rubro = new Rubro();
        rubro.setDescripcionRubro(request.getDescripcionRubro());
        return rubroRepository.save(rubro);
	}
}
