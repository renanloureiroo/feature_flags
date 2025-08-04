# Feature Flags Service

Serviço centralizado de **Feature Flags** com suporte a múltiplos tipos de valores (boolean, number, string, list), cache com invalidação automática, exportação/importação via JSON e observabilidade (Prometheus/Grafana).

---

## TL;DR

```bash
# Subir Postgres, Redis, Prometheus e Grafana
docker compose -f docker/docker-compose.yml up -d

# Rodar a aplicação (perfil dev)
./mvnw spring-boot:run

# Swagger UI
http://localhost:8080/swagger-ui.html

# Métricas Prometheus
http://localhost:8080/actuator/prometheus

# Grafana (admin/admin)
http://localhost:3000
```

## Objetivos

- Cadastrar/alterar flags com valores tipados.
- Buscar todas as flags ou por id/slug.
- Manter cache em memória (Caffeine) e invalidar quando houver alteração (via Redis Pub/Sub).
- Exportar o estado atual em JSON (valores + schemas) para fácil replicação entre ambientes.
- Validar valores com base em JSON Schema.

## Stack

- Linguagem/Framework: Java 21 + Spring Boot 3
- Banco: PostgreSQL (Flyway para migrações)
- Cache: Caffeine (local) + Redis (distribuído / invalidação)
- Docs: OpenAPI (Springdoc), ADRs (MADR), Diagramas (C4 + Mermaid)
- Observabilidade: Micrometer + Prometheus + Grafana | Actuator

## Arquitetura (visão rápida)

- Domain: Entidades e regras de negócio puras.
- Application: Use cases (serviços) orquestrando domínio.
- Infrastructure: Repositórios JPA, Redis, cache, adapters.
- API: Controllers REST, DTOs e validação.

## Estrutura do Projeto

```
feature-flags/
 ├── pom.xml
 ├── src/
 │   ├── main/java/com/renanloureiro/flags/...
 │   ├── main/resources/
 │   │   └── db/migration/            # Flyway (V1__create_tables.sql etc.)
 │   └── test/java/...
 ├── docker/
 │   ├── docker-compose.yml
 │   └── prometheus/prometheus.yml
 └── docs/
     ├── adr/
     ├── diagrams/
     ├── openapi/
     └── schemas/
```
