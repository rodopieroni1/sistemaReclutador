package com.sistemaReclutador.sistemaReclutador.controllers;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class MyWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); // Almacenar la sesión
        System.out.println("Cliente conectado: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Mensaje recibido: " + message.getPayload());
        // Ejemplo de respuesta
        session.sendMessage(new TextMessage("Servidor recibió: " + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session); // Remover la sesión al desconectarse
        System.out.println("Cliente desconectado: " + session.getId());
    }

    // Método para notificar a todos los clientes
    public void notifyClients(String imageUrl) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    System.out.println("Imagen subida: " + imageUrl);
                    session.sendMessage(new TextMessage(imageUrl));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
