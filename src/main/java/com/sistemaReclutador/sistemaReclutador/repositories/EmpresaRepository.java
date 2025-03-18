package com.sistemaReclutador.sistemaReclutador.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistemaReclutador.sistemaReclutador.entities.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

}
