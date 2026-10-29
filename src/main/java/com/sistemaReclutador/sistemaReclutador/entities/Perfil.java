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
    @Column(name = "idPerfil")
    private Integer idPerfil;

    @Column(name = "clave", nullable = false, length = 100, unique = true)
    private String clave;

    @Column(name = "direccion", length = 255)
    private String direccion;
    
    @Column(name = "dni", length = 20, unique = true)
    private String dni;
    
    @Column(name = "email", length = 100, unique = true)
    private String email;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "fechaUltimaActividad")
    private LocalDateTime fechaUltimaActividad;

    @Column(name = "sessionId")
    private String sessionId;

    @Column(name = "ultimoDispositivo")
    private String ultimoDispositivo;
    
    @Column(name = "telefono", length = 25, unique = true)
    private String telefono;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(name = "documento_url", length = 255)
    private String documentoUrl;

}