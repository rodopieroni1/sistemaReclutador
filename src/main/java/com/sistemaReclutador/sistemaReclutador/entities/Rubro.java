package com.sistemaReclutador.sistemaReclutador.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rubro")
public class Rubro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRubro")
    private Integer idRubro;

    @Column(name = "descripcionRubro")
    private String descripcionRubro;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "rubro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Empresa> empresas = new ArrayList<>();

    // Constructor personalizado explicito
    public Rubro(Integer idRubro, String descripcionRubro) {
        this.idRubro = idRubro;
        this.descripcionRubro = descripcionRubro;
        this.empresas = new ArrayList<>();
    }
}