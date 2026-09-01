package com.sistemaReclutador.sistemaReclutador.strategies;

import java.util.Optional;

import com.sistemaReclutador.sistemaReclutador.dto.PerfilDTO;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;

public interface PerfilStrategy {
	Optional<String> validar(PerfilDTO dto, PerfilRepository repository);
}
