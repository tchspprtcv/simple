# Citizen Service - Simple

## 📋 Visão Geral

O **Citizen Service** é responsável pelo gerenciamento de dados dos cidadãos na aplicação Simple. Ele mantém informações pessoais, endereços, documentos e histórico de interações dos cidadãos com os serviços municipais.

## 🏗️ Arquitetura

O serviço implementa:
- CRUD completo de dados de cidadãos
- Gerenciamento de endereços
- Controle de documentos
- Histórico de atividades
- Validação de dados pessoais
- Integração com outros microserviços

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança
- **PostgreSQL** - Banco de dados
- **Bean Validation** - Validação de dados
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

## 📡 Endpoints da API

### Gerenciamento de Cidadãos

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/citizens` | Listar todos os cidadãos |
| `GET` | `/citizens/{id}` | Buscar cidadão por ID |
| `GET` | `/citizens/search` | Buscar cidadãos por critérios |
| `POST` | `/citizens` | Criar novo cidadão |
| `PUT` | `/citizens/{id}` | Atualizar cidadão |
| `DELETE` | `/citizens/{id}` | Deletar cidadão |
| `GET` | `/citizens/{id}/profile` | Perfil completo do cidadão |

### Endereços

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/citizens/{id}/addresses` | Listar endereços do cidadão |
| `POST` | `/citizens/{id}/addresses` | Adicionar endereço |
| `PUT` | `/citizens/{id}/addresses/{addressId}` | Atualizar endereço |
| `DELETE` | `/citizens/{id}/addresses/{addressId}` | Remover endereço |
| `PUT` | `/citizens/{id}/addresses/{addressId}/primary` | Definir endereço principal |

### Documentos

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/citizens/{id}/documents` | Listar documentos do cidadão |
| `POST` | `/citizens/{id}/documents` | Adicionar documento |
| `PUT` | `/citizens/{id}/documents/{docId}` | Atualizar documento |
| `DELETE` | `/citizens/{id}/documents/{docId}` | Remover documento |
| `GET` | `/citizens/{id}/documents/{docId}/verify` | Verificar validade do documento |

### Histórico

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/citizens/{id}/history` | Histórico de atividades |
| `POST` | `/citizens/{id}/history` | Adicionar entrada no histórico |
| `GET` | `/citizens/{id}/interactions` | Interações com serviços |

## 🔧 Configuração

### Variáveis de Ambiente

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:9433/simple_citizens
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Service Integration
AUTH_SERVICE_URL=http://auth-service:8081
CONFIG_SERVICE_URL=http://config-service:8084
ORDER_SERVICE_URL=http://order-service:8083

# JWT Configuration
JWT_SECRET_KEY=your-secret-key

# Validation Configuration
CNI_VALIDATION_ENABLED=true
ADDRESS_VALIDATION_ENABLED=true
DOCUMENT_EXPIRY_CHECK=true
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
docker build -t simple-citizen-service .

# Executar container
docker run -p 9082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:9433/simple_citizens \
  -e JWT_SECRET_KEY=your-secret-key \
  simple-citizen-service
```

### Docker Compose

```bash
# Executar serviço específico
docker-compose up citizen-service

# Executar com dependências
docker-compose up citizen-service db auth-service
```

## 🗄️ Modelo de Dados

### Entidades Principais

#### Citizen
```java
@Entity
public class Citizen {
    private Long id;
    private String firstName;
    private String lastName;
    private String cpf;
    private String rg;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private String profession;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Address> addresses;
    private List<Document> documents;
}
```

#### Address
```java
@Entity
public class Address {
    private Long id;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private boolean isPrimary;
    private AddressType type;
    private Citizen citizen;
}
```

#### Document
```java
@Entity
public class Document {
    private Long id;
    private DocumentType type;
    private String number;
    private String issuingAuthority;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private boolean isValid;
    private String filePath;
    private Citizen citizen;
}
```

### Enums

```java
public enum DocumentType {
    CNI, BI, Carta de Condução, PASSPORT, WORK_CARD, VOTER_ID, BIRTH_CERTIFICATE
}

public enum AddressType {
    RESIDENTIAL, COMMERCIAL, CORRESPONDENCE
}

public enum Gender {
    MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY
}

public enum MaritalStatus {
    SINGLE, MARRIED, DIVORCED, WIDOWED, SEPARATED
}
```

## 📊 Monitoramento

### Endpoints do Actuator

- **Health Check**: `GET /actuator/health`
- **Métricas**: `GET /actuator/metrics`
- **Info**: `GET /actuator/info`
- **Database Health**: `GET /actuator/health/db`

### Métricas Customizadas

- Número de cidadãos cadastrados
- Documentos vencidos
- Endereços por região
- Atividade de cadastros por período

## 🔒 Segurança

### Autenticação
- Validação de tokens JWT
- Integração com Auth Service
- Controle de acesso baseado em roles

### Autorização
- **ADMIN**: Acesso total
- **ATTENDANT**: Leitura e escrita
- **CITIZEN**: Apenas seus próprios dados

### Validação de Dados

```java
@Valid
public class CitizenDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String firstName;
    
    @CNI(message = "CNI inválido")
    private String cpf;
    
    @Email(message = "Email inválido")
    private String email;
    
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate birthDate;
}
```

## 🧪 Testes

```bash
# Executar todos os testes
./mvnw test

# Testes de integração
./mvnw test -Dtest=*IntegrationTest

# Testes de validação
./mvnw test -Dtest=*ValidationTest

# Cobertura de código
./mvnw jacoco:report
```

### Testes de Exemplo

```bash
# Criar cidadão
curl -X POST http://localhost:9082/citizens \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "firstName": "João",
    "lastName": "Silva",
    "cpf": "12345678901",
    "email": "joao@example.com",
    "birthDate": "1990-01-01"
  }'

# Buscar cidadão
curl -X GET http://localhost:9082/citizens/1 \
  -H "Authorization: Bearer $JWT_TOKEN"
```

## 📝 Logs

### Eventos Logados

- Criação/atualização de cidadãos
- Validação de documentos
- Alterações de endereços
- Consultas realizadas
- Erros de validação

### Configuração de Logs

```yaml
logging:
  level:
    com.simple.citizen: DEBUG
    org.springframework.data.jpa: INFO
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

## 🔧 Desenvolvimento

### Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/simple/citizen/
│   │   ├── config/          # Configurações
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── exception/      # Tratamento de exceções
│   │   ├── repository/     # Repositórios JPA
│   │   ├── service/        # Serviços de negócio
│   │   ├── validation/     # Validadores customizados
│   │   └── util/           # Utilitários
│   └── resources/
│       ├── application.yml
│       └── db/migration/   # Scripts Flyway
└── test/
    └── java/              # Testes unitários e integração
```

### Adicionando Novas Funcionalidades

1. **Novo Campo**: Adicionar em entidade e DTO
2. **Nova Validação**: Implementar em `validation/`
3. **Novo Endpoint**: Criar controller e service
4. **Nova Consulta**: Adicionar em repository

## 🔍 Busca e Filtros

### Critérios de Busca

```java
// Busca por nome
GET /citizens/search?name=João

// Busca por CNI
GET /citizens/search?cpf=12345678901

// Busca por cidade
GET /citizens/search?city=São Paulo

// Busca combinada
GET /citizens/search?name=João&city=São Paulo&page=0&size=10
```

### Paginação

```java
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 100,
  "totalPages": 10
}
```

## 🚨 Troubleshooting

### Problemas Comuns

1. **CNI inválido**:
   ```bash
   # Verificar algoritmo de validação
   curl http://localhost:9082/citizens/validate-cpf/12345678901
   ```

2. **Endereço não encontrado**:
   ```bash
   # Verificar CEP
   curl http://localhost:9082/citizens/validate-address
   ```

3. **Documento vencido**:
   - Verificar data de validade
   - Atualizar documento

### Debug

```bash
# Habilitar logs de debug
export LOGGING_LEVEL_COM_SIMPLE_CITIZEN=DEBUG

# Verificar health
curl http://localhost:9082/actuator/health
```

## 📚 Documentação Adicional

- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Bean Validation](https://beanvalidation.org/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Implemente as mudanças
4. Adicione testes de validação
5. Envie um pull request

---

**Citizen Service** - Gerenciando dados dos cidadãos 👥