package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "perfil")
public class Perfil {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_perfil;

	@Column(name = "dni", nullable = false, length = 20)
	private String dni;

	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;

	@Column(name = "direccion", length = 255)
	private String direccion;
	
	@Column(name = "contraseña", nullable = false, length = 100)
	private String contraseña;

	@Column(name = "email", length = 255)
	private String email;
	

	@Lob
	@Column(name = "foto")
	private byte[] foto;

	@Lob
	@Column(name = "uploadcv")
	private byte[] uploadcv;
	
	public int getId_perfil() {
		return id_perfil;
	}

	public void setId_perfil(int id_perfil) {
		this.id_perfil = id_perfil;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
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