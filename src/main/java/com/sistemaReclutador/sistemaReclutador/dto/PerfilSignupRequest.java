package com.sistemaReclutador.sistemaReclutador.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Lob;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerfilSignupRequest {
	
	@JsonProperty("id_perfil")
	private Integer id;

	@JsonProperty("email")
	private String email;
	
	@JsonProperty("nombre")
	private String nombre;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("clave")
	private String clave;
	
	@JsonProperty("dni")
	private String dni;
	
	@JsonProperty("direccion")
	private String direccion;
	
	@Lob
	@JsonProperty("foto")
	private String foto;
	
	@Lob
	@JsonProperty("uploadcv")
	private String uploadcv;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getUploadcv() {
		return uploadcv;
	}
	public void setUploadcv(String uploadcv) {
		this.uploadcv = uploadcv;
	}
}
