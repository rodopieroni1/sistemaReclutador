package com.sistemaReclutador.sistemaReclutador.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.sistemaReclutador.sistemaReclutador.controllers.MyWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer  {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		System.out.println("Pasaaaaaaa:"+ registry);
        registry.addHandler(new MyWebSocketHandler(), "/ws").setAllowedOrigins("*");		
	}

}
