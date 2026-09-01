package com.sistemaReclutador.sistemaReclutador.services;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;

public interface EmpresaService {
    Empresa saveEmpresa(EmpresaRequest empresaRequest);
    Empresa updateEmpresa(Long id, EmpresaRequest empresaDetails);
    void deleteEmpresa(Long id);
    Iterable<Empresa> buscarPorEmpresa();
    boolean existsByCuit(Long cuit);
    Empresa findEmpresa(Long id);
}