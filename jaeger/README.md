# Jaeger Configuration

Esta pasta contém configurações do Jaeger (tracing).

## Sobre o Jaeger

O Jaeger é usado para visualizar traces (rastreamento) de requisições através da aplicação.

## Funcionalidades

- **Visualização de traces** - Fluxo completo de requisições
- **Análise de performance** - Identificação de gargalos
- **Debugging** - Correlação entre logs e traces
- **Distributed tracing** - Rastreamento entre serviços

## Acesso

- **URL**: http://localhost:16686
- **Porta UI**: 16686
- **Porta Collector HTTP**: 14268
- **Porta Collector gRPC**: 14250

## Como Usar

1. **Acesse** http://localhost:16686
2. **Selecione** o serviço "feature-flags-api"
3. **Clique** em "Find Traces"
4. **Visualize** o fluxo das requisições

## Traces Automáticos

O Spring Boot com OpenTelemetry automaticamente rastreia:

- Requisições HTTP
- Operações de banco de dados
- Chamadas Redis
- Métricas JVM

## Traces Manuais

Para adicionar traces customizados, use o `Tracer`:

```java
@Autowired
private Tracer tracer;

Span span = tracer.spanBuilder("minha-operacao")
    .setAttribute("meu.parametro", "valor")
    .startSpan();

try (var scope = span.makeCurrent()) {
    // Sua lógica aqui
    span.addEvent("evento.importante");
} finally {
    span.end();
}
```

## Configuração

O Jaeger está configurado no `docker-compose.yml` com:

- **Collector OTLP habilitado**
- **UI acessível** na porta 16686
- **Collectors** nas portas 14268 (HTTP) e 14250 (gRPC)
