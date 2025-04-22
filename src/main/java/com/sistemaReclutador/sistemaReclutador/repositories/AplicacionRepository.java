package com.sistemaReclutador.sistemaReclutador.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;

@Repository
public interface AplicacionRepository extends JpaRepository<Aplicacion, Integer> {
	
	@Query("SELECT a FROM Aplicacion a ORDER BY a.idaplicacion DESC")
	List<Aplicacion> findAllDesc();
	

}
