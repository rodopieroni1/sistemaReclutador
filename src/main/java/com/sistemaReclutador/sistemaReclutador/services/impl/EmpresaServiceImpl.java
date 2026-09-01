package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

	private static final List<String> EXTENSIONES_PERMITIDAS = List.of("image/png", "image/jpeg", "image/webp");

	private final EmpresaRepository empresaRepository;
	private final RubroService rubroService;

	@Value("${app.upload.dir}")
	private String uploadDir;

	@Transactional
	@Override
	public Empresa saveEmpresa(EmpresaRequest empresaRequest) {
		if (empresaRepository.existsByCuit(empresaRequest.getCuit())) {
			throw new IllegalArgumentException("El CUIT ya está registrado.");
		}
		if (empresaRepository.existsByEmail(empresaRequest.getEmail())) {
			throw new IllegalArgumentException("El email ya está registrado.");
		}

		Empresa empresa = convertirDtoAEntidad(empresaRequest);

		if (empresaRequest.getLogo() != null && !empresaRequest.getLogo().isEmpty()) {
			String nombreGuardado = guardarArchivoLogo(empresaRequest.getLogo());
			empresa.setLogo(nombreGuardado);
		}

		return empresaRepository.save(empresa);
	}

	@Transactional
	@Override
	public Empresa updateEmpresa(Long id, EmpresaRequest empresaDetails) {
		Empresa empresa = empresaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("La empresa con ID " + id + " no existe."));

		if (!empresa.getCuit().equals(empresaDetails.getCuit()) && empresaRepository.existsByCuit(empresaDetails.getCuit())) {
			throw new IllegalArgumentException("El nuevo CUIT ya se encuentra en uso.");
		}

		if (!empresa.getEmail().equalsIgnoreCase(empresaDetails.getEmail()) && empresaRepository.existsByEmail(empresaDetails.getEmail())) {
			throw new IllegalArgumentException("El nuevo email ya se encuentra en uso.");
		}

		Rubro rubro = rubroService.findRubro(empresaDetails.getIdRubro());
		if (rubro == null) {
			throw new IllegalArgumentException("El rubro especificado no existe.");
		}

		empresa.setCuit(empresaDetails.getCuit());
		empresa.setNombre(empresaDetails.getNombre());
		empresa.setEmail(empresaDetails.getEmail());
		empresa.setTelefono(empresaDetails.getTelefono());
		empresa.setDireccion(empresaDetails.getDireccion());
		empresa.setHistoriaEmpresa(empresaDetails.getHistoriaEmpresa());
		empresa.setObservaciones(empresaDetails.getObservaciones());
		empresa.setRubro(rubro);

		MultipartFile logo = empresaDetails.getLogo();
		if (logo != null && !logo.isEmpty()) {
			String nombreGuardado = guardarArchivoLogo(logo);
			empresa.setLogo(nombreGuardado);
		}

		return empresaRepository.save(empresa);
	}

	@Transactional
	@Override
	public void deleteEmpresa(Long id) {
		Empresa empresa = empresaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("No se encontró la empresa con el ID: " + id));
		empresaRepository.delete(empresa);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Empresa> buscarPorEmpresa() {
		return empresaRepository.findAllDesc();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByCuit(Long cuit) {
		return empresaRepository.existsByCuit(cuit);
	}

	@Override
	@Transactional(readOnly = true)
	public Empresa findEmpresa(Long id) {
		return empresaRepository.findById(id).orElse(null);
	}

	public Empresa convertirDtoAEntidad(EmpresaRequest dto) {
		Rubro rubro = rubroService.findRubro(dto.getIdRubro());
		if (rubro == null) {
			throw new IllegalArgumentException("El rubro especificado no existe.");
		}

		Empresa empresa = new Empresa();
		empresa.setNombre(dto.getNombre());
		empresa.setDireccion(dto.getDireccion());
		empresa.setHistoriaEmpresa(dto.getHistoriaEmpresa());
		empresa.setObservaciones(dto.getObservaciones());
		empresa.setTelefono(dto.getTelefono());
		empresa.setCuit(dto.getCuit());
		empresa.setEmail(dto.getEmail());
		empresa.setRubro(rubro);

		return empresa;
	}

	private String guardarArchivoLogo(MultipartFile logo) {
		String contentType = logo.getContentType();
		if (contentType == null || !EXTENSIONES_PERMITIDAS.contains(contentType)) {
			throw new IllegalArgumentException("Formato de imagen no permitido. Solo se aceptan PNG, JPEG y WEBP.");
		}

		try {
			Path baseRuta = Paths.get(uploadDir, "logos").normalize();
			Files.createDirectories(baseRuta);

			String originalFilename = Objects.requireNonNullElse(logo.getOriginalFilename(), "file");
			String extension = "";
			int i = originalFilename.lastIndexOf('.');
			if (i > 0) {
				extension = originalFilename.substring(i);
			}

			// Generación de un nombre único para evitar colisiones y sobrescritura accidental
			String nombreUnico = UUID.randomUUID().toString() + extension;
			Path archivoDestino = baseRuta.resolve(nombreUnico).normalize();

			if (!archivoDestino.startsWith(baseRuta)) {
				throw new SecurityException("Intento de acceso fuera del directorio de subida.");
			}

			Files.copy(logo.getInputStream(), archivoDestino, StandardCopyOption.REPLACE_EXISTING);
			return nombreUnico;

		} catch (IOException e) {
			log.error("Error de I/O al guardar el logo de empresa: {}", e.getMessage(), e);
			throw new RuntimeException("Error al procesar el almacenamiento del logo.", e);
		}
	}
}