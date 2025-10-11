package com.sistemaReclutador.sistemaReclutador.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.repositories.RubroRepository;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;


@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes desde el frontend
@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private RubroRepository rubroRepository;

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
    public ResponseEntity<ResponseRest<Empresa>>crearEmpresa(@RequestBody EmpresaRequest empresaRequest) {
       return empresaService.saveEmpresa(empresaRequest);         
    }
      
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ResponseRest<Empresa>> updateOferta(@PathVariable Long id, @RequestBody EmpresaRequest empresaDetails) {
       return empresaService.updateEmpresa(id, empresaDetails);
    }

    
    @DeleteMapping("/eliminar/{id}")
    public void eliminarEmpresa(@PathVariable Long id) {
    	empresaRepository.deleteById(id);
    	
    }
}
