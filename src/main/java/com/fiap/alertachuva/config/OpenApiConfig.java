package com.fiap.alertachuva.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI alertachuvaOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Alerta Chuva API")
                .description("Sistema de monitoramento de áreas de risco - Global Solution FIAP 2025/1")
                .version("v1.0"));
    }
}
