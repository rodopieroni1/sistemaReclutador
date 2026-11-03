package com.sistemaReclutador.sistemaReclutador.services;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.dto.OfertaUpdateRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;

public interface OfertaService {

	Oferta obtenerOferta(Long id);
	Oferta saveOferta(OfertaRequest ofertaDetail, Authentication authentication);
	Oferta updateOferta(Long id, OfertaUpdateRequest request, Authentication authentication);
	void eliminarOferta(Long id);
	List<Oferta> findAllOfertas();
	List<Oferta> findAllOfertasActivas();
	List<Oferta> findEmpresaByOferta();
	boolean existsByEmpresa_Id_empresa(Long id);
	List<Oferta> buscarPorCampo(String nombreOferta, String descripcionEmpresa, String descripcionRubro);
}
