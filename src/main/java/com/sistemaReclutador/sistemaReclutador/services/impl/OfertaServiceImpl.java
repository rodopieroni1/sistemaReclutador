package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.OfertaService;

@Service
public class OfertaServiceImpl implements OfertaService {

	@Value("${app.base.url}")
	private String appBaseUrl;

	@Value("${app.upload.dir}")
	private String uploadDir;

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
	public ResponseEntity<ResponseRest<Oferta>> updateOferta(Long id, String nombreOferta, String descripcionOferta,
			boolean estadoOferta, Long idEmpresa, String fotoOferta, MultipartFile fotoArchivo) {
		ResponseRest<Oferta> response;
		try {
			OfertaRequest ofertaDetails = new OfertaRequest();
			ofertaDetails.setNombreOferta(nombreOferta);
			ofertaDetails.setDescripcionOferta(descripcionOferta);
			ofertaDetails.setEstadoOferta(estadoOferta);
			ofertaDetails.setFotoOferta(fotoOferta);
			ofertaDetails.setFotoArchivo(fotoArchivo);

			if (idEmpresa != null) {
				Empresa empresa = new Empresa();
				empresa.setId_empresa(idEmpresa);
				ofertaDetails.setIdEmpresa(empresa);
			}

			Optional<Oferta> ofertaOptional = ofertaRepository.findById(id);
			if (ofertaOptional.isPresent()) {
				Oferta ofertaUpdate = ofertaOptional.get();

				ofertaUpdate.setNombreOferta(ofertaDetails.getNombreOferta());
				ofertaUpdate.setDescripcionOferta(ofertaDetails.getDescripcionOferta());
				ofertaUpdate.setEstadoOferta(ofertaDetails.isEstadoOferta());

				if (ofertaDetails.getIdEmpresa() != null) {
					ofertaUpdate.setEmpresa(ofertaDetails.getIdEmpresa());
				}

				MultipartFile foto = ofertaDetails.getFotoArchivo();
	            if (foto != null && !foto.isEmpty()) {
	                String fileFoto = foto.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
	                File ofertaDir = new File(uploadDir + "ofertas/");
	                if (!ofertaDir.exists()) {
	                    ofertaDir.mkdirs();
	                }
	                
	                File fotoFile = new File(ofertaDir, fileFoto);
	                foto.transferTo(fotoFile); // Transferencia física
	                
	                // Seteamos la URL pública en la entidad para persistir en BD
	                ofertaUpdate.setFotoOferta(fileFoto);
	            } else {
	                // Si no se adjuntó archivo, preservamos la foto existente enviada desde el front
	                ofertaUpdate.setFotoOferta(ofertaDetails.getFotoOferta());
	            }

	            // 4. Guardar la entidad actualizada
	            ofertaRepository.save(ofertaUpdate);
	            
	            response = new ResponseRest<>(true, "Oferta actualizada satisfactoriamente", ofertaUpdate, LocalDateTime.now(), "200"); 
	            return ResponseEntity.ok(response);
	            
	        } else { 
	            response = new ResponseRest<>(false, "No se pudo encontrar la oferta para actualizar", null, LocalDateTime.now(), "404"); 
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); 
	        } 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	        response = new ResponseRest<>(false, "Ocurrió un error al actualizar la oferta: " + e.getMessage(), null, LocalDateTime.now(), "500"); 
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); 
	    } 
	}

	@Override
	public ResponseEntity<ResponseRest<Oferta>> saveOferta(OfertaRequest ofertaDetail) {
		ResponseRest<Oferta> response;
		try {
			Oferta oferta = convertirDtoAEntidad(ofertaDetail);
			Oferta ofertaCreate = ofertaRepository.save(oferta);
			if (ofertaCreate != null) {
				response = new ResponseRest<>(true, "Oferta creada satisfactoriamente", ofertaCreate,
						LocalDateTime.now(), "200");
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			} else {
				response = new ResponseRest<>(false, "No se pudo crear la oferta", null, LocalDateTime.now(), "400");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseRest<>(false, "Ocurrió un error al crear la oferta: " + e.getMessage(), null,
					LocalDateTime.now(), "500");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@Override
	public List<Oferta> findAllOfertas() {
		try {
			return ofertaRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error al obtener las ofertas: " + e.getMessage());
		}
	}

	@Override
	public List<Oferta> findAllOfertasActivas() {
		try {
			return ofertaRepository.findAllDesc();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error al obtener las ofertas activas: " + e.getMessage());
		}
	}

}