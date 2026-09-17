package com.gestoria.tickets.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestión de Tickets API")
                        .version("v1")
                        .description("Bienvenido a la API del Sistema de Gestión de Tickets.\n\n" +
                                "Esta interfaz permite consultar y gestionar incidencias técnicas aplicando **Arquitectura Hexagonal** e **Inteligencia Artificial**.\n\n" +
                                "[Ver repositorio en GitHub](https://github.com/juan1016g/GestorIATickets)"));
    }
}