package com.sistemaReclutador.sistemaReclutador.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;

@Service
public interface EmpresaService {
	ResponseEntity<ResponseRest<Empresa>> saveEmpresa(EmpresaRequest empresaRequest);
	ResponseEntity<ResponseRest<Empresa>> updateEmpresa(Long id, EmpresaRequest empresaRequest);
	ResponseEntity<ResponseRest<Empresa>> deleteEmpresa(Long id);

}
