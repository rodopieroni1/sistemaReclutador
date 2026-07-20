package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Rubro> actualizarRubro(int id, RubroRequest request) {
        try {
            Rubro rubro = obtenerRubroPorId(id);
            rubro.setDescripcionRubro(request.getDescripcionRubro());
            rubroRepository.save(rubro);

            return ResponseEntity.ok(rubro);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
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
    public Rubro crearRubro(RubroRequest request) {
        try {
            Rubro rubro = new Rubro();
            rubro.setDescripcionRubro(request.getDescripcionRubro());

            return rubroRepository.save(rubro);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el rubro: " + e.getMessage());
        }
    }

    @Override
    public Rubro findRubroEmpresa(Long idRubro) {
        try {
            return rubroRepository.findById(idRubro)
                    .orElseThrow(() -> new RuntimeException("Rubro no encontrado"));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar el rubro: " + e.getMessage());
        }
    }

    @Override
    public Rubro findRubro(Long intValue) {
        try {
            return rubroRepository.findById(intValue)
                    .orElseThrow(() -> new RuntimeException("Rubro no encontrado"));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar el rubro: " + e.getMessage());
        }
    }
}
