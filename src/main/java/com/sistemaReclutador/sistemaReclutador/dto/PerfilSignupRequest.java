package com.sistemaReclutador.sistemaReclutador.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerfilSignupRequest {

    @JsonProperty("id_perfil")
    private Integer id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("password")
    private String password;

    @JsonProperty("clave")
    private String clave;

    @JsonProperty("dni")
    private String dni;

    @JsonProperty("direccion")
    private String direccion;

    @Lob
    @JsonProperty("foto")
    private String foto;

    @Lob
    @JsonProperty("uploadcv")
    private String uploadcv;
}