package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "oferta")
public class Oferta {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "id_oferta")
	    private Long idOferta;
	    
		@Column(name = "nombreOferta")
	    private String nombreOferta;
	    
	    @Column(name = "descripcion_oferta", columnDefinition = "TEXT")
	    private String descripcionOferta;
	    
	    @Lob
	    @Column(name = "foto_oferta")
	    private String fotoOferta;
	    
	    @Column(name = "estadoOferta") // Asegura que Hibernate use el nombre correcto
	    private boolean estadoOferta;
	    

	    @ManyToOne
	    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
	    private Empresa empresa;

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

		public Empresa getEmpresa() {
			return empresa;
		}

		public void setEmpresa(Empresa empresa) {
			this.empresa = empresa;
		}

		public boolean isEstadoOferta() {
			return estadoOferta;
		}

		public void setEstadoOferta(boolean estadoOferta) {
			this.estadoOferta = estadoOferta;
		}
	    
}
