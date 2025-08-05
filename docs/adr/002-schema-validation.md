# Validação de Schemas para Feature Flags

- Status: Accepted
- Data: 2025-08-05
- Decisores: Renan Loureiro
- Contexto
  - Problema/força motriz: Necessidade de validar que os schemas de feature flags são válidos e compatíveis com seus tipos
  - Restrições: Deve ser compatível com Spring Boot e JPA
  - Requisitos relevantes: Validação automática de schemas, compatibilidade com tipos de feature flags

## Decisão

Implementar validação de JSON Schema usando a biblioteca `json-schema-validator` com um service dedicado `JsonSchemaValidationService` no pacote `application.validation`.

### Estrutura de Schemas Simples

#### BOOLEAN

```json
{
  "type": "boolean"
}
```

**Valor esperado:** `true` ou `false`

#### NUMBER

```json
{
  "type": "number",
  "minimum": 1,
  "maximum": 10
}
```

**Valor esperado:** `5`

#### STRING

```json
{
  "type": "string",
  "enum": ["v1", "v2", "v3"]
}
```

**Valor esperado:** `"v2"`

#### LIST

```json
{
  "type": "array",
  "items": {
    "type": "string"
  }
}
```

**Valor esperado:** `["feature1", "feature2"]`

## Consequências

### Positivas

- Validação automática de schemas JSON
- Prevenção de erros em runtime
- Schemas simples e intuitivos
- Compatibilidade com tipos de feature flags
- Facilita testes e documentação

### Negativas

- Dependência adicional (json-schema-validator)
- Complexidade adicional na camada de validação
- Possível overhead de performance

## Alternativas Consideradas

- **Bean Validation com annotations customizadas** – Limitado para schemas dinâmicos
- **Validação manual no UseCase** – Menos reutilizável e testável
- **Validação no Controller** – Viola separação de responsabilidades

## Links Relacionados

- Issues/PRs: Implementação de validação de schema
- Documentos: JSON Schema specification
