# Grafana Configuration

Esta pasta contém todas as configurações do Grafana.

## Estrutura

```
grafana/
├── provisioning/
│   ├── datasources/     # Configuração de datasources
│   └── dashboards/      # Configuração de dashboards
├── dashboards/          # Dashboards JSON
└── README.md           # Esta documentação
```

## Configuração Automática

### Datasources

- **Prometheus** - Configurado automaticamente
- **URL**: http://prometheus:9090

### Dashboards

- **Feature Flags Dashboard** - Dashboard principal com métricas da aplicação
- **Auto-loading** - Dashboards carregados automaticamente

## Dashboards Disponíveis

### Feature Flags Dashboard

- **Request Rate** - Taxa de requisições por segundo
- **95th Percentile Response Time** - Tempo de resposta (95º percentil)
- **Error Rate** - Taxa de erros (%)
- **Memory Usage** - Uso de memória
- **CPU Usage** - Uso de CPU (%)
- **Database Connections** - Conexões ativas com o banco

## Acesso

- **URL**: http://localhost:3000
- **Usuário**: admin
- **Senha**: admin

## Customização

Para adicionar novos dashboards:

1. Crie um arquivo JSON na pasta `dashboards/`
2. O Grafana carregará automaticamente
3. Acesse via interface web para editar
