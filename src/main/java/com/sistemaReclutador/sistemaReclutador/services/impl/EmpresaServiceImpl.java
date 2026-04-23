package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.RubroRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService {
	
	@Autowired
	EmpresaRepository empresaRepository;
	@Autowired
	RubroRepository rubroRepository;
	 
	@Override
	public ResponseEntity<ResponseRest<Empresa>> saveEmpresa(EmpresaRequest empresaRequest) {
	    ResponseRest<Empresa> response;

	    try {
	        Empresa empresa = convertirDtoAEntidad(empresaRequest);
	        
	        boolean existeCuit = empresaRepository.existsByCuit(empresaRequest.getCuit());

	        if (existeCuit) {
	            return ResponseEntity.badRequest().body(
	                new ResponseRest<>(false, "El existeCuit ya está registrado", null, LocalDateTime.now(), "400")
	            );
	        }
	        
	        boolean existeEmail = empresaRepository.existsByEmail(empresaRequest.getEmail());

	        if (existeEmail) {
	            return ResponseEntity.badRequest().body(
	                new ResponseRest<>(false, "El email ya está registrado", null, LocalDateTime.now(), "400")
	            );
	        }
	        
	        
	        Empresa empresaCreate = empresaRepository.save(empresa);

	        response = new ResponseRest<>(
	            true,
	            "Empresa creada satisfactoriamente",
	            empresaCreate,
	            LocalDateTime.now(),
	            "200"
	        );

	        return ResponseEntity.status(HttpStatus.CREATED).body(response);

	    } catch (Exception e) {
	        if (e.getMessage().contains("Duplicate")) {
	            response = new ResponseRest<>(
	                false,
	                "El email o CUIT ya está registrado",
	                null,
	                LocalDateTime.now(),
	                "400"
	            );
	        } else {
	            response = new ResponseRest<>(
	                false,
	                "Error interno: " + e.getMessage(),
	                null,
	                LocalDateTime.now(),
	                "500"
	            );
	        }

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	}
	
	
	@Override
	public ResponseEntity<ResponseRest<Empresa>> updateEmpresa(Long id, EmpresaRequest empresaDetails) {
		ResponseRest<Empresa> response;
		boolean existe = empresaRepository.findByIdEmpresa(id);
      	Empresa empresaUpdate = (empresaRepository.findById(id)).get();
         if(existe) {
         	empresaUpdate.setCuit(empresaDetails.getCuit());
         	empresaUpdate.setTelefono(empresaDetails.getTelefono());
         	empresaUpdate.setDireccion(empresaDetails.getDireccion());
         	empresaUpdate.setEmail(empresaDetails.getEmail());
         	empresaUpdate.setHistoriaEmpresa(empresaDetails.getHistoria());
         	empresaUpdate.setNombre(empresaDetails.getNombre());
         	empresaUpdate.setObservaciones(empresaDetails.getObservaciones());
         	empresaRepository.save(empresaUpdate);
         	 response = new ResponseRest<Empresa> (true, "Empresa Acutalizada satisfactoriamente", 
			    		empresaUpdate, LocalDateTime.now(), "200");
			    return ResponseEntity.status(HttpStatus.CREATED).body(response);
         }else {
        	 response = new ResponseRest<Empresa>(false, "No se pudo Actualizar la empresa", 
		        		null, LocalDateTime.now(), "400");
			    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);	
         }
	}
	
	 public Empresa convertirDtoAEntidad(EmpresaRequest dto) {
	        Empresa empresa = new Empresa();
	        empresa.setNombre(dto.getNombre());
	        empresa.setDireccion(dto.getDireccion());
	        empresa.setHistoriaEmpresa(dto.getHistoria());
	        empresa.setObservaciones(dto.getObservaciones());
	        empresa.setTelefono(dto.getTelefono());
	        empresa.setCuit(dto.getCuit());
	        empresa.setEmail(dto.getEmail());
	        
	        Rubro rubro = rubroRepository.findById( dto.getIdRubro().intValue())
	            .orElseThrow(() -> new RuntimeException("Rubro no encontrado"));
	        
	        empresa.setRubro(rubro);
	        return empresa;
	    }

}
