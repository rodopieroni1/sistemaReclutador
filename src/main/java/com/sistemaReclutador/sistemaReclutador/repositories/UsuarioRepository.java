package com.sistemaReclutador.sistemaReclutador.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sistemaReclutador.sistemaReclutador.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	boolean existsByEmail(String email);

}
