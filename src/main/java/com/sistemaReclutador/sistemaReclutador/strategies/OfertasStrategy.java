package com.sistemaReclutador.sistemaReclutador.strategies;

import java.util.List;

import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;

public interface OfertasStrategy {
	
	boolean aplica(String nombre, String empresa, String rubro);
    List<Oferta> buscar(OfertaRepository repository, String nombre, String empresa, String rubro);

}
