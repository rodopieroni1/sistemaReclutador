package com.sistemaReclutador.sistemaReclutador.mensajeria;

import java.util.List;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.sistemaReclutador.sistemaReclutador.entities.Perfil;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;
import com.sistemaReclutador.sistemaReclutador.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@RequiredArgsConstructor
@Component
public class NotificacionConsumidor {

    private final EmailService emailService;
    private final PerfilRepository perfilRepository;

    @RabbitListener(queues = "notificaciones.reclutador")
    public void recibirMensaje(String mensaje) {
        System.out.println("Mensaje recibido desde RabbitMQ: " + mensaje);
        // Nuevo flujo: correo a los postulantes por una nueva oferta.
        if (mensaje.startsWith("NUEVA_OFERTA\n")) {
            enviarNuevaOfertaAPostulantes(mensaje);
            return;
        }
        // Flujo existente: correo al administrador por una postulación.
        enviarNotificacionAlReclutador(mensaje);
    }

    private void enviarNotificacionAlReclutador(String mensaje) {
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

    private void enviarNuevaOfertaAPostulantes(String mensaje) {
        String[] datos = mensaje.split("\n", 5);
        if (datos.length < 5) {
            System.err.println("Notificación de oferta inválida.");
            return;
        }
        String nombreOferta = datos[2];
        String nombreEmpresa = datos[3];
        String descripcion = Jsoup.clean(
        	    datos[4],
        	    Safelist.none()
        	);

        	descripcion = Jsoup.parse(descripcion).text();
        List<Perfil> perfiles =
                perfilRepository.findPerfilesConEmailValido();
        if (perfiles.isEmpty()) {
            System.out.println("No hay perfiles con email para notificar.");
            return;
        }
        for (Perfil perfil : perfiles) {
            try {
                String cuerpo = "Hola " + perfil.getNombre() + ",\n\n"
                        + "Se ha publicado una nueva oferta laboral.\n\n"
                        + "Oferta: " + nombreOferta + "\n"
                        + "Empresa: " + nombreEmpresa + "\n\n"
                        + "Descripción:\n" + descripcion;
                emailService.send(
                        perfil.getEmail(),
                        "Nueva oferta laboral: " + nombreOferta,
                        cuerpo
                );
                System.out.println(
                        "Correo de nueva oferta enviado a: "
                                + perfil.getEmail()
                );
            } catch (Exception e) {
                System.err.println(
                        "Error enviando correo a "
                                + perfil.getEmail() + ": "
                                + e.getMessage()
                );
            }
        }
    }
}
