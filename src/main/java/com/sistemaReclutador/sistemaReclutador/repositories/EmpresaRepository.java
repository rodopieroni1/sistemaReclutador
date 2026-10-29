package com.sistemaReclutador.sistemaReclutador.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    boolean existsByCuit(@Param("cuit") Long cuit);
	
	@Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM Empresa e WHERE e.email = :email")
    boolean existsByEmail(@Param("email") String email);
	
	@Query("SELECT a FROM Empresa a ORDER BY a.idEmpresa DESC")
	List<Empresa> findAllDesc();
	
	@Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM Empresa e WHERE e.idEmpresa = :id")
	 boolean findByIdEmpresa(@Param("id") Long idEmpresa);
	
	@Query("SELECT e FROM Empresa e WHERE e.idEmpresa = :id")
	 Empresa findEmpresa(@Param("id") Long idEmpresa);	
}
