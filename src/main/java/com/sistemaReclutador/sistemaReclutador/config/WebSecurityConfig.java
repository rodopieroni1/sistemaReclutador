package com.sistemaReclutador.sistemaReclutador.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${cors.allowed-origins:http://localhost:4200}")
	private String allowedOrigins;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/uploads/**", "/login", "/ws").permitAll()
						.requestMatchers("/aplicaciones/**", "/empresas/**", "/ofertas/**", "/rubro/**", "/usuarios/**", "/perfiles/**").permitAll()
						.anyRequest().authenticated()
				)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		
		List<String> origins = Arrays.asList(allowedOrigins.split(","));
		config.setAllowedOriginPatterns(origins); 
		
		config.setAllowedHeaders(List.of("*"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setExposedHeaders(List.of("Authorization", "Content-Disposition"));

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
				String uploadDir = env.getProperty("app.upload.dir", "C:/uploads/");
				if (!uploadDir.endsWith("/")) {
					uploadDir += "/";
				}
				
				String resourcePath = "file:///" + uploadDir.replace("\\", "/");
				
				registry.addResourceHandler("/uploads/**")
						.addResourceLocations(resourcePath)
						.setCachePeriod(0);
			}

			@Override
			public void configureContentNegotiation(
					org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer configurer) {
				configurer.mediaType("js", org.springframework.http.MediaType.valueOf("application/javascript"));
				configurer.mediaType("css", org.springframework.http.MediaType.valueOf("text/css"));
				configurer.mediaType("docx", org.springframework.http.MediaType
						.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
			}
		};
	}
}