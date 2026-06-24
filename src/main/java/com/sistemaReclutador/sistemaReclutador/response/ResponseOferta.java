package com.sistemaReclutador.sistemaReclutador.response;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;

public class ResponseOferta {
	public Oferta convertirDtoAEntidad(OfertaRequest dto) {
		Oferta oferta = new Oferta();
		oferta.setNombreOferta(dto.getNombreOferta());
		oferta.setDescripcionOferta(dto.getDescripcionOferta());
		oferta.setEmpresa(dto.getIdEmpresa());
		oferta.setEstadoOferta(dto.isEstadoOferta());
		oferta.setFotoOferta(dto.getFotoOferta());
		return oferta;
	}
	
	
}
