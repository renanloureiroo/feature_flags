# Simplificação da API de Schemas

- Status: Proposed
- Data: 2025-01-27
- Decisores: Renan Loureiro
- Contexto
  - Problema/força motriz: A API atual requer que o usuário forneça schemas JSON complexos, mesmo para casos simples
  - Restrições: Deve manter compatibilidade com validação de schemas existente
  - Requisitos relevantes: Simplicidade de uso, flexibilidade, validação automática

## Decisão

Simplificar a API removendo a necessidade do usuário fornecer schemas JSON. Em vez disso, a API receberá propriedades específicas por tipo e gerará os schemas automaticamente.

### Nova Estrutura do DTO

```java
public class CreateFeatureFlagDTO {
    private String name;
    private String description;
    private FeatureFlagType type;

    // Propriedades específicas por tipo
    private NumberConstraints numberConstraints;
    private StringConstraints stringConstraints;
    private ListConstraints listConstraints;
}
```

### Constraints por Tipo

#### BOOLEAN

- **Uso**: Sem constraints adicionais
- **Schema gerado**: `{"type": "boolean"}`

#### NUMBER

```java
public class NumberConstraints {
    private Double minimum;
    private Double maximum;
    private Boolean exclusiveMinimum;
    private Boolean exclusiveMaximum;
}
```

**Exemplo:**

```json
{
  "name": "max-retries",
  "description": "Número máximo de tentativas",
  "type": "NUMBER",
  "numberConstraints": {
    "minimum": 1,
    "maximum": 10
  }
}
```

**Schema gerado:**

```json
{
  "type": "number",
  "minimum": 1,
  "maximum": 10
}
```

#### STRING

```java
public class StringConstraints {
    private List<String> enum;
    private String pattern;
    private Integer minLength;
    private Integer maxLength;
}
```

**Exemplo:**

```json
{
  "name": "api-version",
  "description": "Versão da API",
  "type": "STRING",
  "stringConstraints": {
    "enum": ["v1", "v2", "v3"]
  }
}
```

**Schema gerado:**

```json
{
  "type": "string",
  "enum": ["v1", "v2", "v3"]
}
```

#### LIST

```java
public class ListConstraints {
    private String itemType; // "string", "number", "boolean"
    private Integer minItems;
    private Integer maxItems;
    private Boolean uniqueItems;
}
```

**Exemplo:**

```json
{
  "name": "enabled-features",
  "description": "Lista de features habilitadas",
  "type": "LIST",
  "listConstraints": {
    "itemType": "string",
    "minItems": 1,
    "maxItems": 10
  }
}
```

**Schema gerado:**

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

### Implementação

#### 1. Novos DTOs

- `NumberConstraints.java`
- `StringConstraints.java`
- `ListConstraints.java`

#### 2. Schema Generator Service

```java
@Service
public class SchemaGeneratorService {
    public JsonNode generateSchema(FeatureFlagType type, Object constraints) {
        // Gera schema baseado no tipo e constraints
    }
}
```

#### 3. Modificação do UseCase

```java
public FeatureFlag execute(CreateFeatureFlagDTO dto) {
    // Valida constraints
    validateConstraints(dto);

    // Gera schema automaticamente
    JsonNode schema = schemaGeneratorService.generateSchema(dto.getType(), dto.getConstraints());

    // Valida schema gerado
    schemaValidationService.validateSchema(schema);

    // Cria feature flag
    // ...
}
```

### Exemplos de Uso Simplificados

#### BOOLEAN (sem constraints)

```json
{
  "name": "new-ui",
  "description": "Habilita a nova interface do usuário",
  "type": "BOOLEAN"
}
```

#### NUMBER (com constraints)

```json
{
  "name": "max-retries",
  "description": "Número máximo de tentativas",
  "type": "NUMBER",
  "numberConstraints": {
    "minimum": 1,
    "maximum": 10
  }
}
```

#### STRING (com enum)

```json
{
  "name": "api-version",
  "description": "Versão da API",
  "type": "STRING",
  "stringConstraints": {
    "enum": ["v1", "v2", "v3"]
  }
}
```

#### LIST (com constraints)

```json
{
  "name": "enabled-features",
  "description": "Lista de features habilitadas",
  "type": "LIST",
  "listConstraints": {
    "itemType": "string",
    "minItems": 1,
    "maxItems": 10
  }
}
```

## Consequências

### Positivas

- **Simplicidade**: Usuário não precisa conhecer JSON Schema
- **Validação automática**: Constraints são validadas na API
- **Flexibilidade**: Fácil adicionar novos tipos de constraints
- **Documentação clara**: Cada constraint tem propósito específico
- **Menos erros**: Validação na API previne schemas inválidos
- **Melhor UX**: API mais intuitiva e fácil de usar

### Negativas

- **Breaking change**: Requer mudança na API
- **Complexidade interna**: Lógica adicional para gerar schemas
- **Dependência**: Novo service para geração de schemas
- **Validação adicional**: Constraints precisam ser validadas

## Alternativas Consideradas

- **Manter schema atual**: Mantém complexidade para o usuário
- **Schemas predefinidos**: Limita flexibilidade
- **Híbrido**: Suportar ambos (muito complexo)

## Migração

### Estratégia de Migração

1. **Fase 1**: Implementar nova API mantendo a antiga
2. **Fase 2**: Deprecar API antiga com warning
3. **Fase 3**: Remover API antiga em versão futura

### Compatibilidade

- Manter endpoint atual como deprecated
- Adicionar novo endpoint `/v2/feature-flags`
- Documentar migração na API

## Links Relacionados

- ADR-001: Feature Flag Types
- ADR-002: Schema Validation
- Issues: Simplificação da API de schemas
- Documentos: JSON Schema specification
