package com.sistemaReclutador.sistemaReclutador.response;

import org.springframework.stereotype.Component;
import com.sistemaReclutador.sistemaReclutador.dto.OfertaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.exceptions.ResourceNotFoundException;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResponseOferta {

    private final EmpresaRepository empresaRepository;

    public Oferta convertirDtoAEntidad(OfertaRequest dto) {
        Oferta oferta = new Oferta();
        oferta.setNombreOferta(dto.getNombreOferta());
        oferta.setDescripcionOferta(dto.getDescripcionOferta());
        oferta.setEstadoOferta(dto.isEstadoOferta());
        oferta.setFotoOferta(dto.getFotoOferta());

        if (dto.getIdEmpresa() != null) {
            Empresa empresa = empresaRepository.findById(dto.getIdEmpresa())
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + dto.getIdEmpresa()));
            oferta.setEmpresa(empresa); 
        }

        return oferta;
    }
}