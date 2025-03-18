package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresas")
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_empresa;

	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;

	@Column(name = "direccion", length = 255)
	private String direccion;

	@Column(name = "historiaEmpresa", columnDefinition = "TEXT")
	private String historiaEmpresa;

	@Column(name = "observaciones", columnDefinition = "TEXT")
	private String observaciones;

	public int getId_empresa() {
		return id_empresa;
	}

	public void setId_empresa(int id_empresa) {
		this.id_empresa = id_empresa;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getHistoriaEmpresa() {
		return historiaEmpresa;
	}

	public void setHistoriaEmpresa(String historiaEmpresa) {
		this.historiaEmpresa = historiaEmpresa;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
