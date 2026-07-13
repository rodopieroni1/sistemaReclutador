package com.sistemaReclutador.sistemaReclutador.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;

@Repository
public interface RubroRepository extends JpaRepository<Rubro, Integer>{

	@Query("SELECT r FROM Rubro r WHERE r.idRubro = :idRubro")
	Optional<Rubro> findById(Long idRubro);

}
