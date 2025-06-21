package com.sistemaReclutador.sistemaReclutador.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AplicacionRequest {
		
	@JsonProperty("fechaAplicacion")
	private LocalDateTime fechaAplicacion;
	
	@JsonProperty("estadoaplicaciones")
	private boolean estadoaplicaciones;
	
	@JsonProperty("id_perfil")
	private Perfil idPerfil;
	
	@JsonProperty("id_oferta")
	private Oferta idOferta;

	
	public LocalDateTime getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(LocalDateTime fechaAplicacion) {
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
