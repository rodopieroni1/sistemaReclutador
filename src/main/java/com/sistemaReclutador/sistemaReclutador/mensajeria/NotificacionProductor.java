package com.sistemaReclutador.sistemaReclutador.mensajeria;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.sistemaReclutador.sistemaReclutador.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificacionProductor {

    private final RabbitTemplate rabbitTemplate;

    public void enviarMensaje(String destinatario, String mensaje) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.COLA_NOTIFICACIONES,
                destinatario + "\n" + mensaje
        );
    }
}
