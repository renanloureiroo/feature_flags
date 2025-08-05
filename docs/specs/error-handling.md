# Sistema de Tratamento de Erros

Este documento descreve o sistema padronizado de tratamento de erros implementado na aplicação.

## Estrutura de Resposta de Erro

Todas as respostas de erro seguem o mesmo formato:

```json
{
  "message": "Descrição do erro",
  "timestamp": "2024-01-15T10:30:45.123",
  "status": 400
}
```

### Campos

- **message**: Descrição clara do erro
- **timestamp**: Data e hora do erro (formato ISO 8601)
- **status**: Código HTTP do erro

## Exceções Customizadas

### BaseException

Classe base para todas as exceções customizadas da aplicação.

```java
public abstract class BaseException extends RuntimeException {
  private final LocalDateTime timestamp;
  private final int status;
}
```

### Exceções Disponíveis

#### SlugAlreadyExists (409)

Lançada quando uma feature flag com o mesmo slug já existe.

```java
throw new SlugAlreadyExists();
```

#### ValidationException (400)

Lançada para erros de validação de negócio.

```java
throw new ValidationException("Nome deve ter pelo menos 3 caracteres");
```

#### ResourceNotFoundException (404)

Lançada quando um recurso não é encontrado.

```java
throw new ResourceNotFoundException("Feature flag não encontrada");
```

## Handler Global

O `GlobalExceptionHandler` captura automaticamente:

1. **Exceções Customizadas**: Todas as exceções que estendem `BaseException`
2. **Erros de Validação**: `MethodArgumentNotValidException` (validações do Bean Validation)
3. **Erros Genéricos**: Qualquer exceção não tratada

### Logs

Todos os erros são logados automaticamente com:

- Nível ERROR
- Mensagem descritiva
- Stack trace completo

## Como Usar

### 1. Validações de DTO

```java
@Data
public class CreateFeatureFlagDTO {
  @NotBlank(message = "Name is required")
  private String name;

  @NotNull(message = "Type is required")
  private FeatureFlagType type;
}
```

### 2. Validações de Negócio

```java
public FeatureFlag execute(CreateFeatureFlagDTO dto) {
  if (dto.getName().length() < 3) {
    throw new ValidationException("Nome deve ter pelo menos 3 caracteres");
  }

  if (featureFlagRepository.existsBySlug(slug)) {
    throw new SlugAlreadyExists();
  }

  // ... resto da lógica
}
```

### 3. Recursos Não Encontrados

```java
public FeatureFlag findById(String id) {
  return featureFlagRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Feature flag não encontrada"));
}
```

## Exemplos de Resposta

### Erro de Validação (400)

```json
{
  "message": "name: Name is required",
  "timestamp": "2024-01-15T10:30:45.123",
  "status": 400
}
```

### Slug Já Existe (409)

```json
{
  "message": "Already exists a feature flag with this slug, please try another name",
  "timestamp": "2024-01-15T10:30:45.123",
  "status": 409
}
```

### Erro Interno (500)

```json
{
  "message": "Erro interno do servidor",
  "timestamp": "2024-01-15T10:30:45.123",
  "status": 500
}
```

## Criando Novas Exceções

Para criar uma nova exceção customizada:

1. Estenda `BaseException`
2. Defina o status HTTP apropriado
3. Adicione um handler específico no `GlobalExceptionHandler` se necessário

```java
public class CustomBusinessException extends BaseException {
  public CustomBusinessException(String message) {
    super(message, 422); // Unprocessable Entity
  }
}
```
