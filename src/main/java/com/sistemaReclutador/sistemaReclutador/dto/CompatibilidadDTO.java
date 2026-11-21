
package com.sistemaReclutador.sistemaReclutador.dto;

import java.util.List;
import lombok.Data;

@Data
public class CompatibilidadDTO {

    private double porcentajeCompatibilidad;
    private List<String> fortalezas;
    private List<String> aspectosAMejorar;
    private List<String> requisitosCumplidos;
    private List<String> requisitosNoAcreditados;
    private Double experiencia;
    private Double habilidades;
    private Double formacion;
    private Double ubicacion;
    private Double modalidad;
    private Double preferencias;
}
