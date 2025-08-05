# Observabilidade

Guia completo do sistema de observabilidade do projeto Feature Flags.

## Visão Geral

O projeto inclui um sistema completo de observabilidade com:

- **Métricas** - Prometheus + Grafana
- **Tracing** - Jaeger + OpenTelemetry
- **Logs** - Logback estruturado
- **Alertas** - Regras de alerta automáticas

## Quick Start

### 1. Subir Infraestrutura

```bash
# Subir todos os serviços de observabilidade
docker compose up -d prometheus grafana jaeger
```

### 2. Rodar Aplicação

```bash
# Rodar com observabilidade habilitada
./mvnw spring-boot:run
```

### 3. Acessar Ferramentas

```bash
# Grafana - Visualização de métricas
open http://localhost:3000
# Login: admin/admin

# Prometheus - Coleta de métricas
open http://localhost:9090

# Jaeger - Tracing distribuído
open http://localhost:16686
```

## Métricas (Prometheus + Grafana)

### Métricas Disponíveis

#### Métricas da Aplicação

- **`http_server_requests_seconds`** - Tempo de resposta das requisições
- **`http_server_requests_total`** - Total de requisições por endpoint
- **`process_cpu_usage`** - Uso de CPU
- **`jvm_memory_used_bytes`** - Uso de memória JVM
- **`hikaricp_connections_active`** - Conexões ativas do banco

#### Métricas Customizadas

- **`feature_flags_created_total`** - Total de feature flags criadas
- **`feature_flags_validation_errors_total`** - Erros de validação
- **`feature_flags_cache_hits`** - Cache hits
- **`feature_flags_cache_misses`** - Cache misses

### Dashboards Grafana

#### Dashboard Principal

- **Taxa de requisições** por segundo
- **Tempo de resposta** (médio, 95º percentil)
- **Taxa de erros** (%)
- **Uso de recursos** (CPU, memória)
- **Performance do banco** (conexões, queries)

#### Dashboard de Feature Flags

- **Feature flags criadas** por hora/dia
- **Erros de validação** por tipo
- **Performance de cache** (hit/miss ratio)
- **Tipos de feature flags** mais usados

### Configuração Prometheus

```yaml
# prometheus/prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: "feature-flags"
    static_configs:
      - targets: ["host.docker.internal:8080"]
    metrics_path: "/actuator/prometheus"
```

### Alertas

```yaml
# prometheus/alert_rules.yml
groups:
  - name: feature-flags
    rules:
      - alert: HighErrorRate
        expr: rate(http_server_requests_total{status=~"5.."}[5m]) > 0.1
        for: 2m
        labels:
          severity: warning
        annotations:
          summary: "High error rate detected"

      - alert: HighResponseTime
        expr: histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m])) > 1
        for: 2m
        labels:
          severity: warning
        annotations:
          summary: "High response time detected"
```

## Tracing (Jaeger + OpenTelemetry)

### Traces Automáticos

O Spring Boot automaticamente instrumenta:

- **Requisições HTTP** - Controllers REST
- **Queries de banco** - JPA/Hibernate
- **Chamadas Redis** - Cache operations
- **Métricas customizadas** - Business operations

### Traces Manuais

```java
@RestController
public class FeatureFlagController {

    @PostMapping("/v1/feature-flags")
    public ResponseEntity<FeatureFlagResponseDTO> createFeatureFlag(
            @Valid @RequestBody CreateFeatureFlagDTO dto) {

        // Trace manual para operação de negócio
        Span span = tracer.spanBuilder("create-feature-flag")
            .setAttribute("feature.flag.name", dto.getName())
            .setAttribute("feature.flag.type", dto.getType().name())
            .startSpan();

        try (var scope = span.makeCurrent()) {
            // Lógica de criação
            var result = createFeatureFlagUseCase.execute(dto);
            span.setAttribute("feature.flag.id", result.getId().toString());
            return ResponseEntity.status(201).body(FeatureFlagPresenter.toHttp(result));
        } catch (Exception e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
```

### Configuração OpenTelemetry

```yaml
# application.yaml
management:
  tracing:
    sampling:
      probability: 1.0
    propagation:
      type: w3c
  otel:
    traces:
      exporter: jaeger
    metrics:
      exporter: prometheus
```

### Visualização Jaeger

- **Service Map** - Visualização de serviços
- **Trace List** - Lista de traces
- **Trace Details** - Detalhes de um trace específico
- **Dependencies** - Dependências entre serviços

## Logs Estruturados

### Configuração Logback

```xml
<!-- logback-spring.xml -->
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <message/>
                <mdc/>
                <stackTrace/>
            </providers>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

### Logs Automáticos

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "level": "INFO",
  "logger": "com.renanloureiro.feature_flags.application.usecases.CreateFeatureFlagUseCase",
  "message": "Feature flag created successfully",
  "feature_flag_id": "550e8400-e29b-41d4-a716-446655440000",
  "feature_flag_name": "dark-mode",
  "trace_id": "abc123def456",
  "span_id": "xyz789"
}
```

### Logs Customizados

```java
@Slf4j
@Service
public class CreateFeatureFlagUseCase {

    public FeatureFlag execute(CreateFeatureFlagDTO dto) {
        log.info("Creating feature flag",
            Map.of(
                "name", dto.getName(),
                "type", dto.getType().name()
            ));

        // Lógica de criação

        log.info("Feature flag created successfully",
            Map.of(
                "id", result.getId(),
                "slug", result.getSlug()
            ));

        return result;
    }
}
```

## Alertas e Monitoramento

### Alertas Automáticos

#### Performance

- **Alta taxa de erro** (> 5% por 5 minutos)
- **Tempo de resposta alto** (> 1s no 95º percentil)
- **Alto uso de CPU** (> 80% por 5 minutos)
- **Alto uso de memória** (> 85% do heap)

#### Business

- **Muitos erros de validação** (> 10 por hora)
- **Cache miss rate alto** (> 20%)
- **Falhas de conexão com banco**
- **Falhas de conexão com Redis**

### Notificações

```yaml
# grafana/provisioning/alerting/notifications.yml
notifiers:
  - name: email
    type: email
    settings:
      addresses: admin@company.com

  - name: slack
    type: slack
    settings:
      url: https://hooks.slack.com/services/xxx
```

## Configuração Detalhada

### Micrometer

```yaml
# application.yaml
management:
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      application: feature-flags
      environment: development
    distribution:
      percentiles-histogram:
        http.server.requests: true
      percentiles:
        http.server.requests: 0.5, 0.95, 0.99
```

### OpenTelemetry

```yaml
# application.yaml
management:
  otel:
    traces:
      exporter: jaeger
      endpoint: http://localhost:14250
    metrics:
      exporter: prometheus
    logs:
      exporter: otlp
    propagators: w3c
```

### Jaeger

```yaml
# docker-compose.yml
jaeger:
  image: jaegertracing/all-in-one:latest
  ports:
    - "16686:16686"
    - "14250:14250"
  environment:
    - COLLECTOR_OTLP_ENABLED=true
```

## Dashboards

### Dashboard Principal

**URL**: `http://localhost:3000/d/feature-flags/main`

**Métricas incluídas**:

- Request rate (req/s)
- Response time (p50, p95, p99)
- Error rate (%)
- CPU usage (%)
- Memory usage (MB)
- Database connections
- Cache hit/miss ratio

### Dashboard de Feature Flags

**URL**: `http://localhost:3000/d/feature-flags/business`

**Métricas incluídas**:

- Feature flags created per hour
- Feature flags by type
- Validation errors by type
- Cache performance
- Database query performance

## 🧪 Testes de Observabilidade

### Script de Teste

```bash
#!/bin/bash
# scripts/test-observability.sh

echo "🧪 Testing Observability Stack"

# Test Prometheus
echo "Testing Prometheus..."
curl -f http://localhost:9090/api/v1/status/config || exit 1

# Test Grafana
echo "Testing Grafana..."
curl -f http://localhost:3000/api/health || exit 1

# Test Jaeger
echo "Testing Jaeger..."
curl -f http://localhost:16686/api/services || exit 1

# Generate some traffic
echo "Generating test traffic..."
for i in {1..10}; do
  curl -X POST "http://localhost:8080/v1/feature-flags" \
    -H "Content-Type: application/json" \
    -d "{
      \"name\": \"test-flag-$i\",
      \"description\": \"Test flag $i\",
      \"type\": \"BOOLEAN\",
      \"schema\": {\"type\": \"boolean\"}
    }" &
done

wait

echo "✅ Observability stack is working correctly!"
```

### Verificação Manual

```bash
# Verificar métricas
curl http://localhost:8080/actuator/metrics

# Verificar health
curl http://localhost:8080/actuator/health

# Verificar traces
curl http://localhost:16686/api/services

# Verificar logs
docker logs feature_flags-app-1 | tail -20
```

## Troubleshooting

### Problemas Comuns

#### 1. Prometheus não coleta métricas

```bash
# Verificar se a aplicação está expondo métricas
curl http://localhost:8080/actuator/prometheus

# Verificar configuração do Prometheus
docker exec -it feature_flags-prometheus-1 cat /etc/prometheus/prometheus.yml
```

#### 2. Jaeger não mostra traces

```bash
# Verificar se OpenTelemetry está configurado
curl http://localhost:8080/actuator/health

# Verificar logs da aplicação
docker logs feature_flags-app-1 | grep -i trace
```

#### 3. Grafana não carrega dashboards

```bash
# Verificar se Grafana está conectado ao Prometheus
curl http://localhost:3000/api/datasources

# Verificar logs do Grafana
docker logs feature_flags-grafana-1
```

### Logs de Debug

```bash
# Logs detalhados de métricas
mvn spring-boot:run -Dlogging.level.io.micrometer=DEBUG

# Logs detalhados de tracing
mvn spring-boot:run -Dlogging.level.io.opentelemetry=DEBUG

# Logs detalhados de cache
mvn spring-boot:run -Dlogging.level.org.springframework.cache=DEBUG
```

## 📚 Recursos Adicionais

- **[Prometheus Documentation](https://prometheus.io/docs/)**
- **[Grafana Documentation](https://grafana.com/docs/)**
- **[Jaeger Documentation](https://www.jaegertracing.io/docs/)**
- **[OpenTelemetry Documentation](https://opentelemetry.io/docs/)**
- **[Micrometer Documentation](https://micrometer.io/docs/)**

## 🔮 Próximas Melhorias

1. **Log Aggregation** - ELK Stack (Elasticsearch, Logstash, Kibana)
2. **APM** - Application Performance Monitoring
3. **Synthetic Monitoring** - Testes automatizados de endpoints
4. **Real User Monitoring** - Métricas de usuários reais
5. **Custom Dashboards** - Dashboards específicos por equipe
