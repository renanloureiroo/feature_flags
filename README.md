# Feature Flags Service

Serviço centralizado de **Feature Flags** com suporte a múltiplos tipos de valores (boolean, number, string, list), cache com invalidação automática, exportação/importação via JSON e observabilidade completa (Prometheus/Grafana/Jaeger).

---

## TL;DR

```bash
# Subir toda a infraestrutura (Postgres, Redis, Prometheus, Grafana, Jaeger)
docker compose up -d

# Rodar a aplicação
./mvnw spring-boot:run

# Testar o sistema de observabilidade
./scripts/test-observability.sh

# Acessar as ferramentas
# Grafana: http://localhost:3000 (admin/admin)
# Prometheus: http://localhost:9090
# Jaeger: http://localhost:16686
# API: http://localhost:8080/swagger-ui.html
```

## Objetivos

- Cadastrar/alterar flags com valores tipados.
- Buscar todas as flags ou por id/slug.
- Manter cache em memória (Caffeine) e invalidar quando houver alteração (via Redis Pub/Sub).
- Exportar o estado atual em JSON (valores + schemas) para fácil replicação entre ambientes.
- Validar valores com base em JSON Schema.

## Stack

- **Linguagem/Framework**: Java 21 + Spring Boot 3
- **Banco**: PostgreSQL (Flyway para migrações)
- **Cache**: Caffeine (local) + Redis (distribuído / invalidação)
- **Docs**: OpenAPI (Springdoc), ADRs (MADR), Diagramas (C4 + Mermaid)
- **Observabilidade**:
  - **Métricas**: Micrometer + Prometheus + Grafana
  - **Tracing**: OpenTelemetry + Jaeger
  - **Logs**: Logback + OpenTelemetry
  - **Health**: Spring Boot Actuator

## Arquitetura (visão rápida)

- Domain: Entidades e regras de negócio puras.
- Application: Use cases (serviços) orquestrando domínio.
- Infrastructure: Repositórios JPA, Redis, cache, adapters.
- API: Controllers REST, DTOs e validação.

## Estrutura do Projeto

```
feature-flags/
 ├── pom.xml
 ├── docker-compose.yml              # Infraestrutura completa
 ├── scripts/
 │   └── test-observability.sh      # Script de teste
 ├── prometheus/                     # Configurações Prometheus
 │   ├── prometheus.yml             # Configuração principal
 │   ├── alert_rules.yml            # Regras de alerta
 │   └── README.md                  # Documentação
 ├── grafana/                       # Configurações Grafana
 │   ├── provisioning/              # Configuração automática
 │   ├── dashboards/                # Dashboards JSON
 │   └── README.md                  # Documentação
 ├── jaeger/                        # Configurações Jaeger
 │   └── README.md                  # Documentação
 ├── src/
 │   ├── main/java/com/renanloureiro/feature_flags/
 │   │   ├── api/                   # Controllers REST
 │   │   ├── application/           # Use cases
 │   │   ├── domain/                # Entidades e regras
 │   │   └── infrastructure/        # Repositórios e adapters
 │   ├── main/resources/
 │   │   ├── application.yaml       # Configuração da aplicação
 │   │   └── db/migration/          # Flyway migrations
 │   └── test/java/
 └── docs/
     ├── observabilidade.md          # Guia completo de observabilidade
     ├── adr/                       # Architecture Decision Records
     ├── diagrams/                   # Diagramas C4/Mermaid
     ├── openapi/                    # Especificações OpenAPI
     └── schemas/                    # JSON Schemas
```

## 📊 Observabilidade

O projeto inclui um sistema completo de observabilidade com:

### 🎯 **Métricas (Prometheus + Grafana)**

- **Taxa de requisições** por segundo
- **Tempo de resposta** (médio, 95º percentil)
- **Taxa de erros** (%)
- **Uso de recursos** (CPU, memória, conexões de banco)
- **Dashboards** pré-configurados no Grafana

### 🔍 **Tracing (Jaeger + OpenTelemetry)**

- **Rastreamento automático** de requisições HTTP
- **Traces manuais** para operações customizadas
- **Visualização** do fluxo completo de requisições
- **Análise de performance** detalhada

### 📝 **Logs Estruturados**

- **Logs automáticos** do Spring Boot
- **Integração** com OpenTelemetry
- **Correlação** entre logs e traces

### ⚠️ **Alertas**

- **Alertas automáticos** para problemas de performance
- **Notificações** configuráveis
- **Thresholds** baseados em métricas reais

### 🚀 **Como Usar**

```bash
# 1. Subir infraestrutura
docker compose up -d

# 2. Rodar aplicação
./mvnw spring-boot:run

# 3. Testar sistema
./scripts/test-observability.sh

# 4. Acessar ferramentas
# Grafana: http://localhost:3000 (admin/admin)
# Prometheus: http://localhost:9090
# Jaeger: http://localhost:16686
```

**📖 Documentação completa**: [docs/observabilidade.md](docs/observabilidade.md)
