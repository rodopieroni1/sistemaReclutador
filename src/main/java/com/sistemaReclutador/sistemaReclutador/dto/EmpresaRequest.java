package com.sistemaReclutador.sistemaReclutador.dto;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {

    @JsonProperty("id_empresa")
    private Integer idempresa;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @JsonProperty("nombreEmpresa")
    private String nombre;

    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
    @JsonProperty("direccionEmpresa")
    private String direccion;

    @JsonProperty("historiaEmpresa")
    private String historiaEmpresa;

    @JsonProperty("observacionesEmpresa")
    private String observaciones;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    @JsonProperty("emailEmpresa")
    private String email;

    @NotNull(message = "El CUIT es obligatorio")
    @JsonProperty("cuit")
    private Long cuit;

    @Size(max = 100, message = "El teléfono no puede superar los 100 caracteres")
    @JsonProperty("telefonoEmpresa")
    private String telefono;

    @NotNull(message = "El rubro es obligatorio")
    @JsonProperty("idRubro")
    private Integer idRubro;

    @JsonProperty("logo")
    private MultipartFile logo;
}