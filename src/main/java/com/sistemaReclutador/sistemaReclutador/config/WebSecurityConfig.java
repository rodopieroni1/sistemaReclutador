package com.sistemaReclutador.sistemaReclutador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> {
		}).authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/uploads/**").permitAll().requestMatchers("/login").permitAll()
				.requestMatchers("/aplicaciones").permitAll().requestMatchers("/aplicaciones/{idPerfil}").permitAll()
				.requestMatchers("/aplicaciones/estado/**").permitAll()
				.requestMatchers("/aplicaciones/perfil/{idPerfil}").permitAll()
				.requestMatchers("/aplicaciones/existeId/{idPerfil}").permitAll().requestMatchers("/empresas")
				.permitAll().requestMatchers("/empresas/existe/{cuit}").permitAll()
				.requestMatchers("/empresas/existeId/{id}").permitAll().requestMatchers("/empresas/crear").permitAll()
				.requestMatchers("/empresas/actualizar/{id}").permitAll().requestMatchers("/empresas/eliminar/{id}")
				.permitAll().requestMatchers("/ofertas").permitAll().requestMatchers("/ofertas/todas").permitAll()
				.requestMatchers("/ofertas/existeId/{id}").permitAll().requestMatchers("/ofertas/crear").permitAll()
				.requestMatchers("/ofertas/actualizar/{id}").permitAll().requestMatchers("/ofertas/eliminar/{id}")
				.permitAll().requestMatchers("/ofertas/buscar", "/ofertas/buscar/**").permitAll()
				.requestMatchers("/rubro").permitAll().requestMatchers("/rubro/{id}").permitAll()
				.requestMatchers("/rubro/crear").permitAll().requestMatchers("/rubro/eliminar/{id}").permitAll()
				.requestMatchers("/rubro/actualizar/{id}").permitAll().requestMatchers("/usuarios").permitAll()
				.requestMatchers("/usuarios/auth/login").permitAll().requestMatchers("/perfiles/auth/login").permitAll()
				.requestMatchers("/perfiles/{id}").permitAll().requestMatchers("/perfiles").permitAll()
				.requestMatchers("/perfiles/name/{name}").permitAll().requestMatchers("/perfiles/id/{name}").permitAll()
				.requestMatchers("/perfiles/olvide-password").permitAll().requestMatchers("/perfiles/reset-password")
				.permitAll().requestMatchers("/ws").permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addExposedHeader("Content-Disposition");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// String
		// path="file:C:/Users/Rodrigo/Documents/SistemaReclutadorFront/proyectoReclutador/src/assets/uploads/";
		// String path = "file:/app/uploads/";
		String path = "file:C:/uploads/";
		registry.addResourceHandler("/uploads/**").addResourceLocations(path);

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
