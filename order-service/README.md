# Order Service - Simple

## 📋 Visão Geral

O **Order Service** é o núcleo da aplicação Simple, responsável pelo gerenciamento de pedidos e processos municipais. Ele controla todo o ciclo de vida dos pedidos, desde a criação até a finalização, incluindo aprovações, documentação e acompanhamento.

## 🏗️ Arquitetura

O serviço implementa:
- Gerenciamento completo de pedidos
- Workflow de aprovação
- Controle de status e transições
- Gestão de documentos anexos
- Histórico de atividades
- Notificações automáticas
- Integração com outros microserviços

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança
- **Spring State Machine** - Gerenciamento de estados
- **PostgreSQL** - Banco de dados
- **Bean Validation** - Validação de dados
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

## 📡 Endpoints da API

### Gerenciamento de Pedidos

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/orders` | Listar todos os pedidos |
| `GET` | `/orders/{id}` | Buscar pedido por ID |
| `GET` | `/orders/search` | Buscar pedidos por critérios |
| `POST` | `/orders` | Criar novo pedido |
| `PUT` | `/orders/{id}` | Atualizar pedido |
| `DELETE` | `/orders/{id}` | Cancelar pedido |
| `GET` | `/orders/citizen/{citizenId}` | Pedidos de um cidadão |

### Controle de Status

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `PUT` | `/orders/{id}/status` | Atualizar status do pedido |
| `POST` | `/orders/{id}/approve` | Aprovar pedido |
| `POST` | `/orders/{id}/reject` | Rejeitar pedido |
| `POST` | `/orders/{id}/complete` | Finalizar pedido |
| `GET` | `/orders/{id}/status-history` | Histórico de status |

### Documentos

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/orders/{id}/documents` | Listar documentos do pedido |
| `POST` | `/orders/{id}/documents` | Anexar documento |
| `DELETE` | `/orders/{id}/documents/{docId}` | Remover documento |
| `GET` | `/orders/{id}/documents/{docId}/download` | Download de documento |
| `PUT` | `/orders/{id}/documents/{docId}/validate` | Validar documento |

### Tipos de Serviços

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/orders/service-types` | Listar tipos de serviços |
| `GET` | `/orders/service-types/{id}` | Detalhes do tipo de serviço |
| `GET` | `/orders/service-types/{id}/requirements` | Requisitos do serviço |

### Relatórios

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/orders/reports/summary` | Resumo de pedidos |
| `GET` | `/orders/reports/by-status` | Relatório por status |
| `GET` | `/orders/reports/by-type` | Relatório por tipo |
| `GET` | `/orders/reports/performance` | Relatório de performance |

## 🔧 Configuração

### Variáveis de Ambiente

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:9433/simple_orders
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Service Integration
AUTH_SERVICE_URL=http://auth-service:8081
CITIZEN_SERVICE_URL=http://citizen-service:8082
CONFIG_SERVICE_URL=http://config-service:8084
FAVORITES_SERVICE_URL=http://favorites-service:8085

# JWT Configuration
JWT_SECRET_KEY=your-secret-key

# Workflow Configuration
AUTO_APPROVAL_ENABLED=false
MAX_PROCESSING_DAYS=30
NOTIFICATION_ENABLED=true

# File Upload Configuration
FILE_UPLOAD_MAX_SIZE=10MB
FILE_UPLOAD_ALLOWED_TYPES=pdf,jpg,jpeg,png,doc,docx
FILE_STORAGE_PATH=/app/uploads
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
docker build -t simple-order-service .

# Executar container
docker run -p 9083:8083 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:9433/simple_orders \
  -e JWT_SECRET_KEY=your-secret-key \
  simple-order-service
```

### Docker Compose

```bash
# Executar serviço específico
docker-compose up order-service

# Executar com dependências
docker-compose up order-service db auth-service citizen-service
```

## 🗄️ Modelo de Dados

### Entidades Principais

#### Order
```java
@Entity
public class Order {
    private Long id;
    private String orderNumber;
    private Long citizenId;
    private Long serviceTypeId;
    private OrderStatus status;
    private Priority priority;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
    private Long assignedTo;
    private List<OrderDocument> documents;
    private List<OrderStatusHistory> statusHistory;
    private List<OrderComment> comments;
}
```

#### ServiceType
```java
@Entity
public class ServiceType {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer estimatedDays;
    private BigDecimal fee;
    private boolean isActive;
    private List<ServiceRequirement> requirements;
}
```

#### OrderDocument
```java
@Entity
public class OrderDocument {
    private Long id;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String mimeType;
    private Long fileSize;
    private DocumentType type;
    private boolean isRequired;
    private boolean isValidated;
    private LocalDateTime uploadedAt;
    private Order order;
}
```

#### OrderStatusHistory
```java
@Entity
public class OrderStatusHistory {
    private Long id;
    private OrderStatus fromStatus;
    private OrderStatus toStatus;
    private String reason;
    private String comments;
    private LocalDateTime changedAt;
    private Long changedBy;
    private Order order;
}
```

### Enums

```java
public enum OrderStatus {
    DRAFT,           // Rascunho
    SUBMITTED,       // Submetido
    UNDER_REVIEW,    // Em análise
    PENDING_DOCS,    // Aguardando documentos
    APPROVED,        // Aprovado
    REJECTED,        // Rejeitado
    IN_PROGRESS,     // Em andamento
    COMPLETED,       // Concluído
    CANCELLED        // Cancelado
}

public enum Priority {
    LOW, NORMAL, HIGH, URGENT
}

public enum DocumentType {
    IDENTITY, PROOF_OF_RESIDENCE, INCOME_PROOF, 
    PROPERTY_DEED, BUILDING_PLAN, OTHER
}
```

## 📊 Workflow de Estados

### Máquina de Estados

```
DRAFT → SUBMITTED → UNDER_REVIEW → APPROVED → IN_PROGRESS → COMPLETED
                         ↓
                    PENDING_DOCS → UNDER_REVIEW
                         ↓
                     REJECTED
```

### Transições Permitidas

| De | Para | Quem Pode |
|----|------|----------|
| DRAFT | SUBMITTED | Cidadão |
| SUBMITTED | UNDER_REVIEW | Sistema |
| UNDER_REVIEW | APPROVED | Atendente |
| UNDER_REVIEW | REJECTED | Atendente |
| UNDER_REVIEW | PENDING_DOCS | Atendente |
| PENDING_DOCS | UNDER_REVIEW | Sistema |
| APPROVED | IN_PROGRESS | Atendente |
| IN_PROGRESS | COMPLETED | Atendente |
| * | CANCELLED | Cidadão/Atendente |

## 📊 Monitoramento

### Endpoints do Actuator

- **Health Check**: `GET /actuator/health`
- **Métricas**: `GET /actuator/metrics`
- **Info**: `GET /actuator/info`
- **Database Health**: `GET /actuator/health/db`

### Métricas Customizadas

- Pedidos por status
- Tempo médio de processamento
- Taxa de aprovação
- Documentos pendentes
- Performance por tipo de serviço

## 🔒 Segurança

### Autenticação
- Validação de tokens JWT
- Integração com Auth Service
- Controle de acesso baseado em roles

### Autorização

| Role | Permissões |
|------|------------|
| **ADMIN** | Acesso total, relatórios |
| **ATTENDANT** | Gerenciar pedidos, aprovar/rejeitar |
| **CITIZEN** | Criar pedidos, visualizar próprios pedidos |

### Validação de Dados

```java
@Valid
public class OrderDTO {
    @NotNull(message = "Tipo de serviço é obrigatório")
    private Long serviceTypeId;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 1000, message = "Descrição muito longa")
    private String description;
    
    @Future(message = "Data deve ser futura")
    private LocalDateTime dueDate;
}
```

## 🧪 Testes

```bash
# Executar todos os testes
./mvnw test

# Testes de integração
./mvnw test -Dtest=*IntegrationTest

# Testes de workflow
./mvnw test -Dtest=*WorkflowTest

# Cobertura de código
./mvnw jacoco:report
```

### Testes de Exemplo

```bash
# Criar pedido
curl -X POST http://localhost:9083/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "serviceTypeId": 1,
    "description": "Solicitação de alvará",
    "priority": "NORMAL"
  }'

# Aprovar pedido
curl -X POST http://localhost:9083/orders/1/approve \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{"comments": "Documentação completa"}'
```

## 📝 Logs

### Eventos Logados

- Criação de pedidos
- Mudanças de status
- Upload de documentos
- Aprovações/rejeições
- Erros de validação
- Integrações com outros serviços

### Configuração de Logs

```yaml
logging:
  level:
    com.simple.order: DEBUG
    org.springframework.statemachine: INFO
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

## 🔧 Desenvolvimento

### Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/simple/order/
│   │   ├── config/          # Configurações
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── exception/      # Tratamento de exceções
│   │   ├── repository/     # Repositórios JPA
│   │   ├── service/        # Serviços de negócio
│   │   ├── statemachine/   # Configuração de estados
│   │   ├── validation/     # Validadores customizados
│   │   └── util/           # Utilitários
│   └── resources/
│       ├── application.yml
│       └── db/migration/   # Scripts Flyway
└── test/
    └── java/              # Testes unitários e integração
```

### Adicionando Novo Tipo de Serviço

1. **Criar ServiceType**: Adicionar no banco
2. **Definir Requisitos**: Configurar documentos necessários
3. **Configurar Workflow**: Definir estados específicos
4. **Implementar Validações**: Regras de negócio
5. **Adicionar Testes**: Cobertura completa

## 🔍 Busca e Filtros

### Critérios de Busca

```java
// Busca por status
GET /orders/search?status=UNDER_REVIEW

// Busca por cidadão
GET /orders/search?citizenId=123

// Busca por tipo de serviço
GET /orders/search?serviceTypeId=1

// Busca por período
GET /orders/search?startDate=2024-01-01&endDate=2024-12-31

// Busca combinada com paginação
GET /orders/search?status=APPROVED&page=0&size=10&sort=createdAt,desc
```

## 🚨 Troubleshooting

### Problemas Comuns

1. **Transição de estado inválida**:
   ```bash
   # Verificar estado atual
   curl http://localhost:9083/orders/1/status
   ```

2. **Documento não encontrado**:
   ```bash
   # Verificar uploads
   ls -la /app/uploads/
   ```

3. **Integração com outros serviços**:
   ```bash
   # Testar conectividade
   curl http://citizen-service:8082/actuator/health
   ```

### Debug

```bash
# Habilitar logs de debug
export LOGGING_LEVEL_COM_SIMPLE_ORDER=DEBUG

# Verificar health
curl http://localhost:9083/actuator/health
```

## 📚 Documentação Adicional

- [Spring State Machine](https://spring.io/projects/spring-statemachine)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [File Upload Best Practices](https://spring.io/guides/gs/uploading-files/)

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Implemente as mudanças
4. Adicione testes de workflow
5. Envie um pull request

---

**Order Service** - Gerenciando o ciclo de vida dos pedidos 📋