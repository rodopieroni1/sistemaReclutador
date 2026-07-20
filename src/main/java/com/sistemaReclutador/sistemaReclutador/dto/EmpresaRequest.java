package com.sistemaReclutador.sistemaReclutador.dto;

import org.springframework.web.multipart.MultipartFile;

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
	private String historiaEmpresa;
	
	@JsonProperty("observacionesEmpresa")
	private String observaciones;
	
	@JsonProperty("emailEmpresa")
	private String email;
	
	@JsonProperty("telefonoEmpresa")
	private String telefono;
	
	@JsonProperty("logo")
	private MultipartFile logo;
	
	@JsonProperty("cuitEmpresa")
	private Long  cuit;
	
	@JsonProperty("idRubro")
	private Long  idRubro;	
		
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
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
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
	public MultipartFile getLogo() {
		return logo;
	}
	public void setLogo(MultipartFile logo) {
		this.logo = logo;
	}
	public Long getIdRubro() {
		return idRubro;
	}
	public void setIdRubro(Long idRubro) {
		this.idRubro = idRubro;
	}
}
