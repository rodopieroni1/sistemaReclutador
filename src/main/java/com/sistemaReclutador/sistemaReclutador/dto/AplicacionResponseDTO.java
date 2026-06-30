package com.sistemaReclutador.sistemaReclutador.dto;

import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;

public class AplicacionResponseDTO {
	private ResultadosAplicacion status;
	private Aplicacion aplicacion;

	public AplicacionResponseDTO(ResultadosAplicacion status, Aplicacion aplicacion) {
		this.status = status;
		this.aplicacion = aplicacion;
	}

	public ResultadosAplicacion getStatus() {
		return status;
	}

	public void setStatus(ResultadosAplicacion status) {
		this.status = status;
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}
}
