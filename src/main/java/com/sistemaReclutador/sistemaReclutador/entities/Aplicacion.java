package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "aplicacion")
public class Aplicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idaplicacion")
    private Integer idaplicacion;

    @Column(name = "Fecha")
    private LocalDateTime fecha;

    @Column(name = "estadoaplicaciones")
    private boolean estadoaplicaciones;

    @ManyToOne
    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "id_oferta")
    private Oferta oferta;
    
    public void reactivar() {
        this.estadoaplicaciones = true;
        this.fecha = LocalDateTime.now();
    }
}