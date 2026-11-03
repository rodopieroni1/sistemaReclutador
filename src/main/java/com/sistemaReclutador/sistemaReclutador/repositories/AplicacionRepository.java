package com.sistemaReclutador.sistemaReclutador.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;

@Repository
public interface AplicacionRepository extends JpaRepository<Aplicacion, Long> {

	@Query("SELECT a FROM Aplicacion a ORDER BY a.idAplicacion DESC")
	List<Aplicacion> findAllDesc();

	@Query("SELECT a FROM Aplicacion a where a.estadoAplicaciones ORDER BY a.idAplicacion DESC")
	List<Aplicacion> findAllDescActivas();

	@Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Aplicacion a WHERE a.perfil.idPerfil = :idPerfil AND a.oferta.idOferta = :idOferta")
	boolean existsByThePerfilAndOferta(@Param("idPerfil") Integer idPerfil, @Param("idOferta") Long idOferta);

	@Query("SELECT a.idAplicacion, o.nombreOferta, e.nombreEmpresa, a.fecha, a.estadoAplicaciones, "
	        + "o.descripcionOferta, o.fotoOferta, e.email, e.telefono, e.direccion, o.idOferta "
	        + "FROM Aplicacion a "
	        + "JOIN a.oferta o "
	        + "JOIN o.empresa e "
	        + "WHERE a.perfil.idPerfil = :idPerfil "
	        + "ORDER BY a.idAplicacion DESC")
	List<Object[]> obtenerAplicacionesPerfil(@Param("idPerfil") int idPerfil);

	@Query("SELECT o.nombreOferta FROM Aplicacion a JOIN a.oferta o WHERE a.perfil.idPerfil = :idPerfil"
			+ " ORDER BY a.idAplicacion DESC")
	List<String> obtenerNombreAplicacionesPerfil(@Param("idPerfil") int idPerfil);
	
	@Query("SELECT a FROM Aplicacion a WHERE a.perfil.idPerfil = :idPerfil AND a.oferta.idOferta = :idOferta")
	Optional<Aplicacion> findByPerfilAndOferta(int idPerfil, Long idOferta);

}
