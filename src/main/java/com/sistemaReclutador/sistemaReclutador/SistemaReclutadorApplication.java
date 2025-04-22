package com.sistemaReclutador.sistemaReclutador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EntityScan(basePackages = {
	    "com.sistemaReclutador.sistemaReclutador.dto",
	    "com.sistemaReclutador.sistemaReclutador.entities"
	})
public class SistemaReclutadorApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SistemaReclutadorApplication.class, args);
	}
	  @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(SistemaReclutadorApplication.class);
	    }

}
