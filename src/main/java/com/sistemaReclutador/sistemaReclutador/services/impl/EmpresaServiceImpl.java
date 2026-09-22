package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;
import com.sistemaReclutador.sistemaReclutador.strategies.EmpresaValidationStrategy;
import com.sistemaReclutador.sistemaReclutador.strategies.FileStorageStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

	private final EmpresaRepository empresaRepository;
	private final RubroService rubroService;
	private final FileStorageStrategy fileStorageStrategy;
	private final List<EmpresaValidationStrategy> validationStrategies;

	@Transactional
	@Override
	public Empresa saveEmpresa(EmpresaRequest empresaRequest) {
		ejecutarValidaciones(empresaRequest, null);
		Rubro rubro = obtenerRubroOThrow(empresaRequest.getIdRubro());
		Empresa empresa = convertirDtoAEntidad(empresaRequest, rubro);
		procesarLogoSiExiste(empresaRequest.getLogo(), empresa::setLogo);

		return empresaRepository.save(empresa);
	}

	@Transactional
	@Override
	public Empresa updateEmpresa(Long id, EmpresaRequest empresaDetails) {
		Empresa empresa = empresaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("La empresa con ID " + id + " no existe."));
		ejecutarValidaciones(empresaDetails, empresa);
		Rubro rubro = obtenerRubroOThrow(empresaDetails.getIdRubro());
		actualizarCamposEmpresa(empresa, empresaDetails, rubro);
		procesarLogoSiExiste(empresaDetails.getLogo(), empresa::setLogo);
		return empresaRepository.save(empresa);
	}

	private void ejecutarValidaciones(EmpresaRequest request, Empresa empresaExistente) {
		validationStrategies.forEach(strategy -> strategy.validar(request, empresaExistente));
	}

	private void procesarLogoSiExiste(MultipartFile logo, java.util.function.Consumer<String> setterLogo) {
		if (logo != null && !logo.isEmpty()) {
			String urlLogo = fileStorageStrategy.storeFile(logo, "logos");
			String nombreLogo = urlLogo.substring(urlLogo.lastIndexOf("/") + 1);
			setterLogo.accept(nombreLogo);
		}
	}

	@Transactional
	@Override
	public void deleteEmpresa(Long id) {
		Empresa empresa = empresaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("No se encontro la empresa con el ID: " + id));
		empresaRepository.delete(empresa);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Empresa> buscarPorEmpresa() {
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

	private Rubro obtenerRubroOThrow(Integer idRubro) {
		Rubro rubro = rubroService.findRubro(idRubro);
		if (rubro == null) {
			throw new IllegalArgumentException("El rubro especificado no existe.");
		}
		return rubro;
	}

	private Empresa convertirDtoAEntidad(EmpresaRequest dto, Rubro rubro) {
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

	private void actualizarCamposEmpresa(Empresa empresa, EmpresaRequest dto, Rubro rubro) {
		empresa.setCuit(dto.getCuit());
		empresa.setNombre(dto.getNombre());
		empresa.setEmail(dto.getEmail());
		empresa.setTelefono(dto.getTelefono());
		empresa.setDireccion(dto.getDireccion());
		empresa.setHistoriaEmpresa(dto.getHistoriaEmpresa());
		empresa.setObservaciones(dto.getObservaciones());
		empresa.setRubro(rubro);
	}

}