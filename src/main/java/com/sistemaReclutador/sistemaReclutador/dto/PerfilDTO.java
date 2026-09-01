package com.sistemaReclutador.sistemaReclutador.dto;

public record PerfilDTO(
	    Integer id, 
	    String nombre, 
	    String dni, 
	    String direccion, 
	    String email, 
	    String clave
	) {}