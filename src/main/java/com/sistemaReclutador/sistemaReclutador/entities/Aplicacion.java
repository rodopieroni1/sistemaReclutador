package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "aplicacion")
public class Aplicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAplicacion")
    private Long idAplicacion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "estadoAplicaciones")
    private boolean estadoAplicaciones;

    @ManyToOne
    @JoinColumn(name = "idPerfil", referencedColumnName = "idPerfil")
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "idOferta", referencedColumnName ="idOferta")
    private Oferta oferta;
    
    public void reactivar() {
        this.estadoAplicaciones = true;
        this.fecha = LocalDateTime.now();
    }
}