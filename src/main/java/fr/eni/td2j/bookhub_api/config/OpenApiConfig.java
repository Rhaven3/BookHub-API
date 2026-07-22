package fr.eni.td2j.bookhub_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3.1 documentation configuration for the TaskFlow API.
 *
 * <p>Configures the Swagger UI metadata via {@link OpenAPIDefinition}
 * and registers a global Bearer token security scheme via {@link SecurityScheme},
 * allowing JWT authentication directly from the Swagger UI interface.</p>
 *
 * <p>The Swagger UI is accessible at:
 * <a href="http://localhost:8082/swagger-ui/index.html">
 * http://localhost:8082/swagger-ui/index.html</a></p>
 *
 * <p>The API documentation (Redoc) is accessible at:
 * <a href="http://localhost:4200/api-docs">
 * http://localhost:4200/api-docs</a> (requires Angular frontend)</p>
 *
 * @see <a href="https://swagger.io/specification/">OpenAPI Specification</a>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "TaskFlow API",
                version = "1.0.0",
                description = "blabla",
                contact = @Contact(
                        name = "Mehdi Rochereau",
                        url = "https://github.com/mehdi-rochereau/taskflow-api"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development server"),
                @Server(url = "https://api.taskflow.mehdi-rochereau.dev", description = "Production server")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT Bearer token obtained from POST /api/auth/login or POST /api/auth/register"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addParameters("Accept-Language", new Parameter()
                                .in("header")
                                .name("Accept-Language")
                                .description("Language for error messages. Supported values: `en` (default), `fr`")
                                .required(false)
                                .schema(new StringSchema()
                                        .addEnumItem("en")
                                        .addEnumItem("fr")
                                        ._default("en"))
                        )
                );
    }
}