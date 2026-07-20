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

	@Query("SELECT o FROM Oferta o WHERE o.estadoOferta = 1 ORDER BY o.idOferta DESC")
	List<Oferta> findAllDesc();

	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Oferta o WHERE o.idOferta = :id")
	boolean findByIdOferta(@Param("id") Long idOferta);

	@Query("SELECT o FROM Oferta o WHERE o.idOferta = :id")
	Oferta findOferta(@Param("id") Long id);

	@Query("SELECT o FROM Oferta o JOIN o.empresa e "
			+ "WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :descripcionEmpresa, '%'))")
	List<Oferta> buscarPorDescripcionEmpresa(@Param("descripcionEmpresa") String descripcionEmpresa);

	@Query("SELECT o FROM Oferta o " + "WHERE LOWER(o.nombreOferta) LIKE LOWER(CONCAT('%', :nombreOferta, '%'))")
	List<Oferta> buscarPorNombreOferta(@Param("nombreOferta") String nombreOferta);

	@Query("SELECT o FROM Oferta o JOIN o.empresa e JOIN e.rubro r " +
		       "WHERE LOWER(r.descripcionRubro) LIKE LOWER(CONCAT('%', :descripcionRubro, '%'))")
		List<Oferta> buscarPorRubro(@Param("descripcionRubro") String descripcionRubro);

	@Query("SELECT o FROM Oferta o JOIN o.empresa e " +
		       "WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :descripcionEmpresa, '%')) " +
		       "AND LOWER(e.rubro.descripcionRubro) LIKE LOWER(CONCAT('%', :descripcionRubro, '%'))")
		List<Oferta> buscarPorDescripcionYRubro(@Param("descripcionEmpresa") String descripcionEmpresa,
		                                        @Param("descripcionRubro") String descripcionRubro);
	
	@Query("SELECT o FROM Oferta o WHERE LOWER(o.nombreOferta) LIKE LOWER(CONCAT('%', :nombreOferta, '%')) " +
		       "AND LOWER(o.empresa.rubro.descripcionRubro) LIKE LOWER(CONCAT('%', :descripcionRubro, '%'))")
		List<Oferta> buscarPorNombreYRubro(@Param("nombreOferta") String nombreOferta,
		                                   @Param("descripcionRubro") String descripcionRubro);

	@Query("SELECT o FROM Oferta o JOIN o.empresa e " +
		       "WHERE LOWER(o.nombreOferta) LIKE LOWER(CONCAT('%', :nombreOferta, '%')) " +
		       "AND LOWER(e.nombre) LIKE LOWER(CONCAT('%', :descripcionEmpresa, '%')) " +
		       "AND LOWER(e.rubro.descripcionRubro) LIKE LOWER(CONCAT('%', :descripcionRubro, '%'))")
		List<Oferta> buscarPorCampos(@Param("nombreOferta") String nombreOferta,
		                             @Param("descripcionEmpresa") String descripcionEmpresa,
		                             @Param("descripcionRubro") String descripcionRubro);

}
