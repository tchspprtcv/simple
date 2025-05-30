# Config Service - Simple

## 📋 Visão Geral

O **Config Service** é responsável pelo gerenciamento centralizado de configurações da aplicação Simple. Ele mantém parâmetros do sistema, configurações de negócio, templates de documentos e outras configurações que podem ser alteradas sem necessidade de redeploy.

## 🏗️ Arquitetura

O serviço implementa:
- Gerenciamento centralizado de configurações
- Versionamento de configurações
- Cache de configurações para performance
- API para consulta e atualização
- Validação de configurações
- Auditoria de mudanças

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança
- **Spring Cache** - Cache de configurações
- **PostgreSQL** - Banco de dados
- **Bean Validation** - Validação de dados
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

## 📡 Endpoints da API

### Configurações Gerais

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/config` | Listar todas as configurações |
| `GET` | `/config/{key}` | Buscar configuração por chave |
| `GET` | `/config/category/{category}` | Configurações por categoria |
| `POST` | `/config` | Criar nova configuração |
| `PUT` | `/config/{key}` | Atualizar configuração |
| `DELETE` | `/config/{key}` | Deletar configuração |

### Configurações do Sistema

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/config/system/email` | Configurações de email |
| `GET` | `/config/system/notification` | Configurações de notificação |
| `GET` | `/config/system/workflow` | Configurações de workflow |
| `GET` | `/config/system/security` | Configurações de segurança |
| `PUT` | `/config/system/{type}` | Atualizar configurações do sistema |

### Templates

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/config/templates` | Listar templates |
| `GET` | `/config/templates/{id}` | Buscar template por ID |
| `POST` | `/config/templates` | Criar novo template |
| `PUT` | `/config/templates/{id}` | Atualizar template |
| `DELETE` | `/config/templates/{id}` | Deletar template |

### Parâmetros de Negócio

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/config/business/fees` | Taxas e valores |
| `GET` | `/config/business/deadlines` | Prazos de processos |
| `GET` | `/config/business/requirements` | Requisitos por tipo de serviço |
| `PUT` | `/config/business/{type}` | Atualizar parâmetros |

### Auditoria

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/config/audit` | Histórico de mudanças |
| `GET` | `/config/audit/{key}` | Histórico de uma configuração |
| `GET` | `/config/versions/{key}` | Versões de uma configuração |

## 🔧 Configuração

### Variáveis de Ambiente

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:9433/simple_config
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Cache Configuration
SPRING_CACHE_TYPE=caffeine
CACHE_EXPIRY_MINUTES=60
CACHE_MAX_SIZE=1000

# JWT Configuration
JWT_SECRET_KEY=your-secret-key

# Default Configurations
DEFAULT_EMAIL_SENDER=noreply@simple.gov
DEFAULT_NOTIFICATION_ENABLED=true
DEFAULT_MAX_FILE_SIZE=10MB
DEFAULT_SESSION_TIMEOUT=30
```

### Configuração do Banco de Dados

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=1000,expireAfterWrite=60m
```

## 🚀 Execução

### Desenvolvimento Local

```bash
# Compilar e executar
./mvnw spring-boot:run

# Ou usando Maven
mvn spring-boot:run

# Com perfil específico
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Docker

```bash
# Construir imagem
docker build -t simple-config-service .

# Executar container
docker run -p 9084:8084 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:9433/simple_config \
  -e JWT_SECRET_KEY=your-secret-key \
  simple-config-service
```

### Docker Compose

```bash
# Executar serviço específico
docker-compose up config-service

# Executar com dependências
docker-compose up config-service db
```

## 🗄️ Modelo de Dados

### Entidades Principais

#### Configuration
```java
@Entity
public class Configuration {
    private Long id;
    private String key;
    private String value;
    private String description;
    private ConfigType type;
    private String category;
    private boolean isActive;
    private boolean isEncrypted;
    private String defaultValue;
    private String validationRule;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer version;
}
```

#### ConfigurationHistory
```java
@Entity
public class ConfigurationHistory {
    private Long id;
    private String configKey;
    private String oldValue;
    private String newValue;
    private String changedBy;
    private LocalDateTime changedAt;
    private String reason;
    private Integer version;
}
```

#### Template
```java
@Entity
public class Template {
    private Long id;
    private String name;
    private String description;
    private TemplateType type;
    private String content;
    private String variables;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### Enums

```java
public enum ConfigType {
    STRING, INTEGER, BOOLEAN, DECIMAL, JSON, EMAIL, URL, PASSWORD
}

public enum TemplateType {
    EMAIL, DOCUMENT, NOTIFICATION, REPORT
}

public enum ConfigCategory {
    SYSTEM, BUSINESS, SECURITY, NOTIFICATION, WORKFLOW, UI
}
```

## 📊 Configurações Padrão

### Configurações do Sistema

```yaml
# Email Settings
email.smtp.host: smtp.gmail.com
email.smtp.port: 587
email.smtp.username: ${EMAIL_USERNAME}
email.smtp.password: ${EMAIL_PASSWORD}
email.from.address: noreply@simple.gov
email.from.name: Sistema Simple

# Notification Settings
notification.enabled: true
notification.email.enabled: true
notification.sms.enabled: false
notification.push.enabled: true

# File Upload Settings
file.upload.max.size: 10MB
file.upload.allowed.types: pdf,jpg,jpeg,png,doc,docx
file.storage.path: /app/uploads

# Security Settings
security.session.timeout: 30
security.password.min.length: 8
security.max.login.attempts: 5
security.account.lockout.duration: 300

# Workflow Settings
workflow.auto.approval.enabled: false
workflow.max.processing.days: 30
workflow.reminder.days: 7
```

### Configurações de Negócio

```yaml
# Service Fees
service.fee.building.permit: 150.00
service.fee.business.license: 200.00
service.fee.property.registration: 100.00

# Processing Deadlines
deadline.building.permit: 15
deadline.business.license: 10
deadline.property.registration: 20

# Document Requirements
requirements.building.permit: ["identity", "property_deed", "building_plan"]
requirements.business.license: ["identity", "proof_of_residence", "income_proof"]
```

## 📊 Monitoramento

### Endpoints do Actuator

- **Health Check**: `GET /actuator/health`
- **Métricas**: `GET /actuator/metrics`
- **Info**: `GET /actuator/info`
- **Cache Stats**: `GET /actuator/caches`

### Métricas Customizadas

- Configurações por categoria
- Frequência de acesso
- Cache hit/miss ratio
- Mudanças por período

## 🔒 Segurança

### Autenticação
- Validação de tokens JWT
- Controle de acesso baseado em roles

### Autorização

| Role | Permissões |
|------|------------|
| **ADMIN** | Acesso total, criar/editar/deletar |
| **ATTENDANT** | Leitura de configurações |
| **SYSTEM** | Acesso programático |

### Configurações Sensíveis

```java
// Configurações criptografadas
@Encrypted
private String emailPassword;

@Encrypted
private String databasePassword;

@Encrypted
private String apiKey;
```

## 🧪 Testes

```bash
# Executar todos os testes
./mvnw test

# Testes de integração
./mvnw test -Dtest=*IntegrationTest

# Testes de cache
./mvnw test -Dtest=*CacheTest

# Cobertura de código
./mvnw jacoco:report
```

### Testes de Exemplo

```bash
# Buscar configuração
curl -X GET http://localhost:9084/config/email.smtp.host \
  -H "Authorization: Bearer $JWT_TOKEN"

# Atualizar configuração
curl -X PUT http://localhost:9084/config/notification.enabled \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{"value": "true", "reason": "Habilitar notificações"}'
```

## 📝 Logs

### Eventos Logados

- Criação/atualização de configurações
- Acesso a configurações
- Mudanças de cache
- Erros de validação
- Operações administrativas

### Configuração de Logs

```yaml
logging:
  level:
    com.simple.config: DEBUG
    org.springframework.cache: INFO
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

## 🔧 Desenvolvimento

### Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/simple/config/
│   │   ├── config/          # Configurações Spring
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── exception/      # Tratamento de exceções
│   │   ├── repository/     # Repositórios JPA
│   │   ├── service/        # Serviços de negócio
│   │   ├── cache/          # Configurações de cache
│   │   ├── validation/     # Validadores customizados
│   │   └── util/           # Utilitários
│   └── resources/
│       ├── application.yml
│       ├── default-configs.yml
│       └── db/migration/   # Scripts Flyway
└── test/
    └── java/              # Testes unitários e integração
```

### Adicionando Nova Configuração

1. **Definir Configuração**: Adicionar em `default-configs.yml`
2. **Criar Validação**: Implementar regra de validação
3. **Atualizar Cache**: Configurar estratégia de cache
4. **Documentar**: Adicionar descrição e exemplos
5. **Testar**: Criar testes unitários

## 💾 Cache

### Estratégias de Cache

```java
@Cacheable(value = "configurations", key = "#key")
public Configuration getByKey(String key) {
    return repository.findByKey(key);
}

@CacheEvict(value = "configurations", key = "#key")
public void updateConfiguration(String key, String value) {
    // Atualizar configuração
}

@CacheEvict(value = "configurations", allEntries = true)
public void clearCache() {
    // Limpar todo o cache
}
```

### Configuração do Cache

```yaml
spring:
  cache:
    caffeine:
      spec: |
        configurations=maximumSize=500,expireAfterWrite=60m
        templates=maximumSize=100,expireAfterWrite=120m
        business-rules=maximumSize=200,expireAfterWrite=30m
```

## 🔍 Busca e Filtros

### Critérios de Busca

```java
// Busca por categoria
GET /config/category/SYSTEM

// Busca por tipo
GET /config?type=BOOLEAN

// Busca por padrão
GET /config/search?pattern=email

// Configurações ativas
GET /config?active=true
```

## 🚨 Troubleshooting

### Problemas Comuns

1. **Cache não atualiza**:
   ```bash
   # Limpar cache
   curl -X POST http://localhost:9084/config/cache/clear
   ```

2. **Configuração não encontrada**:
   ```bash
   # Verificar se existe
   curl http://localhost:9084/config/your-key
   ```

3. **Validação falha**:
   - Verificar regras de validação
   - Confirmar tipo de dados

### Debug

```bash
# Habilitar logs de debug
export LOGGING_LEVEL_COM_SIMPLE_CONFIG=DEBUG

# Verificar cache
curl http://localhost:9084/actuator/caches
```

## 📚 Documentação Adicional

- [Spring Cache](https://spring.io/guides/gs/caching/)
- [Caffeine Cache](https://github.com/ben-manes/caffeine)
- [Configuration Management Best Practices](https://12factor.net/config)

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Implemente as mudanças
4. Adicione testes de configuração
5. Envie um pull request

---

**Config Service** - Centralizando configurações do sistema ⚙️