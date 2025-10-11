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
	    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
	    private Perfil perfil;

		public Long getIdReset() {
			return idReset;
		}

		public void setIdReset(Long idReset) {
			this.idReset = idReset;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public LocalDateTime getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(LocalDateTime expiryDate) {
			this.expiryDate = expiryDate;
		}

		public Perfil getPerfil() {
			return perfil;
		}

		public void setPerfil(Perfil perfil) {
			this.perfil = perfil;
		}
	    
}
