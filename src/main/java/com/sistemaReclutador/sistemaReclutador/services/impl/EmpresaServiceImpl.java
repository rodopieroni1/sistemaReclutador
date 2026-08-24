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
			if (empresaRequest.getEmail() == null || empresaRequest.getEmail().trim().isEmpty()) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El correo electrónico es obligatorio", null, LocalDateTime.now(), "400"));
			}
			String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
			if (!empresaRequest.getEmail().matches(emailRegex)) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El formato del correo electrónico no es válido", null, LocalDateTime.now(), "400"));
			}
			if (empresaRequest.getCuit() == null) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El CUIT es obligatorio", null, LocalDateTime.now(), "400"));
			}
			String cuitString = String.valueOf(empresaRequest.getCuit());
			if (cuitString.length() != 11) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El CUIT debe tener exactamente 11 dígitos", null, LocalDateTime.now(), "400"));
			}

			if (empresaRequest.getNombre() == null || empresaRequest.getNombre().trim().isEmpty()) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El nombre de la empresa es obligatorio", null, LocalDateTime.now(), "400"));
			}
			if (empresaRequest.getNombre().length() > 100) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El nombre de la empresa no puede superar los 100 caracteres", null, LocalDateTime.now(), "400"));
			}
			if (empresaRequest.getEmail().length() > 100) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El correo electrónico no puede superar los 100 caracteres", null, LocalDateTime.now(), "400"));
			}
			if (empresaRequest.getDireccion() != null && empresaRequest.getDireccion().length() > 255) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "La dirección no puede superar los 255 caracteres", null, LocalDateTime.now(), "400"));
			}
			if (empresaRequest.getTelefono() != null && empresaRequest.getTelefono().length() > 100) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El teléfono no puede superar los 100 caracteres", null, LocalDateTime.now(), "400"));
			}
			MultipartFile logo = empresaRequest.getLogo();
			String nombreArchivo = null;
			if (logo != null && !logo.isEmpty() && logo.getOriginalFilename() != null) {
				nombreArchivo = logo.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
				if (nombreArchivo.length() > 245) { // logo varchar(245)
					return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El nombre del archivo del logo es demasiado largo (máximo 245 caracteres)", null, LocalDateTime.now(), "400"));
				}
			}
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
			
			// REEMPLAZA TU BLOQUE DE LOGO ACTUAL POR ESTE:
			if (logo != null && !logo.isEmpty()) {
				// 1. Usamos Path y Paths.get para que Java maneje las barras de forma nativa e inteligente
				java.nio.file.Path baseRuta = java.nio.file.Paths.get(uploadDir, "logos").normalize();
				
				// 2. Creamos los directorios de forma segura si no existen
				java.nio.file.Files.createDirectories(baseRuta);
				
				// 3. Definimos la ruta absoluta final del archivo
				java.nio.file.Path archivoDestino = baseRuta.resolve(nombreArchivo).normalize();
				
				// 4. Guardamos el archivo usando Files.copy (es mucho más robusto en Docker que transferTo)
				java.nio.file.Files.copy(logo.getInputStream(), archivoDestino, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				
				empresa.setLogo(nombreArchivo);
			}

			Empresa empresaCreate = empresaRepository.save(empresa);
			response = new ResponseRest<>(true, "Empresa creada satisfactoriamente", empresaCreate, LocalDateTime.now(),"200");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
				response = new ResponseRest<>(false, "El email o CUIT ya está registrado", null, LocalDateTime.now(), "400");
			} else {
				response = new ResponseRest<>(false, "Error interno: " + e.getMessage(), null, LocalDateTime.now(), "500");
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<ResponseRest<Empresa>> updateEmpresa(Long id, EmpresaRequest empresaDetails) {
		try {
			// 1. VALIDACIÓN DE FORMATO DE CORREO
			if (empresaDetails.getEmail() == null || empresaDetails.getEmail().trim().isEmpty()) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El correo electrónico es obligatorio", null, LocalDateTime.now(), "400"));
			}
			String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
			if (!empresaDetails.getEmail().matches(emailRegex)) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El formato del correo electrónico no es válido", null, LocalDateTime.now(), "400"));
			}

			// 2. VALIDACIÓN DE LONGITUD DE CUIT (11 dígitos numéricos exigidos por el tipo bigint)
			if (empresaDetails.getCuit() == null) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El CUIT es obligatorio", null, LocalDateTime.now(), "400"));
			}
			String cuitString = String.valueOf(empresaDetails.getCuit());
			if (cuitString.length() != 11) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El CUIT debe tener exactamente 11 dígitos", null, LocalDateTime.now(), "400"));
			}

			if (empresaDetails.getNombre() == null || empresaDetails.getNombre().trim().isEmpty()) {
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El nombre de la empresa es obligatorio", null, LocalDateTime.now(), "400"));
			}
			if (empresaDetails.getNombre().length() > 100) { // nombre varchar(100)
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El nombre de la empresa no puede superar los 100 caracteres", null, LocalDateTime.now(), "400"));
			}
			if (empresaDetails.getEmail().length() > 100) { // email varchar(100)
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El correo electrónico no puede superar los 100 caracteres", null, LocalDateTime.now(), "400"));
			}
			if (empresaDetails.getDireccion() != null && empresaDetails.getDireccion().length() > 255) { // direccion varchar(255)
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "La dirección no puede superar los 255 caracteres", null, LocalDateTime.now(), "400"));
			}
			if (empresaDetails.getTelefono() != null && empresaDetails.getTelefono().length() > 100) { // telefono varchar(100)
				return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El teléfono no puede superar los 100 caracteres", null, LocalDateTime.now(), "400"));
			}

			MultipartFile logo = empresaDetails.getLogo();
			if (logo != null && !logo.isEmpty() && logo.getOriginalFilename() != null) {
				String nombreArchivoVerificar = logo.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
				if (nombreArchivoVerificar.length() > 245) { // logo varchar(245)
					return ResponseEntity.badRequest().body(new ResponseRest<>(false, "El nombre del archivo del logo es demasiado largo (máximo 245 caracteres)", null, LocalDateTime.now(), "400"));
				}
			}

			// Lógica original de actualización
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
			// Procesamiento físico del archivo si se seleccionó uno nuevo
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
			    
			    // Actualiza el campo en la base de datos con el nuevo nombre sanitizado
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
		Empresa empresa = new Empresa();
		empresa.setNombre(dto.getNombre());
		empresa.setDireccion(dto.getDireccion());
		empresa.setHistoriaEmpresa(dto.getHistoriaEmpresa());
		empresa.setObservaciones(dto.getObservaciones());
		empresa.setTelefono(dto.getTelefono());
		empresa.setCuit(dto.getCuit());
		empresa.setEmail(dto.getEmail());
		if (dto.getLogo() != null && !dto.getLogo().isEmpty() && dto.getLogo().getOriginalFilename() != null) {
		    String nombreArchivoSanitizado = dto.getLogo().getOriginalFilename()
		            .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
		    empresa.setLogo(nombreArchivoSanitizado);
		}
		Rubro rubro = rubroService.findRubro(dto.getIdRubro());
		if (rubro == null) {
		    throw new IllegalArgumentException("El rubro no existe");
		}
		empresa.setRubro(rubro);	
		return empresa;
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