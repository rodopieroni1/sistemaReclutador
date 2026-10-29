package com.sistemaReclutador.sistemaReclutador.mensajeria;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.sistemaReclutador.sistemaReclutador.services.EmailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class NotificacionConsumidor {

    private final EmailService emailService;

    @RabbitListener(queues = "notificaciones.reclutador")
    public void recibirMensaje(String mensaje) {
        int separador = mensaje.indexOf("\n");
        if (separador == -1) {
            System.err.println("Notificación inválida: falta el destinatario.");
            return;
        }

        String destinatario = mensaje.substring(0, separador).trim();
        String cuerpo = mensaje.substring(separador + 1);

        if (destinatario.isEmpty()) {
            System.err.println("No se encontró el email del reclutador.");
            return;
        }

        emailService.send(
                destinatario,
                "Nueva postulación recibida",
                cuerpo
        );

        System.out.println("Correo enviado a: " + destinatario);
    }
}