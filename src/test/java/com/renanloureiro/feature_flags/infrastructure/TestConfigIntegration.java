package com.renanloureiro.feature_flags.infrastructure;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ActiveProfiles("test")
public class TestConfigIntegration {
  // Usa as implementações reais dos repositórios
  // Configuração para testes de integração com banco real
}