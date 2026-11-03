package com.sistemaReclutador.sistemaReclutador.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistemaReclutador.sistemaReclutador.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Optional<Usuario> findByClave(String clave);
	@Query("SELECT u FROM Usuario u WHERE u.clave = :clave")
	Usuario findByClaveSendMail(@Param("clave") String clave);
	boolean existsByClave(String clave);
	boolean existsByEmail(String email);

}
