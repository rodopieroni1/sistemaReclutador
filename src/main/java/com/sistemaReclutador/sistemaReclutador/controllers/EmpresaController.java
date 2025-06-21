package com.sistemaReclutador.sistemaReclutador.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;


@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes desde el frontend
@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    public Iterable<Empresa> listarEmpresas() {
        return empresaRepository.findAllDesc();
    }

    @GetMapping("/existe/{cuit}")
    public boolean obtenerEmpresaPorId(@PathVariable Long cuit) {
    	boolean isEmpresa = empresaRepository.existsByCuit(cuit);
    	 if (isEmpresa) {
    	    	 return true;
    		} else {
    	    	 return false;
    		}
    }
    
    @GetMapping("/existeId/{id}")
    public Empresa obtenerEmpresa(@PathVariable Long id) {
    		System.out.println("empresaRepository.findEmpresa(id): "+ empresaRepository.findEmpresa(id).getId_empresa());
 	       return empresaRepository.findEmpresa(id);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearEmpresa(@RequestBody EmpresaRequest empresaRequest) {
        Empresa empresa = convertirDtoAEntidad(empresaRequest);
        boolean isUserCreate = empresaRepository.save(empresa) != null;
 		if(isUserCreate) {
 			 Map<String, String> response = new HashMap<>();
 		    response.put("message", "Empresa creada satisfactoriamente");
 		    return ResponseEntity.status(HttpStatus.CREATED).body(response);
 		}else {
 			 Map<String, String> response = new HashMap<>();
 		    response.put("message", "No se pudo Crear la Empresa");
 		    return ResponseEntity.status(HttpStatus.CREATED).body(response);		
 		}		         
    }
    
    public Empresa convertirDtoAEntidad(EmpresaRequest dto) {
        Empresa empresa = new Empresa();
        empresa.setNombre(dto.getNombre());
        empresa.setDireccion(dto.getDireccion());
        empresa.setHistoriaEmpresa(dto.getHistoria());
        empresa.setObservaciones(dto.getObservaciones());
        empresa.setCuit(dto.getCuit());
        empresa.setEmail(dto.getEmail());
        return empresa;
    }

    @PutMapping("/actualizar/{id}")
    public Empresa updateOferta(@PathVariable Long id, @RequestBody EmpresaRequest empresaDetails) {
        boolean existe = empresaRepository.findByIdEmpresa(id);
                if(existe) {
                	Empresa empresa = (empresaRepository.findById(id)).get();
                	empresa.setCuit(empresaDetails.getCuit());
                	empresa.setDireccion(empresaDetails.getDireccion());
                	empresa.setEmail(empresaDetails.getEmail());
                	empresa.setHistoriaEmpresa(empresaDetails.getHistoria());
                	empresa.setNombre(empresaDetails.getNombre());
                	empresa.setObservaciones(empresaDetails.getObservaciones());
                	return empresaRepository.save(empresa);
                }else
                {
                	ResponseEntity.notFound().build();
                	return null;
                	}
    }

    
    @DeleteMapping("/eliminar/{id}")
    public void eliminarEmpresa(@PathVariable Long id) {
    	empresaRepository.deleteById(id);
    	
    }
}
