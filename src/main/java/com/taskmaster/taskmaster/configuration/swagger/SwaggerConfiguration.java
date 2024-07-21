package com.taskmaster.taskmaster.configuration.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Diffa Azkhani",
            email = "diffaazkhani@gmail.com",
            url = "https://www.linkedin.com/in/diffaazkhani"
        ),
        description = "Swagger open api docs for Task Master project",
        title = "Open api specifications - Task Master"
    ),
    servers = {
        @Server(
            description = "Local Environment",
            url = "http://localhost:8080"
        ),
        @Server(
            description = "Production Environment",
            url = "COMING SOON"
        )
    },
    security = {
        @SecurityRequirement(
            name = "bearerAuth"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "Put your bearer token to validate your credentials",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class SwaggerConfiguration {

}
