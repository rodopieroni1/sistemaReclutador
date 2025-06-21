package com.sistemaReclutador.sistemaReclutador.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;

@Repository
public interface AplicacionRepository extends JpaRepository<Aplicacion, Integer> {
	
	@Query("SELECT a FROM Aplicacion a ORDER BY a.idaplicacion DESC")
	List<Aplicacion> findAllDesc();

	Aplicacion save(AplicacionRequest aplicacion);
   // boolean existsByPerfilIdAndOfertaId(Integer idPerfil, Long idOferta);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Aplicacion a WHERE a.perfil.id_perfil = :idPerfil AND a.oferta.idOferta = :idOferta")
    boolean existsByPerfilAndOferta(@Param("idPerfil") Integer idPerfil, @Param("idOferta") Long idOferta);

}
