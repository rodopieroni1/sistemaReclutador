package com.sistemaReclutador.sistemaReclutador.dto;

import java.time.LocalDateTime;

public class AplicacionesMiasResponse {
	private Integer idaplicacion;
	private String puesto;
	private String empresa;
	private LocalDateTime fecha;
	private boolean estado;
	
	public AplicacionesMiasResponse(Integer idaplicacion, String puesto, String empresa, LocalDateTime fecha, boolean estado) {
		super();
		this.idaplicacion = idaplicacion;
		this.puesto = puesto;
		this.empresa = empresa;
		this.fecha = fecha;
		this.estado = estado;
	}
	
	public Integer getIdaplicacion() {
		return idaplicacion;
	}

	public void setIdaplicacion(Integer idaplicacion) {
		this.idaplicacion = idaplicacion;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}


}
