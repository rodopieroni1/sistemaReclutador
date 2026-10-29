package com.sistemaReclutador.sistemaReclutador.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "passwordResetToken")
	public class PasswordResetToken {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "idReset")
	    private Long idReset;

		@Column(name = "token", nullable = false, length = 100)
	    private String token;
		
		@Column(name = "expiryDate", nullable = false, length = 100)
	    private LocalDateTime expiryDate;

	    @OneToOne
	    @JoinColumn(name = "idPerfil", referencedColumnName = "idPerfil")
	    private Perfil perfil;
	    
}
