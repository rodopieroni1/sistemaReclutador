package com.sistemaReclutador.sistemaReclutador.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Integer id_perfil;

    @Column(name = "dni", length = 20, unique = true)
    private String dni;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "clave", nullable = false, length = 100, unique = true)
    private String clave;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "fechaUltimaActividad")
    private LocalDateTime fechaUltimaActividad;

    @Column(name = "ultimoDispositivo")
    private String ultimoDispositivo;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(name = "documento_url", length = 255)
    private String documentoUrl;
}