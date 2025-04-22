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
	    private Long idOferta;
	    
	    @Column(name = "descripcionOferta", columnDefinition = "TEXT")
	    private String descripcionOferta;
	    
	    @Lob
	    @Column(name = "fotoOferta")
	    private String fotoOferta; 

	    @ManyToOne
	    @JoinColumn(name = "idEmpresa", nullable = true)
	    private Empresa empresa;

		public Long getIdOferta() {
			return idOferta;
		}

		public void setIdOferta(Long idOferta) {
			this.idOferta = idOferta;
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
	    
}
