package com.sistemaReclutador.sistemaReclutador.dto;

import java.time.LocalDateTime;

public class AplicacionesMiasResponse {
	private Integer idaplicacion;
    private String puesto;
    private String empresa;
    private LocalDateTime fecha;
    private Boolean estado;
    private String descripcionOferta;
    private String fotoOferta;
    private String email;
    private String telefono;
    private String direccion;
    private Long idOferta;
	public AplicacionesMiasResponse(Integer idaplicacion, String puesto, String empresa, LocalDateTime fecha,
			Boolean estado, String descripcionOferta, String fotoOferta, String email, String telefono,
			String direccion, Long idOferta) {
		super();
		this.idaplicacion = idaplicacion;
		this.puesto = puesto;
		this.empresa = empresa;
		this.fecha = fecha;
		this.estado = estado;
		this.descripcionOferta = descripcionOferta;
		this.fotoOferta = fotoOferta;
		this.email = email;
		this.telefono = telefono;
		this.direccion = direccion;
		this.idOferta = idOferta;
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
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public String getDescripcionOferta() {
		return descripcionOferta;
	}
	public void setDescripcionOferta(String descripcionOferta) {
		this.descripcionOferta = descripcionOferta;
	}
	public String getFotoOferta() {
		return fotoOferta;
	}
	public void setFotoOferta(String fotoOferta) {
		this.fotoOferta = fotoOferta;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Long getIdOferta() {
		return idOferta;
	}
	public void setIdOferta(Long idOferta) {
		this.idOferta = idOferta;
	}
	
	
}
