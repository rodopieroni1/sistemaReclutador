package com.sistemaReclutador.sistemaReclutador.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistemaReclutador.sistemaReclutador.entities.Oferta;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {

	@Query("SELECT o FROM Oferta o JOIN o.empresa e order by o.idOferta desc")
	 List<Oferta> findEmpresaByOferta();
	
	@Query("SELECT o FROM Oferta o ORDER BY o.idOferta DESC")
	List<Oferta> findAllDesc();
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Oferta o WHERE o.idOferta = :id")
	 boolean findByIdOferta(@Param("id") Long idOferta);
	
	@Query("SELECT o FROM Oferta o WHERE o.idOferta = :id")
	Oferta findOferta(@Param("id") Long id);

}
