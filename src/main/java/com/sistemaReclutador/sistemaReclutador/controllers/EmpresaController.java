package com.sistemaReclutador.sistemaReclutador.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
@Validated
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<ResponseRest<Iterable<Empresa>>> listarEmpresas() {
        return ResponseEntity.ok(
            ResponseRest.success(empresaService.buscarPorEmpresa(), "Lista de empresas obtenida correctamente")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseRest<Empresa>> obtenerEmpresaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
            ResponseRest.success(empresaService.findEmpresa(id), "Empresa encontrada")
        );
    }

    @GetMapping("/existe/{cuit}")
    public ResponseEntity<ResponseRest<Boolean>> existePorCuit(@PathVariable Long cuit) {
        return ResponseEntity.ok(
            ResponseRest.success(empresaService.existsByCuit(cuit), "Verificación de CUIT realizada")
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseRest<Empresa>> crearEmpresa(@Valid @ModelAttribute EmpresaRequest empresaRequest) {
        Empresa empresaCreada = empresaService.saveEmpresa(empresaRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseRest.success(empresaCreada, "Empresa creada satisfactoriamente", "201"));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseRest<Empresa>> actualizarEmpresa(@PathVariable Long id, @Valid @ModelAttribute EmpresaRequest request) {
        Empresa empresaActualizada = empresaService.updateEmpresa(id, request);
        return ResponseEntity.ok(
            ResponseRest.success(empresaActualizada, "Empresa actualizada satisfactoriamente")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseRest<Void>> eliminarEmpresa(@PathVariable Long id) {
        empresaService.deleteEmpresa(id);
        return ResponseEntity.ok(
            ResponseRest.success(null, "Empresa eliminada satisfactoriamente")
        );
    }
}