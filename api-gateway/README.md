# API Gateway - Simple

## 📋 Visão Geral

O **API Gateway** é o ponto de entrada único para todas as requisições da aplicação Simple. Ele atua como um proxy reverso que roteia as requisições para os microserviços apropriados, fornecendo funcionalidades como autenticação, autorização, rate limiting e monitoramento.

## 🏗️ Arquitetura

O API Gateway utiliza **Spring Cloud Gateway** para:
- Roteamento de requisições para microserviços
- Autenticação e autorização centralizadas
- Filtros de segurança
- Monitoramento e métricas
- Documentação API centralizada

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Cloud Gateway**
- **Spring Boot Actuator** - Monitoramento e métricas
- **SpringDoc OpenAPI** - Documentação da API
- **Maven** - Gerenciamento de dependências

## 📡 Rotas Configuradas

O API Gateway roteia as requisições para os seguintes serviços:

| Rota | Serviço de Destino | Porta |
|------|-------------------|-------|
| `/auth/**` | Auth Service | 9081 |
| `/citizens/**` | Citizen Service | 9082 |
| `/orders/**` | Order Service | 9083 |
| `/config/**` | Config Service | 9084 |
| `/favoritos/**` | Favorites Service | 9085 |

## 🔧 Configuração

### Variáveis de Ambiente

```bash
# JWT Configuration
JWT_SECRET_KEY=your-secret-key

# Service URLs (internal Docker network)
AUTH_SERVICE_URL=http://auth-service:8081
CITIZEN_SERVICE_URL=http://citizen-service:8082
ORDER_SERVICE_URL=http://order-service:8083
CONFIG_SERVICE_URL=http://config-service:8084
FAVORITES_SERVICE_URL=http://favorites-service:8085
```

### Arquivo de Configuração

As rotas e filtros são configurados em `src/main/resources/application.yml`:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: ${AUTH_SERVICE_URL:http://localhost:9081}
          predicates:
            - Path=/auth/**
        # ... outras rotas
```

## 🚀 Execução

### Desenvolvimento Local

```bash
# Compilar e executar
./mvnw spring-boot:run

# Ou usando Maven
mvn spring-boot:run
```

### Docker

```bash
# Construir imagem
docker build -t simple-api-gateway .

# Executar container
docker run -p 9080:8080 simple-api-gateway
```

### Docker Compose

```bash
# Executar todos os serviços
docker-compose up api-gateway
```

## 📊 Monitoramento

### Endpoints do Actuator

- **Health Check**: `GET /actuator/health`
- **Métricas**: `GET /actuator/metrics`
- **Info**: `GET /actuator/info`
- **Gateway Routes**: `GET /actuator/gateway/routes`

### Documentação da API

- **Swagger UI**: http://localhost:9080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:9080/v3/api-docs

## 🔒 Segurança

### Filtros de Segurança

1. **Authentication Filter**: Valida tokens JWT
2. **Authorization Filter**: Verifica permissões
3. **CORS Filter**: Configuração de CORS
4. **Rate Limiting**: Controle de taxa de requisições

### Fluxo de Autenticação

1. Cliente envia requisição com token JWT no header `Authorization`
2. API Gateway valida o token
3. Se válido, roteia para o microserviço apropriado
4. Se inválido, retorna erro 401 Unauthorized

## 🧪 Testes

```bash
# Executar testes unitários
./mvnw test

# Executar testes de integração
./mvnw verify

# Gerar relatório de cobertura
./mvnw jacoco:report
```

## 📝 Logs

### Configuração de Logs

Os logs são configurados em `src/main/resources/logback-spring.xml`:

- **Console**: Logs formatados para desenvolvimento
- **File**: Logs estruturados para produção
- **JSON**: Formato para agregação de logs

### Níveis de Log

- `ERROR`: Erros críticos
- `WARN`: Avisos importantes
- `INFO`: Informações gerais
- `DEBUG`: Informações detalhadas para debug

## 🔧 Desenvolvimento

### Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/simple/gateway/
│   │   ├── config/          # Configurações
│   │   ├── filter/          # Filtros customizados
│   │   ├── exception/       # Tratamento de exceções
│   │   └── GatewayApplication.java
│   └── resources/
│       ├── application.yml  # Configurações principais
│       └── logback-spring.xml
└── test/
    └── java/               # Testes unitários e integração
```

### Adicionando Novas Rotas

1. Edite `application.yml`
2. Adicione a nova rota na seção `spring.cloud.gateway.routes`
3. Configure predicados e filtros conforme necessário
4. Teste a nova rota

### Filtros Customizados

Para criar filtros customizados:

```java
@Component
public class CustomGatewayFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Lógica do filtro
        return chain.filter(exchange);
    }
}
```

## 🚨 Troubleshooting

### Problemas Comuns

1. **Serviço não encontrado**:
   - Verifique se o microserviço está rodando
   - Confirme as URLs de configuração

2. **Token JWT inválido**:
   - Verifique a chave secreta JWT
   - Confirme o formato do token

3. **CORS errors**:
   - Verifique a configuração de CORS
   - Confirme as origens permitidas

### Debug

```bash
# Habilitar logs de debug
export LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_CLOUD_GATEWAY=DEBUG

# Verificar rotas ativas
curl http://localhost:9080/actuator/gateway/routes
```

## 📚 Documentação Adicional

- [Spring Cloud Gateway Documentation](https://spring.io/projects/spring-cloud-gateway)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [JWT Authentication](https://jwt.io/)

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Implemente as mudanças
4. Adicione testes
5. Envie um pull request

---

**API Gateway** - Centralizando o acesso aos microserviços 🚪