package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	@Autowired
	EmpresaRepository empresaRepository;
	@Autowired
	private RubroService rubroService;
	@Value("${app.base.url}")
	private String appBaseUrl;
	
	@Value("${app.upload.dir}")
	private String uploadDir;

	@Override
	public ResponseEntity<ResponseRest<Empresa>> saveEmpresa(EmpresaRequest empresaRequest) {
		ResponseRest<Empresa> response;
		try {
			Empresa empresa = convertirDtoAEntidad(empresaRequest);
			boolean existeCuit = empresaRepository.existsByCuit(empresaRequest.getCuit());
			if (existeCuit) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El Cuit ya está registrado",
						null, LocalDateTime.now(), "400"));
			}
			boolean existeEmail = empresaRepository.existsByEmail(empresaRequest.getEmail());
			if (existeEmail) {
				return ResponseEntity.badRequest().body(
						new ResponseRest<>(false, "El email ya está registrado", null, LocalDateTime.now(), "400"));
			}
			
			String baseDir = uploadDir.endsWith(File.separator)
			        ? uploadDir
			        : uploadDir + File.separator;

			String logoDir = baseDir + "logos" + File.separator;
			File directorioLogo = new File(logoDir);
			if (!directorioLogo.exists()) {
			    directorioLogo.mkdirs();
			}
			MultipartFile logo = empresaRequest.getLogo();
			if (logo != null && !logo.isEmpty()) {
			    String nombreArchivo = logo.getOriginalFilename()
			            .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
			    File archivoLogo = new File(logoDir + nombreArchivo);
			    logo.transferTo(archivoLogo);
			}
			Empresa empresaCreate = empresaRepository.save(empresa);
			response = new ResponseRest<>(true, "Empresa creada satisfactoriamente", empresaCreate, LocalDateTime.now(),"200");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			if (e.getMessage().contains("Duplicate")) {
				response = new ResponseRest<>(false, "El email o CUIT ya está registrado", null, LocalDateTime.now(),
						"400");
			} else {
				response = new ResponseRest<>(false, "Error interno: " + e.getMessage(), null, LocalDateTime.now(),
						"500");
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<ResponseRest<Empresa>> updateEmpresa(Long id, EmpresaRequest empresaDetails) {
		try {
			boolean existe = empresaRepository.findByIdEmpresa(id);
			if (!existe) {
				ResponseRest<Empresa> response = new ResponseRest<>(false, "No se pudo actualizar la empresa", null,
						LocalDateTime.now(), "400");
				return ResponseEntity.badRequest().body(response);
			}
			Empresa empresaUpdate = empresaRepository.findById(id).get();
			Rubro rubro = rubroService.findRubroEmpresa(empresaDetails.getIdRubro());
			empresaUpdate.setCuit(empresaDetails.getCuit());
			empresaUpdate.setTelefono(empresaDetails.getTelefono());
			empresaUpdate.setDireccion(empresaDetails.getDireccion());
			empresaUpdate.setEmail(empresaDetails.getEmail());
			empresaUpdate.setHistoriaEmpresa(empresaDetails.getHistoriaEmpresa());
			empresaUpdate.setNombre(empresaDetails.getNombre());
			empresaUpdate.setObservaciones(empresaDetails.getObservaciones());
			empresaUpdate.setRubro(rubro);
			
			MultipartFile logo = empresaDetails.getLogo();
			if (logo != null && !logo.isEmpty()) {
			    String baseDir = uploadDir.endsWith(File.separator)
			            ? uploadDir
			            : uploadDir + File.separator;
			    String logoDir = baseDir + "logos" + File.separator;
			    File directorioLogo = new File(logoDir);
			    if (!directorioLogo.exists()) {
			        directorioLogo.mkdirs();
			    }
			    String nombreArchivo = logo.getOriginalFilename()
			            .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
			    File archivoLogo = new File(logoDir + nombreArchivo);
			    logo.transferTo(archivoLogo);
			    empresaUpdate.setLogo(nombreArchivo);
			}
			
			empresaRepository.save(empresaUpdate);
			ResponseRest<Empresa> response = new ResponseRest<>(true, "Empresa actualizada satisfactoriamente",
					empresaUpdate, LocalDateTime.now(), "200");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseRest<Empresa> response = new ResponseRest<>(false,
					"Ocurrió un error al actualizar la empresa: " + e.getMessage(), null, LocalDateTime.now(), "500");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	public Empresa convertirDtoAEntidad(EmpresaRequest dto) {
		try {
			Empresa empresa = new Empresa();
			empresa.setNombre(dto.getNombre());
			empresa.setDireccion(dto.getDireccion());
			empresa.setHistoriaEmpresa(dto.getHistoriaEmpresa());
			empresa.setObservaciones(dto.getObservaciones());
			empresa.setTelefono(dto.getTelefono());
			empresa.setCuit(dto.getCuit());
			empresa.setEmail(dto.getEmail());
			empresa.setLogo(dto.getNombre());
			Rubro rubro = rubroService.findRubro(dto.getIdRubro());
			empresa.setRubro(rubro);	
			return empresa;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public ResponseEntity<ResponseRest<Empresa>> deleteEmpresa(Long id) {
		try {
			Optional<Empresa> empresaOpt = empresaRepository.findById(id);
			if (empresaOpt.isEmpty()) {
				ResponseRest<Empresa> response = new ResponseRest<>(false, "No se pudo eliminar la empresa", null,
						LocalDateTime.now(), "404");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			Empresa empresa = empresaOpt.get();
			empresaRepository.delete(empresa);
			ResponseRest<Empresa> response = new ResponseRest<>(true, "Empresa eliminada satisfactoriamente", empresa,
					LocalDateTime.now(), "200");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			ResponseRest<Empresa> response = new ResponseRest<>(false,
					"Ocurrió un error al eliminar la empresa: " + e.getMessage(), null, LocalDateTime.now(), "500");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@Override
	public Iterable<Empresa> buscarPorEmpresa() {
		return empresaRepository.findAllDesc();
	}

	@Override
	public boolean existsByCuit(Long cuit) {
		boolean isEmpresa = empresaRepository.existsByCuit(cuit);
		if (isEmpresa) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Empresa findEmpresa(Long id) {
		return empresaRepository.findEmpresa(id);
	}
}