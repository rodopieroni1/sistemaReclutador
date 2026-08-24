package com.sistemaReclutador.sistemaReclutador.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.sistemaReclutador.sistemaReclutador.entities.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

	@Query("SELECT p FROM Perfil p WHERE p.clave = :name")
	Perfil findByName(@Param("name") String name);
	
	@Query("SELECT p FROM Perfil p WHERE p.id = :id")
	Optional<Perfil> findById(@Param("id") Long id);

    Optional<Perfil> findByClave(String clave);

    @Query("SELECT p FROM Perfil p WHERE p.clave= :clave AND p.email = :email")
    Optional<Perfil> findByEmail(String clave, String email);
    
    @Query("SELECT p FROM Perfil p WHERE p.email = :email")
    Optional<Perfil> findByEmail (String email);
    
    
    @Query("SELECT p FROM Perfil p WHERE p.clave = :clave OR p.email = :email OR p.dni = :dni")
    List<Perfil> verificarDniClaveEmail(@Param("clave") String clave, @Param("email") String email, @Param("dni") String dni);
    
	boolean existsByEmail(String email);
	boolean existsByDni(String dni);
	boolean existsByClave(String clave);

	Optional<Perfil> findByDni(String dni);

}
