package com.sistemaReclutador.sistemaReclutador.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Empresa> obtenerEmpresaPorId(@PathVariable int id) {
        return empresaRepository.findById(id);
    }

    @PostMapping
    public Empresa crearEmpresa(@RequestBody Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @PutMapping("/{id}")
    public Empresa actualizarEmpresa(@PathVariable int id, @RequestBody Empresa empresaDetails) {
    	
    	 Optional<Empresa> empresaActualizado = empresaRepository.findById(id);
	        if (empresaActualizado.isPresent()) {
	        	Empresa empresa = empresaActualizado.get();
	        	empresa.setDireccion(empresaDetails.getDireccion());
	        	empresa.setHistoriaEmpresa(empresaDetails.getHistoriaEmpresa());
	        	empresa.setNombre(empresaDetails.getNombre());
	        	empresa.setObservaciones(empresaDetails.getObservaciones());
	        	return empresaRepository.save(empresa);
	        }
	        return null; // Devuelve null si el usuario no existe
       }

    @DeleteMapping("/{id}")
    public void eliminarEmpresa(@PathVariable int id) {
    	empresaRepository.deleteById(id);
    }
}
