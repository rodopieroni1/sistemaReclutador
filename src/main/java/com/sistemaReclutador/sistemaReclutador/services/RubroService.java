package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;

@Service
public interface RubroService {
	Rubro crearRubro(RubroRequest request);
    Rubro obtenerRubroPorId(int id);
    List<Rubro> listarRubros();
    ResponseEntity<Rubro> actualizarRubro(int id, RubroRequest request);
    ResponseEntity<String> eliminarRubro(int id);
	Rubro findRubroEmpresa(Long idRubro);
	Rubro findRubro(int intValue);
}

