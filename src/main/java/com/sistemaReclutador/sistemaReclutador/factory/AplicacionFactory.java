package com.sistemaReclutador.sistemaReclutador.factory;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.sistemaReclutador.sistemaReclutador.entities.Aplicacion;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;

@Component
public class AplicacionFactory {

	public Aplicacion crear(Perfil perfil, Oferta oferta) {

        return Aplicacion.builder()
                .fecha(LocalDateTime.now())
                .estadoaplicaciones(true)
                .perfil(perfil)
                .oferta(oferta)
                .build();
    }
     
}
