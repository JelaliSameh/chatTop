package com.chatTop.backend.configuration;
import io.swagger.v3.oas.models.Components;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	   /**
     * Cette méthode configure l'instance OpenAPI pour la documentation de l'API.
     *
     * @return Une instance de OpenAPI configurée avec les schémas de sécurité.
     */
	@Bean
	OpenAPI customnOpenApiConfig() {
	    final String securitySchemeName = "bearerAuth";

	    return new OpenAPI()
	            .info(new io.swagger.v3.oas.models.info.Info()
	                    .title("ChatTop API")
	                    .description("Documentation des endpoints REST pour ChatTop")
	                    .version("1.0.0"))
	            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
	            .components(new Components()
	                    .addSecuritySchemes(securitySchemeName,
	                            new SecurityScheme()
	                                    .name(securitySchemeName)
	                                    .type(SecurityScheme.Type.HTTP)
	                                    .scheme("bearer")
	                                    .bearerFormat("JWT")));
	}}
