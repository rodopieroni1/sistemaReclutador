package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;

@Service
public interface RubroService {
	ResponseEntity<ResponseRest<Rubro>> crearRubro(RubroRequest request);
	ResponseEntity<ResponseRest<Rubro>> actualizarRubro(int id, RubroRequest request);


	Rubro obtenerRubroPorId(int id);
	List<Rubro> listarRubros();
	ResponseEntity<String> eliminarRubro(int id);
	Rubro findRubroEmpresa(Long idRubro);
	Rubro findRubro(Long intValue);
}
