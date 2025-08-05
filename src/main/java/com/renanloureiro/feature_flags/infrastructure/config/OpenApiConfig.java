package com.renanloureiro.feature_flags.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Feature Flags API")
            .description(
                "API para gerenciamento de feature flags com suporte a diferentes tipos de flags e valores dinâmicos")
            .version("1.0.0")
            .contact(new Contact()
                .name("Renan Loureiro")
                .email("renanloureiro.dev@gmail.com")
                .url("https://github.com/renanloureiro"))
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")))
        .servers(List.of(
            new Server()
                .url("http://localhost:8080/api")
                .description("Servidor de Desenvolvimento"),
            new Server()
                .url("https://api.featureflags.com/api")
                .description("Servidor de Produção")));
  }
}