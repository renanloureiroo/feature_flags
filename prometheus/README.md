# Prometheus Configuration

Esta pasta contém todas as configurações do Prometheus.

## Arquivos

- `prometheus.yml` - Configuração principal do Prometheus
- `alert_rules.yml` - Regras de alerta
- `README.md` - Esta documentação

## Configuração

O Prometheus está configurado para coletar métricas de:

1. **Aplicação Spring Boot** - Endpoint `/actuator/prometheus`
2. **PostgreSQL** - Métricas de banco de dados
3. **Redis** - Métricas de cache
4. **Prometheus** - Métricas do próprio Prometheus

## Alertas Configurados

- **HighErrorRate** - Taxa de erro > 5%
- **HighResponseTime** - Tempo de resposta > 2s
- **HighMemoryUsage** - Uso de memória > 80%
- **HighCPUUsage** - Uso de CPU > 80%
- **DatabaseConnectionPoolExhausted** - Pool de conexões > 80%

## Acesso

- **URL**: http://localhost:9090
- **Targets**: http://localhost:9090/targets
- **Alerts**: http://localhost:9090/alerts
