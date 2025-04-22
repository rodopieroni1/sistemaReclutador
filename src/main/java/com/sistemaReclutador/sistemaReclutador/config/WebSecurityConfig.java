package com.sistemaReclutador.sistemaReclutador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig implements WebMvcConfigurer{

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}  
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) // Desactiva CSRF
	        .authorizeHttpRequests(authorize -> authorize
	            .requestMatchers("/login")
	            .permitAll()
	            .requestMatchers("/aplicaciones")
	            .permitAll()
	            .requestMatchers("/empresas")
	            .permitAll()
	            .requestMatchers("/empresas/existe/{cuit}")
	            .permitAll()
	            .requestMatchers("/empresas/existeId/{id}")
	            .permitAll()
	            .requestMatchers("/empresas/crear")
	            .permitAll()
	            .requestMatchers("/empresas/actualizar/{id}")
	            .permitAll()
	            .requestMatchers("/empresas/eliminar/{id}")
	            .permitAll()
	            .requestMatchers("/ofertas")
	            .permitAll()
	            .requestMatchers("/ofertas/todas")
	            .permitAll()
	            .requestMatchers("/ofertas/existeId/{id}")
	            .permitAll()
	            .requestMatchers("/ofertas/crear")
	            .permitAll()
	            .requestMatchers("/ofertas/actualizar/{id}")
	            .permitAll()
	            .requestMatchers("/ofertas/eliminar/{id}")
	            .permitAll()
	            .requestMatchers("/api/uploads")
	            .permitAll()
	            .anyRequest().authenticated() // Exige autenticación para todas las demás solicitudes
	        )
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Política de sesión sin estado
	        );
	    return http.build();
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
	    return new WebMvcConfigurer() {
	        @Override
	        public void addCorsMappings(CorsRegistry registry) {
	            registry.addMapping("/**") // Permite todas las rutas
	                    .allowedOrigins("http://localhost:4200") // Cambia al origen necesario
	                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
	                    .allowedHeaders("*") // Todos los encabezados permitidos
	                    .allowCredentials(true) // Permite enviar cookies o credenciales
	                    .exposedHeaders("Content-Disposition"); // Exposición de encabezados (ej. descarga de archivos)
	        }
	    };
	}

	
	@Bean
    public WebMvcConfigurer contentNegotiationConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                configurer.mediaType("js", MediaType.valueOf("application/javascript"));
                configurer.mediaType("css", MediaType.valueOf("text/css"));
            }
        };
    }

}
