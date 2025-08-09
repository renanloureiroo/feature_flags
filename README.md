# Feature Flags Service

Serviço centralizado de **Feature Flags** com suporte a múltiplos tipos de valores, validação de schemas JSON e observabilidade completa.

## Quick Start

```bash
# 1. Subir infraestrutura
docker compose up -d

# 2. Rodar aplicação
./mvnw spring-boot:run

# 3. Acessar ferramentas
# API: http://localhost:8080/v1/feature-flags
# Swagger: http://localhost:8080/api/swagger-ui.html
# Grafana: http://localhost:3000 (admin/admin)
# Prometheus: http://localhost:9090
# Jaeger: http://localhost:16686
```

## 📋 Funcionalidades

- ✅ **Feature Flags tipadas** (BOOLEAN, NUMBER, STRING, LIST)
- ✅ **Validação de schemas** JSON Schema
- ✅ **Cache distribuído** com Redis
- ✅ **Observabilidade completa** (Métricas, Tracing, Logs)
- ✅ **Documentação automática** OpenAPI/Swagger
- ✅ **Testes automatizados** com cobertura

## Arquitetura

- **Domain-Driven Design** (DDD)
- **Clean Architecture** com separação clara de responsabilidades
- **Spring Boot 3** com Java 21
- **PostgreSQL** com Flyway para migrações
- **Redis** para cache distribuído
- **Prometheus + Grafana** para métricas
- **Jaeger** para tracing

## 📁 Estrutura do Projeto

```
feature-flags/
├── src/main/java/
│   └── com/renanloureiro/feature_flags/
│       ├── application/           # Use cases e validações
│       ├── domain/               # Entidades e regras de negócio
│       └── infrastructure/       # Repositórios e adapters
├── docs/                         # Documentação completa
├── docker-compose.yml           # Infraestrutura local
└── scripts/                     # Scripts de automação
```

## 📚 Documentação

- **[Setup do Projeto](docs/setup.md)** - Como configurar e rodar
- **[Arquitetura](docs/architecture.md)** - Detalhes da arquitetura
- **[API Reference](docs/api.md)** - Documentação da API
- **[ADRs](docs/adr/)** - Decisões arquiteturais

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar com cobertura
mvn test jacoco:report
```

## Configuração

- **Java 21+**
- **Maven 3.8+**
- **Docker & Docker Compose**

## 📄 Licença

MIT License
