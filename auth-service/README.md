# Auth Service - Simple

## 📋 Visão Geral

O **Auth Service** é responsável pela autenticação e autorização de usuários na aplicação Simple. Ele gerencia o registro, login, validação de tokens JWT e controle de acesso baseado em roles.

## 🏗️ Arquitetura

O serviço implementa:
- Autenticação baseada em JWT
- Gerenciamento de usuários e roles
- Criptografia de senhas com BCrypt
- Validação de tokens
- Controle de sessões

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Security** - Segurança e autenticação
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **JWT (jjwt 0.12.3)** - Tokens de autenticação
- **BCrypt** - Criptografia de senhas
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

## 📡 Endpoints da API

### Autenticação

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `POST` | `/auth/register` | Registro de novo usuário |
| `POST` | `/auth/login` | Login de usuário |
| `POST` | `/auth/logout` | Logout de usuário |
| `POST` | `/auth/refresh` | Renovação de token |
| `GET` | `/auth/validate` | Validação de token |

### Gerenciamento de Usuários

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/auth/users` | Listar usuários |
| `GET` | `/auth/users/{id}` | Buscar usuário por ID |
| `PUT` | `/auth/users/{id}` | Atualizar usuário |
| `DELETE` | `/auth/users/{id}` | Deletar usuário |
| `PUT` | `/auth/users/{id}/password` | Alterar senha |

### Roles e Permissões

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/auth/roles` | Listar roles |
| `POST` | `/auth/users/{id}/roles` | Atribuir role a usuário |
| `DELETE` | `/auth/users/{id}/roles/{roleId}` | Remover role de usuário |

## 🔧 Configuração

### Variáveis de Ambiente

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:9433/simple_auth
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# JWT Configuration
JWT_SECRET_KEY=your-super-secret-key-here
JWT_EXPIRATION_TIME=86400000  # 24 hours in milliseconds
JWT_REFRESH_EXPIRATION_TIME=604800000  # 7 days in milliseconds

# Security Configuration
PASSWORD_MIN_LENGTH=8
MAX_LOGIN_ATTEMPTS=5
ACCOUNT_LOCKOUT_DURATION=300000  # 5 minutes in milliseconds
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
docker build -t simple-auth-service .

# Executar container
docker run -p 9081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:9433/simple_auth \
  -e JWT_SECRET_KEY=your-secret-key \
  simple-auth-service
```

### Docker Compose

```bash
# Executar serviço específico
docker-compose up auth-service

# Executar com dependências
docker-compose up auth-service db
```

## 🔒 Segurança

### Autenticação JWT

1. **Login**: Usuário envia credenciais
2. **Validação**: Serviço valida credenciais
3. **Token**: Gera JWT com claims do usuário
4. **Resposta**: Retorna token de acesso e refresh token

### Estrutura do Token JWT

```json
{
  "sub": "user@example.com",
  "roles": ["CITIZEN", "USER"],
  "userId": 123,
  "iat": 1640995200,
  "exp": 1641081600
}
```

### Roles Disponíveis

- **ADMIN**: Acesso total ao sistema
- **ATTENDANT**: Funcionário municipal
- **CITIZEN**: Cidadão comum
- **USER**: Usuário básico

### Criptografia de Senhas

```java
// Configuração BCrypt
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
}
```

## 📊 Monitoramento

### Endpoints do Actuator

- **Health Check**: `GET /actuator/health`
- **Métricas**: `GET /actuator/metrics`
- **Info**: `GET /actuator/info`
- **Database Health**: `GET /actuator/health/db`

### Métricas Customizadas

- Número de logins por hora
- Tentativas de login falhadas
- Tokens ativos
- Usuários registrados

## 🗄️ Modelo de Dados

### Entidades Principais

#### User
```java
@Entity
public class User {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private LocalDateTime createdAt;
    private Set<Role> roles;
}
```

#### Role
```java
@Entity
public class Role {
    private Long id;
    private String name;
    private String description;
    private Set<Permission> permissions;
}
```

#### Permission
```java
@Entity
public class Permission {
    private Long id;
    private String name;
    private String resource;
    private String action;
}
```

## 🧪 Testes

```bash
# Executar todos os testes
./mvnw test

# Testes de integração
./mvnw test -Dtest=*IntegrationTest

# Testes de segurança
./mvnw test -Dtest=*SecurityTest

# Cobertura de código
./mvnw jacoco:report
```

### Testes de Exemplo

```bash
# Teste de registro
curl -X POST http://localhost:9081/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "João",
    "lastName": "Silva"
  }'

# Teste de login
curl -X POST http://localhost:9081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

## 📝 Logs

### Eventos Logados

- Tentativas de login (sucesso/falha)
- Registro de novos usuários
- Alterações de senha
- Validação de tokens
- Operações administrativas

### Configuração de Logs

```yaml
logging:
  level:
    com.simple.auth: DEBUG
    org.springframework.security: INFO
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

## 🔧 Desenvolvimento

### Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/simple/auth/
│   │   ├── config/          # Configurações de segurança
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── exception/      # Tratamento de exceções
│   │   ├── repository/     # Repositórios JPA
│   │   ├── security/       # Configurações JWT
│   │   ├── service/        # Serviços de negócio
│   │   └── util/           # Utilitários
│   └── resources/
│       ├── application.yml
│       └── db/migration/   # Scripts Flyway
└── test/
    └── java/              # Testes unitários e integração
```

### Adicionando Novas Funcionalidades

1. **Novo Endpoint**: Criar controller e service
2. **Nova Role**: Adicionar em `Role.java` e migration
3. **Nova Validação**: Implementar em `ValidationService`
4. **Novo Filtro**: Estender `JwtAuthenticationFilter`

## 🚨 Troubleshooting

### Problemas Comuns

1. **Token inválido**:
   ```bash
   # Verificar configuração JWT
   echo $JWT_SECRET_KEY
   ```

2. **Conexão com banco**:
   ```bash
   # Testar conexão
   curl http://localhost:9081/actuator/health/db
   ```

3. **Senha não aceita**:
   - Verificar critérios de senha
   - Confirmar encoding BCrypt

### Debug

```bash
# Habilitar logs de debug
export LOGGING_LEVEL_COM_SIMPLE_AUTH=DEBUG

# Verificar health
curl http://localhost:9081/actuator/health
```

## 📚 Documentação Adicional

- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/)
- [BCrypt](https://en.wikipedia.org/wiki/Bcrypt)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Implemente as mudanças
4. Adicione testes de segurança
5. Envie um pull request

---

**Auth Service** - Protegendo o acesso ao sistema 🔐