package com.sistemaReclutador.sistemaReclutador.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfertaRequest {
	@JsonProperty("id_oferta")
    private Long idOferta;
    
	@JsonProperty("nombreOferta")
    private String nombreOferta;
	
	@JsonProperty("estadoOferta")
	private boolean estadoOferta;
	
	@JsonProperty("descripcionOferta")
    private String descripcionOferta;
    
	@JsonProperty("fotoOferta")
    private String fotoOferta; 
	
	@JsonProperty("fotoArchivo")
	private MultipartFile fotoArchivo;

    @JsonProperty("empresa")
    private Empresa idEmpresa;

	public Long getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(Long idOferta) {
		this.idOferta = idOferta;
	}

	public String getNombreOferta() {
		return nombreOferta;
	}

	public void setNombreOferta(String nombreOferta) {
		this.nombreOferta = nombreOferta;
	}

	public boolean isEstadoOferta() {
		return estadoOferta;
	}

	public void setEstadoOferta(boolean estadoOferta) {
		this.estadoOferta = estadoOferta;
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

	public Empresa getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Empresa idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public MultipartFile getFotoArchivo() {
		return fotoArchivo;
	}

	public void setFotoArchivo(MultipartFile fotoArchivo) {
		this.fotoArchivo = fotoArchivo;
	}  
}
