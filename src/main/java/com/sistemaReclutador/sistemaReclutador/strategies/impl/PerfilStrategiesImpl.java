package com.sistemaReclutador.sistemaReclutador.strategies.impl;

import java.util.Optional;
import org.springframework.stereotype.Component;

import com.sistemaReclutador.sistemaReclutador.dto.PerfilDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.strategies.PerfilStrategy;

public class PerfilStrategiesImpl {

	private static final String ALFANUMERICO_REGEX = "^[a-zA-Z0-9]+$";

	@Component
	public static class DniValidationStrategy implements PerfilStrategy {
		@Override
		public Optional<String> validar(PerfilDTO dto, PerfilRepository repository) {
			String dni = dto.dni();

			if (dni == null || !dni.matches("\\d+")) {
				return Optional
						.of("El DNI debe contener solo números, no se permiten letras ni caracteres especiales.");
			}
			if (dni.length() > 20) {
				return Optional.of("El DNI no debe ser mayor de 20 caracteres.");
			}

			Optional<Perfil> existente = repository.findByDni(dni);
			if (existente.isPresent() && !existente.get().getId_perfil().equals(dto.id())) {
				return Optional.of("El DNI ya se encuentra registrado.");
			}

			return Optional.empty();
		}
	}

	@Component
	public static class ClaveValidationStrategy implements PerfilStrategy {
		@Override
		public Optional<String> validar(PerfilDTO dto, PerfilRepository repository) {
			String clave = dto.clave();
			if (clave == null || clave.trim().isEmpty()) {
				return Optional.of("La clave es un campo obligatorio.");
			}
			if (!clave.matches(ALFANUMERICO_REGEX)) {
				return Optional.of("La clave solo puede contener letras y números, sin caracteres especiales.");
			}
			if (clave.length() > 15) {
				return Optional.of("La clave debe tener menos de 15 caracteres.");
			}

			return Optional.empty();
		}
	}

	@Component
	public static class EmailValidationStrategy implements PerfilStrategy {
		private static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@(.+)$";

		@Override
		public Optional<String> validar(PerfilDTO dto, PerfilRepository repository) {
			String email = dto.email();

			if (email != null && (!email.matches(REGEX_EMAIL) || email.length() > 100)) {
				return Optional.of("El formato del correo electrónico no es válido o supera los 100 caracteres.");
			}

			Optional<Perfil> existente = repository.findByEmail(email);
			if (existente.isPresent() && !existente.get().getId_perfil().equals(dto.id())) {
				return Optional.of("El correo electrónico ya se encuentra registrado.");
			}

			return Optional.empty();
		}
	}

	@Component
	public static class DatosBasicosValidationStrategy implements PerfilStrategy {
		@Override
		public Optional<String> validar(PerfilDTO dto, PerfilRepository repository) {
			if (dto.nombre() != null && dto.nombre().length() > 100) {
				return Optional.of("El Nombre no debe ser mayor de 100 caracteres.");
			}
			if (dto.direccion() != null && dto.direccion().length() > 255) {
				return Optional.of("La dirección no debe ser mayor de 255 caracteres.");
			}
			if (dto.clave() != null && dto.clave().length() > 100) {
				return Optional.of("La clave no debe ser mayor de 100 caracteres.");
			}

			Optional<Perfil> existenteClave = repository.findByClave(dto.clave());
			if (existenteClave.isPresent() && !existenteClave.get().getId_perfil().equals(dto.id())) {
				return Optional.of("El nombre de usuario/clave ya se encuentra registrado.");
			}

			return Optional.empty();
		}
	}
}