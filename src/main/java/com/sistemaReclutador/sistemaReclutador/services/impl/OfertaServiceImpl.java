package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.OfertaService;

@Service
public class OfertaServiceImpl implements OfertaService {
	@Autowired
	private OfertaRepository ofertaRepository;
	public Oferta convertirDtoAEntidad(OfertaRequest dto) {
		Oferta oferta = new Oferta();
		oferta.setNombreOferta(dto.getNombreOferta());
		oferta.setDescripcionOferta(dto.getDescripcionOferta());
		oferta.setEmpresa(dto.getIdEmpresa());
		oferta.setEstadoOferta(dto.isEstadoOferta());
		oferta.setFotoOferta(dto.getFotoOferta());
		return oferta;
	}
	
	@Override
	public ResponseEntity<ResponseRest<Oferta>> updateOferta(Long id, OfertaRequest ofertaDetails) {
		boolean existe = ofertaRepository.findByIdOferta(id);
		ResponseRest<Oferta> response;
		Oferta ofertaUpdate = (ofertaRepository.findById(id)).get();
		if (existe) {
			ofertaUpdate.setDescripcionOferta(ofertaDetails.getDescripcionOferta());
			ofertaUpdate.setEmpresa(ofertaDetails.getIdEmpresa());
			ofertaUpdate.setFotoOferta(ofertaDetails.getFotoOferta());
			ofertaUpdate.setEstadoOferta(ofertaDetails.isEstadoOferta());
			ofertaUpdate.setNombreOferta(ofertaDetails.getNombreOferta());
			ofertaRepository.save(ofertaUpdate);
			response = new ResponseRest<Oferta>(true, "Empresa Acutalizada satisfactoriamente", ofertaUpdate,
					LocalDateTime.now(), "200");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} else {
			response = new ResponseRest<Oferta>(false, "No se pudo Actualizar la empresa", null, LocalDateTime.now(),
					"400");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<ResponseRest<Oferta>> saveOferta(OfertaRequest ofertaDetail) {
		Oferta oferta = convertirDtoAEntidad(ofertaDetail);
		Oferta ofertaCreate = ofertaRepository.save(oferta);
		ResponseRest<Oferta> response;
	 	if (ofertaCreate != null){        
		    response = new ResponseRest<Oferta> (true, "Oferta creada satisfactoriamente", 
		    		ofertaCreate, LocalDateTime.now(), "200");
		    return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}else {
	        response = new ResponseRest<>(false, "No se pudo crear la Oferta", null, LocalDateTime.now(), "400");
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);		
		}	

	}

}
