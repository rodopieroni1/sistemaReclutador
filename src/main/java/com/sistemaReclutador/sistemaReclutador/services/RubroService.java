package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;
import com.sistemaReclutador.sistemaReclutador.dto.RubroRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;

public interface RubroService {

	Rubro crearRubro(RubroRequest request);
	Rubro actualizarRubro(Integer id, RubroRequest request);
	Rubro obtenerRubroPorId(Integer id);
	List<Rubro> listarRubros();
	void eliminarRubro(Integer id);
	// Estandarizado a Integer para coincidir con la entidad y DTOs
	Rubro findRubro(Integer id);
	Rubro findRubroEmpresa(Integer id);
}