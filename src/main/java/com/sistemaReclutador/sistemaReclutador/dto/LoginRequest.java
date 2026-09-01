package com.sistemaReclutador.sistemaReclutador.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequest {

    @JsonProperty("clave")
    private String clave;

    @JsonProperty("password")
    private String password;

    @JsonProperty("imagen")
    private String imagen;
}