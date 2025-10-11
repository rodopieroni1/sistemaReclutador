package com.sistemaReclutador.sistemaReclutador.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;

@Repository
public interface RubroRepository extends JpaRepository<Rubro, Integer>{

}
