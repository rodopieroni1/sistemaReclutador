package com.sistemaReclutador.sistemaReclutador.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;

public class AplicacionRequest {
	
	@JsonProperty("id_aplicacion")
	private int idAplicacion;
	
	@JsonProperty("fechaAplicacion")
	private int fechaAplicacion;
	
	@JsonProperty("estadoaplicaciones")
	private boolean estadoaplicaciones;
	
	@JsonProperty("id_perfil")
	private Perfil idPerfil;
	
	@JsonProperty("id_oferta")
	private Oferta idOferta;

	public int getIdAplicacion() {
		return idAplicacion;
	}

	public void setIdAplicacion(int idAplicacion) {
		this.idAplicacion = idAplicacion;
	}

	public int getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(int fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public boolean isEstadoaplicaciones() {
		return estadoaplicaciones;
	}

	public void setEstadoaplicaciones(boolean estadoaplicaciones) {
		this.estadoaplicaciones = estadoaplicaciones;
	}

	public Perfil getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Perfil idPerfil) {
		this.idPerfil = idPerfil;
	}

	public Oferta getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(Oferta idOferta) {
		this.idOferta = idOferta;
	}

}
