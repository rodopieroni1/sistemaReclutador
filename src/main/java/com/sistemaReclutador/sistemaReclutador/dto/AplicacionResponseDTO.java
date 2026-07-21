package com.sistemaReclutador.sistemaReclutador.dto;

import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;
import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;

public class AplicacionResponseDTO {
	private ResultadosAplicacion status;
	private String mensaje;

	public AplicacionResponseDTO(ResultadosAplicacion status, String mensaje) {
		this.status = status;
		this.mensaje = mensaje;
	}

	public ResultadosAplicacion getStatus() {
		return status;
	}

	public void setStatus(ResultadosAplicacion status) {
		this.status = status;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
