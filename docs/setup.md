# Setup do Projeto

Guia completo para configurar e rodar o projeto Feature Flags.

## 📋 Pré-requisitos

- **Java 21+**
- **Maven 3.8+**
- **Docker & Docker Compose**
- **Git**

## Setup Rápido

### 1. Clone o Repositório

```bash
git clone <repository-url>
cd feature_flags
```

### 2. Subir Infraestrutura

```bash
# Subir todos os serviços (PostgreSQL, Redis, Prometheus, Grafana, Jaeger)
docker compose up -d
```

### 3. Rodar a Aplicação

```bash
# Usando Maven Wrapper
./mvnw spring-boot:run

# Ou usando Maven local
mvn spring-boot:run
```

### 4. Verificar se Está Funcionando

```bash
# Health check
curl http://localhost:8080/actuator/health

# Swagger UI
open http://localhost:8080/api/swagger-ui.html
```

## Configuração Detalhada

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=feature_flags
DB_USER=postgres
DB_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Application
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev
```

### Configuração do Banco

O projeto usa **PostgreSQL** com **Flyway** para migrações automáticas.

```yaml
# application.yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/feature_flags
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    locations: classpath:db/migration
```

### Configuração do Redis

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    timeout: 2000ms
```

## 🐳 Docker Compose

O arquivo `docker-compose.yml` inclui:

- **PostgreSQL 15** - Banco de dados principal
- **Redis 7** - Cache distribuído
- **Prometheus** - Coleta de métricas
- **Grafana** - Visualização de métricas
- **Jaeger** - Tracing distribuído

```bash
# Subir todos os serviços
docker compose up -d

# Ver logs
docker compose logs -f

# Parar todos os serviços
docker compose down
```

## 🧪 Testes

### Executar Testes

```bash
# Todos os testes
mvn test

# Testes com cobertura
mvn test jacoco:report

# Testes específicos
mvn test -Dtest=JsonSchemaValidationServiceTest
```

### Testes de Integração

```bash
# Testes com banco real
mvn test -Dspring.profiles.active=test
```

## Debug

### Logs da Aplicação

```bash
# Ver logs em tempo real
tail -f logs/application.log

# Logs do Spring Boot
mvn spring-boot:run -Dlogging.level.com.renanloureiro=DEBUG
```

### Banco de Dados

```bash
# Conectar no PostgreSQL
docker exec -it feature_flags-postgres-1 psql -U postgres -d feature_flags

# Ver tabelas
\dt

# Ver dados
SELECT * FROM feature_flags;
```

### Redis

```bash
# Conectar no Redis
docker exec -it feature_flags-redis-1 redis-cli

# Ver chaves
KEYS *

# Ver valor de uma chave
GET feature_flag:123
```

## Troubleshooting

### Problemas Comuns

#### 1. Porta 8080 já em uso

```bash
# Verificar o que está usando a porta
lsof -i :8080

# Matar o processo
kill -9 <PID>

# Ou usar porta diferente
mvn spring-boot:run -Dserver.port=8081
```

#### 2. Banco não conecta

```bash
# Verificar se PostgreSQL está rodando
docker compose ps

# Reiniciar PostgreSQL
docker compose restart postgres
```

#### 3. Redis não conecta

```bash
# Verificar se Redis está rodando
docker compose ps

# Reiniciar Redis
docker compose restart redis
```

### Logs de Debug

```bash
# Logs detalhados do Spring Boot
mvn spring-boot:run -Dlogging.level.root=DEBUG

# Logs específicos do banco
mvn spring-boot:run -Dlogging.level.org.hibernate.SQL=DEBUG
```

## Monitoramento

### Health Checks

```bash
# Health geral
curl http://localhost:8080/actuator/health

# Health do banco
curl http://localhost:8080/actuator/health/db

# Health do Redis
curl http://localhost:8080/actuator/health/redis
```

### Métricas

```bash
# Métricas do Prometheus
curl http://localhost:8080/actuator/prometheus

# Métricas customizadas
curl http://localhost:8080/actuator/metrics
```

## 🔄 Desenvolvimento

### Hot Reload

```bash
# Com Spring Boot DevTools
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=true"
```

### IDE Setup

1. **IntelliJ IDEA**

   - Importar como projeto Maven
   - Configurar SDK Java 21
   - Habilitar annotation processing

2. **VS Code**
   - Instalar extensão Java
   - Instalar extensão Spring Boot
   - Configurar Java Home

### Git Hooks

```bash
# Pre-commit hook para testes
cp scripts/pre-commit .git/hooks/
chmod +x .git/hooks/pre-commit
```
