
package com.sistemaReclutador.sistemaReclutador.dto;

import java.util.List;
import lombok.Data;

@Data
public class CompatibilidadDTO {

	private double porcentajeCompatibilidad;
    private Double experiencia;
    private Double habilidades;
    private Double formacion;
    private Double ubicacion;
    private Double modalidad;
    private Double preferencias;
    private List<String> fortalezas;
    private List<String> aspectosAMejorar;
    private List<String> requisitosCumplidos;
    private List<String> requisitosNoAcreditados;
    private List<String> requisitosObligatoriosCumplidos;
    private List<String> requisitosObligatoriosNoAcreditados;
    private List<String> requisitosDeseablesEncontrados;
    private List<String> requisitosDeseablesNoEncontrados;
}
