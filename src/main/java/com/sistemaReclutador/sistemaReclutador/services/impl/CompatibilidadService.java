package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.util.List;
import com.sistemaReclutador.sistemaReclutador.dto.CompatibilidadDTO;
import java.util.ArrayList;

public class CompatibilidadService {

	public CompatibilidadDTO construirResultado(Double experiencia, Double habilidades, Double formacion,
			Double ubicacion, Double modalidad, Double preferencias) {

		CompatibilidadDTO resultado = new CompatibilidadDTO();
		List<String> fortalezas = new ArrayList<>();
		List<String> aspectosAMejorar = new ArrayList<>();

		resultado.setExperiencia(experiencia);
		resultado.setHabilidades(habilidades);
		resultado.setFormacion(formacion);
		resultado.setUbicacion(ubicacion);
		resultado.setModalidad(modalidad);
		resultado.setPreferencias(preferencias);

		clasificarCriterio("Experiencia", experiencia, fortalezas, aspectosAMejorar);
		clasificarCriterio("Habilidades", habilidades, fortalezas, aspectosAMejorar);
		clasificarCriterio("Formación", formacion, fortalezas, aspectosAMejorar);
		clasificarCriterio("Ubicación", ubicacion, fortalezas, aspectosAMejorar);
		clasificarCriterio("Modalidad", modalidad, fortalezas, aspectosAMejorar);

		clasificarCriterio("Preferencias", preferencias, fortalezas, aspectosAMejorar);
		resultado.setPorcentajeCompatibilidad(
				calcularPorcentaje(experiencia, habilidades, formacion, ubicacion, modalidad, preferencias));

		resultado.setFortalezas(fortalezas);
		resultado.setAspectosAMejorar(aspectosAMejorar);
		resultado.setRequisitosCumplidos(new ArrayList<>());
		resultado.setRequisitosNoAcreditados(new ArrayList<>());

		return resultado;
	}

	public double calcularPorcentaje(Double experiencia, Double habilidades, Double formacion, Double ubicacion,
			Double modalidad, Double preferencias) {

		validarPuntaje(experiencia);
		validarPuntaje(habilidades);
		validarPuntaje(formacion);
		validarPuntaje(ubicacion);
		validarPuntaje(modalidad);
		validarPuntaje(preferencias);
		double total = 0;
		double pesoTotal = 0;

		if (experiencia != null) {
			total += experiencia * 0.25;
			pesoTotal += 0.25;
		}

		if (habilidades != null) {
			total += habilidades * 0.25;
			pesoTotal += 0.25;
		}

		if (formacion != null) {
			total += formacion * 0.15;
			pesoTotal += 0.15;
		}

		if (ubicacion != null) {
			total += ubicacion * 0.15;
			pesoTotal += 0.15;
		}

		if (modalidad != null) {
			total += modalidad * 0.10;
			pesoTotal += 0.10;
		}

		if (preferencias != null) {
			total += preferencias * 0.10;
			pesoTotal += 0.10;
		}

		if (pesoTotal == 0) {
			return 0;
		}

		return Math.round((total / pesoTotal) * 10000.0) / 100.0;
	}

	private void validarPuntaje(Double puntaje) {

		if (puntaje != null && (puntaje < 0 || puntaje > 1)) {
			throw new IllegalArgumentException("El puntaje debe estar entre 0 y 1");
		}
	}

	private void clasificarCriterio(String criterio, Double puntaje, List<String> fortalezas,
			List<String> aspectosAMejorar) {

		if (puntaje == null) {
			return;
		}

		if (puntaje >= 0.70) {
			fortalezas.add(criterio);
		} else if (puntaje < 0.50) {
			aspectosAMejorar.add(criterio);
		}
	}

}
