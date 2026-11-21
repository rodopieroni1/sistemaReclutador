package com.sistemaReclutador.sistemaReclutador.dto;

import java.util.List;
import lombok.Data;

@Data
public class CvAnalisisDTO {
    private List<String> experienciasLaborales;
    private List<String> habilidades;
    private List<String> formacionAcademica;
    private List<String> certificaciones;
    private List<String> idiomas;
}