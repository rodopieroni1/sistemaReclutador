package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;

@Service
public interface OfertaService {
	
	Oferta obtenerOferta(Long id);
	ResponseEntity<ResponseRest<Oferta>> saveOferta( OfertaRequest ofertaDetail);
	List<Oferta> findAllOfertas();
	List<Oferta> findAllOfertasActivas();
	List<Oferta> findEmpresaByOferta();
	Oferta findOferta(Long id);
	ResponseEntity<ResponseRest<Oferta>> updateOferta(Long id, String nombreOferta, String descripcionOferta,
			boolean estadoOferta, Long idEmpresa, String fotoOferta, MultipartFile fotoArchivo);
	ResponseEntity<ResponseRest<Oferta>> eliminarOferta(Long id);
	List<Oferta> buscarPorCampo(String nombreOferta, String descripcionEmpresa, String descripcionRubro);

}
