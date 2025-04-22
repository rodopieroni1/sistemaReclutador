package com.sistemaReclutador.sistemaReclutador.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemaReclutador.sistemaReclutador.entities.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
	boolean existsByEmail(String email);

}
