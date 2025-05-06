package com.sistemaReclutador.sistemaReclutador.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Desactiva CSRF
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/uploads/**").permitAll()
						.requestMatchers("/login").permitAll().requestMatchers("/aplicaciones").permitAll()
						.requestMatchers("/empresas").permitAll().requestMatchers("/empresas/existe/{cuit}").permitAll()
						.requestMatchers("/empresas/existeId/{id}").permitAll().requestMatchers("/empresas/crear")
						.permitAll().requestMatchers("/empresas/actualizar/{id}").permitAll()
						.requestMatchers("/empresas/eliminar/{id}").permitAll().requestMatchers("/ofertas").permitAll()
						.requestMatchers("/ofertas/todas").permitAll().requestMatchers("/ofertas/existeId/{id}")
						.permitAll().requestMatchers("/ofertas/crear").permitAll()
						.requestMatchers("/ofertas/actualizar/{id}").permitAll()
						.requestMatchers("/ofertas/eliminar/{id}").permitAll().requestMatchers("/api/uploads")
						.permitAll().requestMatchers("/usuarios").permitAll().requestMatchers("/perfiles").permitAll()
						.requestMatchers("/ws").permitAll()
						.requestMatchers("http://localhost:8080/src/assets/uploads/documentos/**").permitAll()
						.anyRequest().authenticated() // Exige autenticación para todas las demás solicitudes
				).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Política
																												// de
																												// sesión
																												// sin
																												// estado
				);
		return http.build();
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:4200");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addExposedHeader("Content-Disposition");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**").addResourceLocations(
				"file:C:/Users/Rodrigo/Documents/SistemaReclutadorFront/proyectoReclutador/src/assets/uploads/");
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
