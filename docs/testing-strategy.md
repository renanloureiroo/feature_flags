# Estratégia de Testes

Este documento descreve a estratégia de testes implementada no projeto Feature Flags.

## Visão Geral

A estratégia de testes segue uma abordagem em camadas, garantindo isolamento e confiabilidade:

### **1. Testes Unitários (In-Memory)**

- **Repositórios em memória** para testes rápidos
- **Mocks** para dependências externas
- **Foco na lógica de negócio**
- **Execução rápida** (< 1 segundo por teste)

### **2. Testes de Integração (Banco Real)**

- **TestContainers** com PostgreSQL isolado
- **Schema único** por bloco de testes
- **Migrations automáticas** via Flyway
- **Cleanup automático** após cada teste

### **3. Testes E2E (Banco Real + API)**

- **Testes completos** de fluxos
- **MockMvc** para testes de API
- **Banco isolado** por teste
- **Validação de respostas** HTTP

## Estrutura de Arquivos

```
src/test/java/
├── com/renanloureiro/feature_flags/
│   ├── application/
│   │   └── usecases/
│   │       ├── CreateFeatureFlagUseCaseTest.java          # Unitário (In-Memory)
│   │       └── CreateFeatureFlagUseCaseIntegrationTest.java # Integração (Banco Real)
│   └── infrastructure/
│       ├── http/controllers/
│       │   └── FeatureFlagControllerE2ETest.java         # E2E (API + Banco)
│       ├── repositories/
│       │   ├── FeatureFlagRepositoryInMemoryImpl.java    # Repositório In-Memory
│       │   └── FeatureFlagValueRepositoryInMemoryImpl.java
│       ├── TestConfigUnit.java                           # Config Unitários
│       ├── TestConfigIntegration.java                    # Config Integração
│       └── IntegrationTestConfig.java                    # Config TestContainers
└── resources/
    └── init-test-db.sql                                 # Script Inicialização DB
```

## Como Executar

### **Testes Unitários (Rápidos)**

```bash
./mvnw test -Dtest="*Test" -Dexclude="*IntegrationTest,*E2ETest"
```

### **Testes de Integração**

```bash
./mvnw test -Dtest="*IntegrationTest"
```

### **Testes E2E**

```bash
./mvnw test -Dtest="*E2ETest"
```

### **Todos os Testes**

```bash
./mvnw test
```

## Configurações

### **Testes Unitários**

- **Repositórios**: In-Memory (HashMap)
- **Dependências**: Mocks
- **Velocidade**: Muito rápida
- **Isolamento**: Completo

### **Testes de Integração**

- **Banco**: PostgreSQL via TestContainers
- **Schema**: `feature_flags_integration_test`
- **Migrations**: Flyway automático
- **Cleanup**: `@DirtiesContext`

### **Testes E2E**

- **Banco**: PostgreSQL via TestContainers
- **Schema**: `feature_flags_e2e_test`
- **API**: MockMvc
- **Validação**: Respostas HTTP completas

## Benefícios

### **1. Isolamento Completo**

- Cada teste roda em seu próprio banco
- Schema isolado por bloco de testes
- Cleanup automático

### **2. Velocidade Otimizada**

- Testes unitários: < 1s
- Testes integração: ~5s
- Testes E2E: ~10s

### **3. Confiabilidade**

- Testes determinísticos
- Sem interferência entre testes
- Validação completa de fluxos

### **4. Manutenibilidade**

- Estrutura clara e organizada
- Fácil adição de novos testes
- Configurações centralizadas

## Adicionando Novos Testes

### **1. Teste Unitário**

```java
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfigUnit.class)
class MeuUseCaseTest {
    // Usa repositórios in-memory
}
```

### **2. Teste de Integração**

```java
@SpringBootTest(classes = FeatureFlagsApplication.class)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MeuUseCaseIntegrationTest {
    // Usa banco real via TestContainers
}
```

### **3. Teste E2E**

```java
@SpringBootTest(classes = FeatureFlagsApplication.class)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = IntegrationTestConfig.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class MeuControllerE2ETest {
    // Testa API completa com banco real
}
```

## Monitoramento

### **Cobertura de Testes**

```bash
./mvnw test jacoco:report
```

### **Relatórios**

- **Jacoco**: Cobertura de código
- **Surefire**: Relatórios de execução
- **TestContainers**: Logs de containers

## Troubleshooting

### **Problema**: TestContainers não inicia

**Solução**: Verificar se Docker está rodando

### **Problema**: Testes lentos

**Solução**: Verificar se está usando configuração correta

### **Problema**: Falhas intermitentes

**Solução**: Verificar `@DirtiesContext` e isolamento

## Métricas

- **Testes Unitários**: ~50ms cada
- **Testes Integração**: ~2s cada
- **Testes E2E**: ~5s cada
- **Cobertura Total**: >90%
- **Isolamento**: 100%
