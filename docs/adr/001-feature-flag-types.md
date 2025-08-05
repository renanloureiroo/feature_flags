# Tipos de Feature Flags Suportados

- Status: Accepted
- Data: 2025-08-05
- Decisores: Renan Loureiro
- Contexto
  - Problema/força motriz: Definir quais tipos de feature flags o sistema suportará
  - Restrições: Deve ser compatível com JSON Schema e casos de uso comuns
  - Requisitos relevantes: Flexibilidade, simplicidade e cobertura de casos de uso

## Decisão

Suportar quatro tipos principais de feature flags: `BOOLEAN`, `NUMBER`, `STRING` e `LIST`.

### Tipos Definidos

```java
public enum FeatureFlagType {
  BOOLEAN,  // true/false
  NUMBER,   // números (int/float)
  STRING,   // texto
  LIST      // arrays
}
```

### Casos de Uso por Tipo

#### BOOLEAN

- **Uso**: Habilitar/desabilitar features
- **Exemplo**: "Nova interface do usuário"
- **Schema**: `{"type": "boolean"}`
- **Valor**: `true` ou `false`

#### NUMBER

- **Uso**: Configurações numéricas
- **Exemplo**: "Número máximo de tentativas"
- **Schema**: `{"type": "number", "minimum": 1, "maximum": 10}`
- **Valor**: `5`

#### STRING

- **Uso**: Seleção de opções
- **Exemplo**: "Versão da API"
- **Schema**: `{"type": "string", "enum": ["v1", "v2", "v3"]}`
- **Valor**: `"v2"`

#### LIST

- **Uso**: Múltiplas seleções
- **Exemplo**: "Features habilitadas"
- **Schema**: `{"type": "array", "items": {"type": "string"}}`
- **Valor**: `["feature1", "feature2"]`

### Exemplos de Schemas por Tipo

#### BOOLEAN

```json
{
  "type": "boolean"
}
```

#### NUMBER

```json
{
  "type": "number",
  "minimum": 1,
  "maximum": 100
}
```

#### STRING

```json
{
  "type": "string",
  "enum": ["development", "staging", "production"]
}
```

#### LIST

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

## Consequências

### Positivas

- Cobre a maioria dos casos de uso
- Simples e intuitivo
- Fácil de entender e usar
- Compatível com JSON Schema
- Flexível para evolução

### Negativas

- Não suporta objetos complexos
- Pode ser limitante para casos avançados
- Requer extensão para novos tipos

## Alternativas Consideradas

- **Mais tipos (OBJECT, DATE, etc.)** – Complexidade desnecessária
- **Tipos dinâmicos** – Menos seguro e previsível
- **Apenas BOOLEAN** – Muito limitado

## Links Relacionados

- Issues/PRs: Definição dos tipos de feature flags
- Documentos: Feature flag patterns, JSON Schema types
