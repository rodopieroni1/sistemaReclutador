package com.sistemaReclutador.sistemaReclutador.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;


@Service
public interface OfertaService {
	
	ResponseEntity<ResponseRest<Oferta>> updateOferta(Long id, OfertaRequest ofertaDetail);
	ResponseEntity<ResponseRest<Oferta>> saveOferta( OfertaRequest ofertaDetail);

}
