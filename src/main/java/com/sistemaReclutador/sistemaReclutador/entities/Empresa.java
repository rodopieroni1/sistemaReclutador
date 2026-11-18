package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEmpresa")
    private Long idEmpresa;

    @Column(name = "nombreEmpresa", nullable = false, length = 100)
    private String nombreEmpresa;

    @Column(name = "direccion", length = 255)
    private String direccion;

    // Ubicación de la sede de la empresa
    @Column(name = "localidad", length = 100)
    private String localidad;

    @Column(name = "provincia", length = 100)
    private String provincia;

    @Column(name = "pais", length = 100)
    private String pais;

    @Column(name = "historiaEmpresa", columnDefinition = "TEXT")
    private String historiaEmpresa;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "cuit", nullable = false, unique = true)
    private Long cuit;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "logo", length = 245)
    private String logo;

    @Column(name = "telefono", unique = true, length = 100)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "idRubro", nullable = false)
    private Rubro rubro;
}
