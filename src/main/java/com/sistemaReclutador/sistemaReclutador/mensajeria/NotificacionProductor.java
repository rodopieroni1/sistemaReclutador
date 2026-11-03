package com.sistemaReclutador.sistemaReclutador.mensajeria;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.sistemaReclutador.sistemaReclutador.config.RabbitMQConfig;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificacionProductor {

	private final RabbitTemplate rabbitTemplate;

	public void enviarMensaje(String destinatario, String mensaje) {
		rabbitTemplate.convertAndSend(RabbitMQConfig.COLA_NOTIFICACIONES, destinatario + "\n" + mensaje);
	}

	public void enviarNuevaOferta(Oferta oferta) {
		String nombreEmpresa = "Empresa no especificada";
		if (oferta.getEmpresa() != null && oferta.getEmpresa().getNombreEmpresa() != null) {
			nombreEmpresa = oferta.getEmpresa().getNombreEmpresa();
		}
		String mensaje = "NUEVA_OFERTA\n" + oferta.getIdOferta() + "\n" + oferta.getNombreOferta() + "\n"
				+ nombreEmpresa + "\n" + (oferta.getDescripcionOferta() != null ? oferta.getDescripcionOferta() : "");
		rabbitTemplate.convertAndSend(RabbitMQConfig.COLA_NOTIFICACIONES, mensaje);
	}

}
