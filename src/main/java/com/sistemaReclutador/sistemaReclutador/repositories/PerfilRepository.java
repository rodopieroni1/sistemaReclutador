package com.sistemaReclutador.sistemaReclutador.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

	@Query("SELECT p.fotoUrl FROM Perfil p WHERE p.clave = :name")
	String findByName(@Param("name") String name);

    Optional<Perfil> findByClave(String clave);
	boolean existsByEmail(String email);

}
