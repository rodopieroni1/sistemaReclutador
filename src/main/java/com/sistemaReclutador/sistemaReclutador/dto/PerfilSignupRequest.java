package com.sistemaReclutador.sistemaReclutador.dto;

public class PerfilSignupRequest {

	private String email;
	private String nombre;
	private String contraseña;
	private String clave;
	private String dni;
	private String direccion;
	private byte[] foto;
	private byte[] uploadcv;
	
	
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
	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
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
	public byte[] getFoto() {
		return foto;
	}
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	public byte[] getUploadcv() {
		return uploadcv;
	}
	public void setUploadcv(byte[] uploadcv) {
		this.uploadcv = uploadcv;
	}
}
