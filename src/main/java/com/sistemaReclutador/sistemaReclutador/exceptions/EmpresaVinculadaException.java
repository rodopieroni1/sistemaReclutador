package com.sistemaReclutador.sistemaReclutador.exceptions;

import org.springframework.http.HttpStatus;

public class EmpresaVinculadaException extends BaseBusinessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmpresaVinculadaException(String message) {
        super(
            message,
            HttpStatus.CONFLICT,
            "409"
        );
    }
}
