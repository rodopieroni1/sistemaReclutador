package com.sistemaReclutador.sistemaReclutador.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
	@Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM Empresa e WHERE e.cuit = :cuit")
    boolean existsByCuit(@Param("cuit") Long cuit);
	
	@Query("SELECT a FROM Empresa a ORDER BY a.id_empresa DESC")
	List<Empresa> findAllDesc();
	
	@Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM Empresa e WHERE e.id_empresa = :id")
	 boolean findByIdEmpresa(@Param("id") Long id_empresa);
	
	@Query("SELECT e FROM Empresa e WHERE e.id_empresa = :id")
	 Empresa findEmpresa(@Param("id") Long id_empresa);	
}
