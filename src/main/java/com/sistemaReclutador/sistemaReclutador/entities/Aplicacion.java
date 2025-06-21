package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aplicacion")
public class Aplicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idaplicacion;
    
    @Column(name = "Fecha")
    private LocalDateTime fecha;
    
    @Column(name = "estadoaplicaciones")
    private boolean estadoaplicaciones;

    @ManyToOne
    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil", nullable = true)
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "id_oferta", nullable = true)
    private Oferta oferta;

    
	

	public int getIdaplicacion() {
		return idaplicacion;
	}

	public void setIdaplicacion(int idaplicacion) {
		this.idaplicacion = idaplicacion;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Oferta getOferta() {
		return oferta;
	}

	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}

	public boolean isEstadoaplicaciones() {
		return estadoaplicaciones;
	}

	public void setEstadoaplicaciones(boolean estadoaplicaciones) {
		this.estadoaplicaciones = estadoaplicaciones;
	}
	
	
    
	}
