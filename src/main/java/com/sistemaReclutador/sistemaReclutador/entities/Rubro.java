package com.sistemaReclutador.sistemaReclutador.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="rubro")
public class Rubro {
	public Rubro(int idRubro, String descripcionRubro) {
		this.idRubro = idRubro;
		this.descripcionRubro = descripcionRubro;
	}

	public Rubro(){}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idRubro;
	
    @Column(name = "descripcionRubro")
	private String descripcionRubro;
		
    @OneToMany(mappedBy = "rubro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Empresa> empresas = new ArrayList<>();
    	
	public int getIdRubro() {
		return idRubro;
	}
	public void setIdRubro(int idRubro) {
		this.idRubro = idRubro;
	}
	public String getDescripcionRubro() {
		return descripcionRubro;
	}
	public void setDescripcionRubro(String descripcionRubro) {
		this.descripcionRubro = descripcionRubro;
	}
}
