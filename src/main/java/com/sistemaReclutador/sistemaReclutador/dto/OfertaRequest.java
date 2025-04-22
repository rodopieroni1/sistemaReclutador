package com.sistemaReclutador.sistemaReclutador.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfertaRequest {
	@JsonProperty("idOferta")
    private Long idOferta;
    
	@JsonProperty("descripcionOferta")
    private String descripcionOferta;
    
	@JsonProperty("fotoOferta")
    private String fotoOferta; 

    @JsonProperty("empresa")
    private Empresa idEmpresa;
    
    
    private String nombreEmpresa;
    private String cuitEmpresa;
    private String emailEmpresa;
    private String observacionesEmpresa;
    private String direccionEmpresa;
    private String historiaEmpresa;
	
	public Long getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(Long idOferta) {
		this.idOferta = idOferta;
	}

	public Empresa getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Empresa idEmpresa) {
		this.idEmpresa = idEmpresa;
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

	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getCuitEmpresa() {
		return cuitEmpresa;
	}

	public void setCuitEmpresa(String cuitEmpresa) {
		this.cuitEmpresa = cuitEmpresa;
	}

	public String getEmailEmpresa() {
		return emailEmpresa;
	}

	public void setEmailEmpresa(String emailEmpresa) {
		this.emailEmpresa = emailEmpresa;
	}

	public String getObservacionesEmpresa() {
		return observacionesEmpresa;
	}

	public void setObservacionesEmpresa(String observacionesEmpresa) {
		this.observacionesEmpresa = observacionesEmpresa;
	}

	public String getDireccionEmpresa() {
		return direccionEmpresa;
	}

	public void setDireccionEmpresa(String direccionEmpresa) {
		this.direccionEmpresa = direccionEmpresa;
	}

	public String getHistoriaEmpresa() {
		return historiaEmpresa;
	}

	public void setHistoriaEmpresa(String historiaEmpresa) {
		this.historiaEmpresa = historiaEmpresa;
	}
}
