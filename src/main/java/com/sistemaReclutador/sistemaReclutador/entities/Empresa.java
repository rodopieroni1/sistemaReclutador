package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_empresa;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "historia_empresa", columnDefinition = "TEXT")
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
    @JoinColumn(name = "id_rubro", nullable = false)
    private Rubro rubro;
}