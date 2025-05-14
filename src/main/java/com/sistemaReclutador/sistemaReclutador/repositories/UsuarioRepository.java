package com.sistemaReclutador.sistemaReclutador.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemaReclutador.sistemaReclutador.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
    Optional<Usuario> findByClave(String clave);
	boolean existsByEmail(String email);

}
