package com.sistemaReclutador.sistemaReclutador.strategies.impl;

import java.util.Optional;
import org.springframework.stereotype.Component;

import com.sistemaReclutador.sistemaReclutador.dto.PerfilDTO;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.strategies.PerfilStrategy;

public class PerfilStrategiesImpl {

	private static final String ALFANUMERICO_REGEX = "^[a-zA-Z0-9]+$";
	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[a-zA-Z0-9]{8,12}$";

	@Component
	public static class DniValidationStrategy implements PerfilStrategy {

		@Override
		public Optional<String> validar(PerfilDTO dto, PerfilRepository repository) {

			String dni = dto.dni();

			if (dni == null || dni.isBlank()) {
				return Optional.of("El DNI es obligatorio.");
			}

			if (!dni.matches("\\d+")) {
				return Optional.of("El DNI debe contener solamente números.");
			}

			if (dni.startsWith("0")) {
				return Optional.of("El DNI no puede comenzar con cero.");
			}

			if (dni.length() < 7 || dni.length() > 8) {
				return Optional.of("El DNI debe tener entre 7 y 8 dígitos.");
			}

			Optional<Perfil> existente = repository.findByDni(dni);

			if (existente.isPresent() && !existente.get().getIdPerfil().equals(dto.id())) {
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
			if (existente.isPresent() && !existente.get().getIdPerfil().equals(dto.id())) {
				return Optional.of("El correo electrónico ya se encuentra registrado.");
			}

			return Optional.empty();
		}
	}

	@Component
	public static class ContraseñaStrategy implements PerfilStrategy {

		@Override
		public Optional<String> validar(PerfilDTO dto, PerfilRepository repository) {
			String contraseña = dto.passord();

			if (contraseña == null || contraseña.isBlank()) {
				return Optional.empty();
			}

			if (!contraseña.matches(PASSWORD_REGEX)) {
				return Optional.of("La contraseña debe tener entre 8 y 12 caracteres, "
						+ "al menos una mayúscula, una minúscula y un número, " + "sin caracteres especiales.");
			}
			return Optional.empty();
		}

	}

	@Component
	public static class DatosBasicosValidationStrategy implements PerfilStrategy {

		@Override
		public Optional<String> validar(PerfilDTO dto, PerfilRepository repository) {

			// NOMBRE
			if (dto.nombre() == null || dto.nombre().isBlank()) {
				return Optional.of("El nombre es obligatorio.");
			}

			if (dto.nombre().length() > 100) {
				return Optional.of("El nombre no debe ser mayor de 100 caracteres.");
			}

			// DIRECCIÓN
			if (dto.direccion() == null || dto.direccion().isBlank()) {
				return Optional.of("La dirección es obligatoria.");
			}

			if (dto.direccion().length() > 255) {
				return Optional.of("La dirección no debe ser mayor de 255 caracteres.");
			}

			// CLAVE
			if (dto.clave() == null || dto.clave().isBlank()) {
				return Optional.of("La clave es obligatoria.");
			}

			if (dto.clave().length() > 100) {
				return Optional.of("La clave no debe ser mayor de 100 caracteres.");
			}

			// CLAVE ÚNICA
			Optional<Perfil> existenteClave = repository.findByClave(dto.clave());
			if (existenteClave.isPresent() && !existenteClave.get().getIdPerfil().equals(dto.id())) {
				return Optional.of("El nombre de usuario/clave ya se encuentra registrado.");
			}

			return Optional.empty();
		}
	}
}