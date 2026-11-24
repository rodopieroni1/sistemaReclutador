package com.sistemaReclutador.sistemaReclutador.dto;

import lombok.Data;

@Data
public class AnalisisCompatibilidadRequestDTO {

    private OfertaAnalisisDTO oferta;
    private CvAnalisisDTO cv;
}