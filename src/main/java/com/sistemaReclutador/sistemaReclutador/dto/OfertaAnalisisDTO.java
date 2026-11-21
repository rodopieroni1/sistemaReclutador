
package com.sistemaReclutador.sistemaReclutador.dto;

import java.util.List;
import lombok.Data;

@Data
public class OfertaAnalisisDTO {

    private String nombreOferta;
    private String descripcion;
    private List<String> requisitosObligatorios;
    private List<String> requisitosDeseables;
    private List<String> habilidadesRequeridas;
    private List<String> formacionRequerida;
    private List<String> experienciaRequerida;
    private String modalidad;
    private String localidad;
    private String provincia;
    private String pais;
}
