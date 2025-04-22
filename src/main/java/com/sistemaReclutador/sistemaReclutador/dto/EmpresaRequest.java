package com.sistemaReclutador.sistemaReclutador.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmpresaRequest {
	
	@JsonProperty("id_empresa")
	private int idempresa;
	
	@JsonProperty("nombreEmpresa")
	private String nombre;
	
	@JsonProperty("direccionEmpresa")
	private String direccion;
	
	@JsonProperty("historiaEmpresa")
	private String historia;
	
	@JsonProperty("observacionesEmpresa")
	private String observaciones;
	
	@JsonProperty("emailEmpresa")
	private String email;
	
	@JsonProperty("cuitEmpresa")
	private Long  cuit;
		
	public int getIdempresa() {
		return idempresa;
	}
	public void setIdempresa(int idempresa) {
		this.idempresa = idempresa;
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
	public String getHistoria() {
		return historia;
	}
	public void setHistoria(String historia) {
		this.historia = historia;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getCuit() {
		return cuit;
	}
	public void setCuit(Long cuit) {
		this.cuit = cuit;
	}	
}
