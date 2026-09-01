package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;

public interface OfertaService {

	Oferta obtenerOferta(Long id);
	Oferta saveOferta(OfertaRequest ofertaDetail);
	Oferta updateOferta(Long id, String nombreOferta, String descripcionOferta,
			boolean estadoOferta, Long idEmpresa, String fotoOferta, MultipartFile fotoArchivo);
	void eliminarOferta(Long id);
	List<Oferta> findAllOfertas();
	List<Oferta> findAllOfertasActivas();
	List<Oferta> findEmpresaByOferta();
	List<Oferta> buscarPorCampo(String nombreOferta, String descripcionEmpresa, String descripcionRubro);
}
