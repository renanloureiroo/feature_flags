# Observabilidade - Feature Flags Service

Este documento explica como usar o sistema completo de observabilidade configurado para o serviço de Feature Flags.

## O que é Observabilidade?

Observabilidade é a capacidade de entender o estado interno de um sistema através de suas saídas externas. No nosso caso, usamos três pilares:

### 1. **Métricas (Prometheus)**

- **O que é**: Dados numéricos sobre o comportamento do sistema
- **Exemplos**: Taxa de requisições, tempo de resposta, uso de CPU/memória
- **Ferramenta**: Prometheus

### 2. **Logs**

- **O que é**: Registros de eventos que aconteceram no sistema
- **Exemplos**: Erros, informações de debug, auditoria
- **Ferramenta**: Logs estruturados + OpenTelemetry

### 3. **Tracing (Jaeger)**

- **O que é**: Rastreamento de requisições através de diferentes serviços
- **Exemplos**: Visualizar o caminho completo de uma requisição
- **Ferramenta**: Jaeger + OpenTelemetry

## Como Usar

### 1. Subir a Infraestrutura

```bash
# Subir todos os serviços
docker compose up -d

# Verificar se tudo está rodando
docker compose ps
```

### 2. Acessar as Ferramentas

| Ferramenta     | URL                    | Credenciais | Descrição                     |
| -------------- | ---------------------- | ----------- | ----------------------------- |
| **Grafana**    | http://localhost:3000  | admin/admin | Dashboards e visualizações    |
| **Prometheus** | http://localhost:9090  | -           | Coleta e consulta de métricas |
| **Jaeger**     | http://localhost:16686 | -           | Visualização de traces        |

### 3. Rodar a Aplicação

```bash
# Rodar a aplicação Spring Boot
./mvnw spring-boot:run
```

## Dashboards Disponíveis

### Grafana - Dashboard Principal

O dashboard `feature-flags-dashboard.json` inclui:

1. **Request Rate** - Taxa de requisições por segundo
2. **95th Percentile Response Time** - Tempo de resposta (95º percentil)
3. **Error Rate** - Taxa de erros (%)
4. **Memory Usage** - Uso de memória
5. **CPU Usage** - Uso de CPU (%)
6. **Database Connections** - Conexões ativas com o banco

### Como Adicionar Novos Gráficos

1. Acesse o Grafana (http://localhost:3000)
2. Vá em "Add panel"
3. Use PromQL para consultar métricas

**Exemplos de PromQL úteis:**

```promql
# Taxa de requisições por endpoint
rate(http_server_requests_seconds_count{application="feature-flags-api"}[5m])

# Tempo médio de resposta
rate(http_server_requests_seconds_sum{application="feature-flags-api"}[5m]) / rate(http_server_requests_seconds_count{application="feature-flags-api"}[5m])

# Uso de memória heap
jvm_memory_used_bytes{application="feature-flags-api", area="heap"}

# Conexões de banco ativas
hikaricp_connections_active{application="feature-flags-api"}
```

## Tracing com Jaeger

### Visualizando Traces

1. Acesse http://localhost:16686
2. Selecione o serviço "feature-flags-api"
3. Clique em "Find Traces"

### Traces Automáticos

O Spring Boot com OpenTelemetry automaticamente rastreia:

- Requisições HTTP
- Operações de banco de dados
- Chamadas Redis
- Métricas JVM

### Traces Manuais

Para adicionar tracing manual (como no `FeatureFlagController`):

```java
@Autowired
private Tracer tracer;

public void minhaOperacao() {
    Span span = tracer.spanBuilder("minha-operacao")
        .setAttribute("meu.parametro", "valor")
        .startSpan();

    try (var scope = span.makeCurrent()) {
        // Sua lógica aqui
        span.addEvent("evento.importante");
    } finally {
        span.end();
    }
}
```

## Alertas Configurados

O Prometheus está configurado com alertas para:

1. **HighErrorRate** - Taxa de erro > 5%
2. **HighResponseTime** - Tempo de resposta > 2s (95º percentil)
3. **HighMemoryUsage** - Uso de memória > 80%
4. **HighCPUUsage** - Uso de CPU > 80%
5. **DatabaseConnectionPoolExhausted** - Pool de conexões > 80%

### Configurar Notificações

Para receber notificações dos alertas, você pode:

1. Configurar um AlertManager
2. Integrar com Slack/Email
3. Usar webhooks

## Métricas Disponíveis

### Métricas HTTP

- `http_server_requests_seconds_count` - Contador de requisições
- `http_server_requests_seconds_sum` - Soma dos tempos de resposta
- `http_server_requests_seconds_bucket` - Histograma de tempos

### Métricas JVM

- `jvm_memory_used_bytes` - Memória usada
- `jvm_memory_max_bytes` - Memória máxima
- `process_cpu_seconds_total` - CPU usado

### Métricas de Banco

- `hikaricp_connections_active` - Conexões ativas
- `hikaricp_connections_max` - Conexões máximas
- `hikaricp_connections_idle` - Conexões ociosas

### Métricas de Cache

- `cache_gets_total` - Hits do cache
- `cache_puts_total` - Puts no cache
- `cache_evictions_total` - Evicções do cache

## Configuração Avançada

### Adicionar Métricas Customizadas

```java
@Component
public class CustomMetrics {

    private final Counter featureFlagCounter;
    private final Timer featureFlagTimer;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.featureFlagCounter = Counter.builder("feature_flags_created")
            .description("Número de feature flags criadas")
            .register(meterRegistry);

        this.featureFlagTimer = Timer.builder("feature_flag_creation_time")
            .description("Tempo para criar feature flag")
            .register(meterRegistry);
    }

    public void incrementFeatureFlagCount() {
        featureFlagCounter.increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start();
    }
}
```

### Configurar Sampling de Traces

No `application.yaml`:

```yaml
otel:
  traces:
    sampler: parentbased_traceidratio
    sampler_arg: 0.1 # 10% dos traces
```

### Adicionar Logs Estruturados

```java
@Slf4j
public class MeuServico {

    public void minhaOperacao(String id) {
        log.info("Iniciando operação",
            Map.of("id", id, "operation", "create_flag"));

        // ... lógica ...

        log.info("Operação concluída",
            Map.of("id", id, "status", "success"));
    }
}
```

## Troubleshooting

### Prometheus não está coletando métricas

1. Verifique se a aplicação está rodando na porta 8080
2. Acesse http://localhost:8080/actuator/prometheus
3. Verifique se o target está "UP" no Prometheus

### Grafana não mostra dados

1. Verifique se o datasource Prometheus está configurado
2. Teste a conexão no Grafana
3. Verifique se há métricas no Prometheus

### Jaeger não mostra traces

1. Verifique se a aplicação está enviando traces para localhost:14250
2. Verifique os logs da aplicação para erros de OpenTelemetry
3. Confirme se o Jaeger está rodando

### Performance Impact

- Métricas: Impacto mínimo (< 1% CPU)
- Tracing: Pode impactar performance se sampling muito alto
- Logs: Impacto baixo com configuração adequada

## 📚 Recursos Adicionais

- [Prometheus Query Language](https://prometheus.io/docs/prometheus/latest/querying/)
- [Grafana Dashboard Tutorial](https://grafana.com/docs/grafana/latest/dashboards/)
- [OpenTelemetry Java](https://opentelemetry.io/docs/instrumentation/java/)
- [Jaeger Documentation](https://www.jaegertracing.io/docs/)
