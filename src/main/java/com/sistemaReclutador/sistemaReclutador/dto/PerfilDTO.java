package com.sistemaReclutador.sistemaReclutador.dto;

public record PerfilDTO(
	    Integer id, 
	    String nombre, 
	    String dni, 
	    String direccion,
	    String pais,
	    String provincia,
	    String localidad,
	    String email, 
	    String telefono,
	    String clave,
	    String passord
	) {}