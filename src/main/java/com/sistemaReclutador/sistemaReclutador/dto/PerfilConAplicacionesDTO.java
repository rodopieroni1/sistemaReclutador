package com.sistemaReclutador.sistemaReclutador.dto;

import java.util.List;

import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfilConAplicacionesDTO {

    private Perfil perfil;
    private List<String> aplicaciones;
}