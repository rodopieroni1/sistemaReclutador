package com.sistemaReclutador.sistemaReclutador.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/uploads/documentos/**", "/uploads/fotos/**", "/uploads/logos/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
						.requestMatchers("/login")
						.permitAll().requestMatchers("/aplicaciones").permitAll()
						.requestMatchers("/aplicaciones/activas").permitAll()
						.requestMatchers("/aplicaciones/{idPerfil}").permitAll()
						.requestMatchers("/aplicaciones/estado/**").permitAll()
						.requestMatchers("/aplicaciones/perfil/{idPerfil}").permitAll()
						.requestMatchers("/aplicaciones/existeId/{idPerfil}").permitAll().requestMatchers("/empresas")
						.permitAll().requestMatchers("/empresas/existe/{cuit}").permitAll()
						.requestMatchers("/empresas/existeId/{id}").permitAll().requestMatchers("/empresas/crear")
						.permitAll().requestMatchers("/empresas/actualizar/{id}").permitAll()
						.requestMatchers("/empresas/eliminar/{id}").permitAll().requestMatchers("/ofertas").permitAll()
						.requestMatchers("/ofertas/disponibles").permitAll().requestMatchers("/ofertas/todas")
						.permitAll().requestMatchers("/ofertas/existeId/{id}").permitAll()
						.requestMatchers("/ofertas/crear").permitAll().requestMatchers("/ofertas/actualizar/{id}")
						.permitAll().requestMatchers("/ofertas/eliminar/{id}").permitAll()
						.requestMatchers("/ofertas/buscar", "/ofertas/buscar/**").permitAll()
						.requestMatchers("/ofertas/todas/activas").permitAll().requestMatchers("/rubro")
						.permitAll().requestMatchers("/rubro/{id}").permitAll().requestMatchers("/rubro/crear")
						.permitAll().requestMatchers("/rubro/eliminar/{id}").permitAll()
						.requestMatchers("/rubro/actualizar/{id}").permitAll().requestMatchers("/usuarios").permitAll()
						.requestMatchers("/usuarios/auth/login").permitAll().requestMatchers("/perfiles/auth/login")
						.permitAll().requestMatchers("/perfiles/{id}").permitAll().requestMatchers("/perfiles")
						.permitAll().requestMatchers("/perfiles/name/{name}").permitAll()
						.requestMatchers("/perfiles/id/{name}").permitAll().requestMatchers("/perfiles/olvide-password")
						.permitAll().requestMatchers("/perfiles/reset-password").permitAll().requestMatchers("/ws")
						.permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
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

	@Bean
	public WebMvcConfigurer webMvcConfigurer(org.springframework.core.env.Environment env) {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(
					org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
				String uploadDir = env.getProperty("app.upload.dir");
				if (uploadDir != null) {
					if (!uploadDir.endsWith("/")) {
						uploadDir += "/";
					}
					String resourcePath = uploadDir.startsWith("/") ? "file:" + uploadDir : "file:///" + uploadDir;
					registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath).setCachePeriod(0);
					System.out.println("=================================================" + resourcePath);
				}
			}

			@Override
			public void configureContentNegotiation(
					org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer configurer) {
				configurer.mediaType("js", org.springframework.http.MediaType.valueOf("application/javascript"));
				configurer.mediaType("css", org.springframework.http.MediaType.valueOf("text/css"));
			    configurer.mediaType("docx", org.springframework.http.MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
			}
		};
	}
}