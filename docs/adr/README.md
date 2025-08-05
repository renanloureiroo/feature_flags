# Architecture Decision Records (ADRs)

Este diretório contém as decisões arquiteturais importantes do projeto Feature Flags.

## ADRs

### [001 - Feature Flag Types](./001-feature-flag-types.md)

**Status:** Accepted  
**Data:** 2025-08-05  
**Decisão:** Suportar quatro tipos principais de feature flags: BOOLEAN, NUMBER, STRING e LIST.

### [002 - Schema Validation](./002-schema-validation.md)

**Status:** Accepted  
**Data:** 2025-08-05  
**Decisão:** Implementar validação de JSON Schema usando `json-schema-validator` com schemas simples.

### [003 - Simplified Schema API](./003-simplified-schema-api.md)

**Status:** Proposed  
**Data:** 2025-01-27  
**Decisão:** Simplificar a API removendo a necessidade do usuário fornecer schemas JSON, gerando-os automaticamente a partir de constraints específicas.

## Status dos ADRs

- **Proposed**: Decisão proposta, ainda não implementada
- **Accepted**: Decisão aceita e implementada
- **Deprecated**: Decisão obsoleta, não mais válida
- **Superseded**: Decisão substituída por outra

## Como Criar um Novo ADR

1. Use o template: `template.md`
2. Numere sequencialmente: `004-nome-da-decisao.md`
3. Documente o contexto, decisão, consequências e alternativas
4. Atualize este README

## Exemplos de Schemas por Tipo

### BOOLEAN

```json
{
  "type": "boolean"
}
```

### NUMBER

```json
{
  "type": "number",
  "minimum": 1,
  "maximum": 10
}
```

### STRING

```json
{
  "type": "string",
  "enum": ["v1", "v2", "v3"]
}
```

### LIST

```json
{
  "type": "array",
  "items": {
    "type": "string"
  }
}
```
