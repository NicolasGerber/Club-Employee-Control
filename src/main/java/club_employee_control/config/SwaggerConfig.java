package club_employee_control.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.OpenAPIService;
import org.springframework.data.domain.Pageable;
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Club Employee Control API")
                        .description("API de gestão de funcionários de clube de futebol")
                        .version("v1"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
    @Bean
    public OperationCustomizer pageableOperationCustomizer() {
        return (operation, handlerMethod) -> {
            if (handlerMethod.getMethodParameters() != null) {
                for (var param : handlerMethod.getMethodParameters()) {
                    if (Pageable.class.isAssignableFrom(param.getParameterType())) {
                        // Remove o parâmetro Pageable genérico que o Swagger gera errado
                        if (operation.getParameters() != null) {
                            operation.getParameters().removeIf(p ->
                                    p.getName().equals("pageable"));
                        }
                    }
                }
            }
            return operation;
        };
    }
}