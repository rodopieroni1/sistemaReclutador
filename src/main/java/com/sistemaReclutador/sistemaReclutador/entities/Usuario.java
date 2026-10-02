package com.sistemaReclutador.sistemaReclutador.entities;

import com.sistemaReclutador.sistemaReclutador.Enum.TipoUsuario;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "clave", nullable = false, length = 50)
    private String clave;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contraseña;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoUsuario", nullable = false, length = 20)
    private TipoUsuario tipoUsuario;
}