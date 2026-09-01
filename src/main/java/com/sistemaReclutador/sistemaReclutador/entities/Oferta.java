package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "oferta")
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferta")
    private Long idOferta;

    @Column(name = "nombreOferta")
    private String nombreOferta;

    @Column(name = "descripcion_oferta", columnDefinition = "TEXT")
    private String descripcionOferta;

    @Lob
    @Column(name = "foto_oferta")
    private String fotoOferta;

    @Column(name = "estadoOferta")
    private boolean estadoOferta;

    @ManyToOne
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    private Empresa empresa;
}