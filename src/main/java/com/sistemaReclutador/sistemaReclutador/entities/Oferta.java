package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "oferta")
public class Oferta {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int idOferta;
	    
	    @Column(name = "descripcionOferta", columnDefinition = "TEXT")
	    private String descripcionOferta;

	    @ManyToOne
	    @JoinColumn(name = "id_empresa", nullable = true)
	    private Empresa empresa;
		    

		public int getIdOferta() {
			return idOferta;
		}

		public void setIdOferta(int idOferta) {
			this.idOferta = idOferta;
		}

		public String getDescripcionOferta() {
			return descripcionOferta;
		}

		public void setDescripcionOferta(String descripcionOferta) {
			this.descripcionOferta = descripcionOferta;
		}

		public Empresa getEmpresa() {
			return empresa;
		}

		public void setEmpresa(Empresa empresa) {
			this.empresa = empresa;
		}
	    
}
