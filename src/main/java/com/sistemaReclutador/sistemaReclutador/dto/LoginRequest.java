package com.sistemaReclutador.sistemaReclutador.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequest {
	
	@JsonProperty("clave")
	private String clave;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("imagen")
	private String imagen;

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
}
