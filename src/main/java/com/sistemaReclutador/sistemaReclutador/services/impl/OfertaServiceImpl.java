package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.io.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.exceptions.ResourceNotFoundException;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import com.sistemaReclutador.sistemaReclutador.services.OfertaService;
import com.sistemaReclutador.sistemaReclutador.strategies.OfertasStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfertaServiceImpl implements OfertaService {

	private final OfertaRepository ofertaRepository;
	private final EmpresaRepository empresaRepository;
	private final List<OfertasStrategy> ofertaStrategy;

	@Value("${app.upload.dir}")
	private String uploadDir;

	@Transactional
	@Override
	public Oferta saveOferta(OfertaRequest ofertaDetail) {
	    if (ofertaDetail == null) {
	        throw new IllegalArgumentException("Los datos de la oferta no pueden ser nulos.");
	    }
	    Oferta oferta = convertirDtoAEntidad(ofertaDetail);
	    oferta.setIdOferta(null);
	    MultipartFile foto = ofertaDetail.getFotoArchivo();
	    if (foto != null && !foto.isEmpty()) {
	        String nombreGuardado = guardarArchivoFoto(foto);
	        oferta.setFotoOferta(nombreGuardado);
	    }
	    return ofertaRepository.save(oferta);
	}

	@Transactional
	@Override
	public Oferta updateOferta(Long id, String nombreOferta, String descripcionOferta, boolean estadoOferta,
			Long idEmpresa, String fotoOferta, MultipartFile fotoArchivo) {
		Oferta ofertaUpdate = ofertaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró la oferta con ID: " + id));
		ofertaUpdate.setNombreOferta(nombreOferta);
		ofertaUpdate.setDescripcionOferta(descripcionOferta);
		ofertaUpdate.setEstadoOferta(estadoOferta);
		if (idEmpresa != null) {
			Empresa empresa = empresaRepository.findById(idEmpresa)
					.orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + idEmpresa));
			ofertaUpdate.setEmpresa(empresa);
		}
		if (fotoArchivo != null && !fotoArchivo.isEmpty()) {
			String nombreGuardado = guardarArchivoFoto(fotoArchivo);
			ofertaUpdate.setFotoOferta(nombreGuardado);
		} else if (fotoOferta != null) {
			ofertaUpdate.setFotoOferta(fotoOferta);
		}
		return ofertaRepository.save(ofertaUpdate);
	}

	@Transactional
	@Override
	public void eliminarOferta(Long id) {
		Oferta oferta = ofertaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró la oferta con ID: " + id));
		oferta.setEstadoOferta(false); // Eliminación lógica
		ofertaRepository.save(oferta);
	}

	@Override
	@Transactional(readOnly = true)
	public Oferta obtenerOferta(Long id) {
		return ofertaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada con ID: " + id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Oferta> findAllOfertas() {
		return ofertaRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Oferta> findAllOfertasActivas() {
		return ofertaRepository.findAllDesc();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Oferta> findEmpresaByOferta() {
		return ofertaRepository.findEmpresaByOferta();
	}

	@Override
    @Transactional(readOnly = true)
    public List<Oferta> buscarPorCampo(String nombreOferta, String descripcionEmpresa, String descripcionRubro) {
        String nombre = (nombreOferta != null && !nombreOferta.trim().isEmpty()) ? nombreOferta.trim() : null;
        String empresa = (descripcionEmpresa != null && !descripcionEmpresa.trim().isEmpty()) ? descripcionEmpresa.trim() : null;
        String rubro = (descripcionRubro != null && !descripcionRubro.trim().isEmpty()) ? descripcionRubro.trim() : null;
        return ofertaStrategy.stream()
                .filter(strategy -> strategy.aplica(nombre, empresa, rubro))
                .findFirst()
                .map(strategy -> strategy.buscar(ofertaRepository, nombre, empresa, rubro))
                .orElseGet(ofertaRepository::findAll);
    }
	
	private Oferta convertirDtoAEntidad(OfertaRequest dto) {
		Oferta oferta = new Oferta();
		oferta.setNombreOferta(dto.getNombreOferta());
		oferta.setDescripcionOferta(dto.getDescripcionOferta());
		oferta.setEstadoOferta(dto.isEstadoOferta());
		oferta.setFotoOferta(dto.getFotoOferta());
		if (dto.getIdEmpresa() != null && dto.getIdEmpresa().getId_empresa() != null) {
			Empresa empresa = empresaRepository.findById(dto.getIdEmpresa().getId_empresa())
					.orElseThrow(() -> new ResourceNotFoundException("Empresa asociada no encontrada."));
			oferta.setEmpresa(empresa);
		}

		return oferta;
	}

	private String guardarArchivoFoto(MultipartFile foto) {
		try {
			File ofertaDir = new File(uploadDir + "ofertas/");
			if (!ofertaDir.exists()) {
				ofertaDir.mkdirs();
			}
			String originalFilename = Objects.requireNonNullElse(foto.getOriginalFilename(), "oferta.png");
			String fileFoto = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
			File fotoFile = new File(ofertaDir, fileFoto);
			foto.transferTo(fotoFile);
			return fileFoto;
		} catch (IOException e) {
			log.error("Error al guardar la foto de la oferta: {}", e.getMessage(), e);
			throw new RuntimeException("No se pudo guardar la imagen de la oferta.", e);
		}
	}

}