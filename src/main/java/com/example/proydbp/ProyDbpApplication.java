package com.example.proydbp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ProyDbpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyDbpApplication.class, args);
	}


	//Model Mapper
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	//Configuración de Cors
	@Bean
	public WebMvcConfigurer corsMappingConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:8081")//aplicación frontend
						.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD","PATCH")
						.maxAge(3600)
						.allowedHeaders("*")
						.allowCredentials(true);
			};
		};
	};
}
