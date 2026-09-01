package com.sistemaReclutador.sistemaReclutador.dto;

import lombok.Data;

@Data
public class SignupRequest {

    private String email;
    private String password;
    private String nombre;
    private String clave;
    private String tipoUsuario;
}