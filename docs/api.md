# API Reference

Documentação completa da API REST do sistema de Feature Flags.

## Base URL

```
http://localhost:8080
```

## 📚 Swagger UI

Acesse a documentação interativa da API:

```
http://localhost:8080/api/swagger-ui.html
```

## 🔐 Autenticação

Atualmente a API não requer autenticação. Em produção, recomenda-se implementar:

- **JWT Tokens**
- **API Keys**
- **OAuth 2.0**

## 📋 Endpoints

### Feature Flags

#### POST `/v1/feature-flags`

Cria uma nova feature flag.

**Request Body:**

```json
{
  "name": "new-user-interface",
  "description": "Habilita a nova interface do usuário",
  "type": "BOOLEAN",
  "schema": {
    "type": "boolean"
  }
}
```

**Response (201 Created):**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "new-user-interface",
  "slug": "new-user-interface",
  "description": "Habilita a nova interface do usuário",
  "type": "BOOLEAN",
  "schema": {
    "type": "boolean"
  },
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

**Exemplos por Tipo:**

#### BOOLEAN

```json
{
  "name": "dark-mode",
  "description": "Habilita o modo escuro",
  "type": "BOOLEAN",
  "schema": {
    "type": "boolean"
  }
}
```

#### NUMBER

```json
{
  "name": "max-retry-attempts",
  "description": "Número máximo de tentativas",
  "type": "NUMBER",
  "schema": {
    "type": "number",
    "minimum": 1,
    "maximum": 10
  }
}
```

#### STRING

```json
{
  "name": "api-version",
  "description": "Versão da API",
  "type": "STRING",
  "schema": {
    "type": "string",
    "enum": ["v1", "v2", "v3"]
  }
}
```

#### LIST

```json
{
  "name": "enabled-features",
  "description": "Lista de features habilitadas",
  "type": "LIST",
  "schema": {
    "type": "array",
    "items": {
      "type": "string"
    },
    "minItems": 1,
    "maxItems": 10
  }
}
```

## Schemas JSON

### Validação de Schemas

A API valida automaticamente os schemas JSON usando a especificação JSON Schema.

#### Schemas Válidos por Tipo

##### BOOLEAN

```json
{
  "type": "boolean"
}
```

##### NUMBER

```json
{
  "type": "number",
  "minimum": 1,
  "maximum": 100
}
```

```json
{
  "type": "number",
  "exclusiveMinimum": 0,
  "exclusiveMaximum": 1000
}
```

##### STRING

```json
{
  "type": "string",
  "minLength": 1,
  "maxLength": 255
}
```

```json
{
  "type": "string",
  "enum": ["development", "staging", "production"]
}
```

```json
{
  "type": "string",
  "pattern": "^[a-zA-Z0-9_-]+$"
}
```

##### LIST

```json
{
  "type": "array",
  "items": {
    "type": "string"
  },
  "minItems": 1,
  "maxItems": 10
}
```

```json
{
  "type": "array",
  "items": {
    "type": "number"
  },
  "uniqueItems": true
}
```

## Códigos de Status HTTP

| Código | Descrição             | Uso                                |
| ------ | --------------------- | ---------------------------------- |
| 200    | OK                    | Sucesso na operação                |
| 201    | Created               | Feature flag criada com sucesso    |
| 400    | Bad Request           | Dados inválidos ou schema inválido |
| 404    | Not Found             | Feature flag não encontrada        |
| 409    | Conflict              | Slug já existe                     |
| 422    | Unprocessable Entity  | Validação de negócio falhou        |
| 500    | Internal Server Error | Erro interno do servidor           |

## Tratamento de Erros

### Formato de Erro

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Schema JSON inválido",
  "path": "/v1/feature-flags",
  "details": {
    "field": "schema",
    "reason": "Schema deve ser do tipo 'boolean' para feature flags BOOLEAN"
  }
}
```

### Tipos de Erro

#### ValidationException

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Schema JSON inválido",
  "path": "/v1/feature-flags"
}
```

#### ResourceNotFoundException

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Feature flag não encontrada",
  "path": "/v1/feature-flags/123"
}
```

#### SlugAlreadyExists

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Slug 'new-user-interface' já existe",
  "path": "/v1/feature-flags"
}
```

## Headers

### Request Headers

| Header         | Tipo   | Obrigatório | Descrição          |
| -------------- | ------ | ----------- | ------------------ |
| `Content-Type` | String | Sim         | `application/json` |
| `Accept`       | String | Não         | `application/json` |

### Response Headers

| Header         | Descrição                   |
| -------------- | --------------------------- |
| `Content-Type` | `application/json`          |
| `Location`     | URL do recurso criado (201) |

## Exemplos de Uso

### cURL

#### Criar Feature Flag BOOLEAN

```bash
curl -X POST "http://localhost:8080/v1/feature-flags" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "dark-mode",
    "description": "Habilita o modo escuro",
    "type": "BOOLEAN",
    "schema": {
      "type": "boolean"
    }
  }'
```

#### Criar Feature Flag NUMBER

```bash
curl -X POST "http://localhost:8080/v1/feature-flags" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "max-retry-attempts",
    "description": "Número máximo de tentativas",
    "type": "NUMBER",
    "schema": {
      "type": "number",
      "minimum": 1,
      "maximum": 10
    }
  }'
```

### JavaScript

```javascript
// Criar feature flag
const response = await fetch("http://localhost:8080/v1/feature-flags", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    name: "dark-mode",
    description: "Habilita o modo escuro",
    type: "BOOLEAN",
    schema: {
      type: "boolean",
    },
  }),
});

const featureFlag = await response.json();
console.log(featureFlag);
```

### Python

```python
import requests
import json

# Criar feature flag
data = {
    "name": "dark-mode",
    "description": "Habilita o modo escuro",
    "type": "BOOLEAN",
    "schema": {
        "type": "boolean"
    }
}

response = requests.post(
    'http://localhost:8080/v1/feature-flags',
    headers={'Content-Type': 'application/json'},
    data=json.dumps(data)
)

feature_flag = response.json()
print(feature_flag)
```

## 🧪 Testes

### Script de Teste

Execute o script de teste para verificar todos os endpoints:

```bash
./test_feature_flags.sh
```

### Testes Automatizados

```bash
# Executar testes da API
mvn test -Dtest=FeatureFlagControllerTest

# Executar testes de integração
mvn test -Dtest=FeatureFlagIntegrationTest
```

## Monitoramento

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

### Métricas

```bash
curl http://localhost:8080/actuator/metrics
```

### Prometheus

```bash
curl http://localhost:8080/actuator/prometheus
```

## 🔮 Próximos Endpoints

### Planejados

- **GET** `/v1/feature-flags` - Listar todas as feature flags
- **GET** `/v1/feature-flags/{id}` - Buscar feature flag por ID
- **GET** `/v1/feature-flags/{slug}` - Buscar feature flag por slug
- **PUT** `/v1/feature-flags/{id}` - Atualizar feature flag
- **DELETE** `/v1/feature-flags/{id}` - Remover feature flag
- **POST** `/v1/feature-flags/{id}/values` - Criar valor para feature flag
- **GET** `/v1/feature-flags/{id}/values` - Listar valores de feature flag

### Em Desenvolvimento

- **POST** `/v1/feature-flags/import` - Importar feature flags
- **GET** `/v1/feature-flags/export` - Exportar feature flags
- **POST** `/v1/feature-flags/{id}/toggle` - Alternar feature flag
- **GET** `/v1/feature-flags/search` - Busca avançada

## 📚 Recursos Adicionais

- **[Swagger UI](http://localhost:8080/api/swagger-ui.html)** - Documentação interativa
- **[OpenAPI Spec](http://localhost:8080/api/v3/api-docs)** - Especificação OpenAPI
- **[Health Check](http://localhost:8080/actuator/health)** - Status da aplicação
- **[Métricas](http://localhost:8080/actuator/metrics)** - Métricas da aplicação
