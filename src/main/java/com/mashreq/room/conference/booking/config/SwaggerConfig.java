package com.mashreq.room.conference.booking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    final String securitySchemeName = "bearer-key";

    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/**")

                .build();
    }

    @Bean
    OpenAPI customOpenAPI() {

        return new OpenAPI()

                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Conference Room Booking API")
                        .version("1.0.0"))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")

                                )
                );


    }

    @Bean
    public GlobalOpenApiCustomizer addHeadersCustomiser() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
//            if (!operation.getTags().contains("authentication-controller")) {
//                operation.addParametersItem(new Parameter().name("Authorization").required(true).in("header").schema(new Schema<>().type("string").example("Bearer Token12345..etc")));
//            }
            operation.addParametersItem(new Parameter().name("request-id").required(true).in("header").schema(new Schema<>().type("string")));
            operation.addParametersItem(new Parameter().name("Accept").required(true).in("header").schema(new Schema<>().type("string")._default("application/json")));
            operation.addParametersItem(new Parameter().name("Content-Type").required(true).in("header").schema(new Schema<>().type("string")._default("application/json")));
        }));
    }

}