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
    @Column(name = "idOferta")
    private Long idOferta;

    @Column(name = "nombreOferta")
    private String nombreOferta;

    @Column(name = "descripcionOferta", columnDefinition = "MEDIUMTEXT")
    private String descripcionOferta;

    @Lob
    @Column(name = "fotoOferta")
    private String fotoOferta;

    @Column(name = "estadoOferta")
    private boolean estadoOferta;
    
    @ManyToOne
    @JoinColumn(name="idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idEmpresa", referencedColumnName = "idEmpresa")
    private Empresa empresa;
    
    
}