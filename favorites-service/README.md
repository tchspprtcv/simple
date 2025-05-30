# Favorites Service - Simple

## 📋 Visão Geral

O **Favorites Service** é responsável pelo gerenciamento de favoritos dos usuários na aplicação Simple. Permite que cidadãos marquem serviços, documentos e outras funcionalidades como favoritos para acesso rápido e personalização da experiência do usuário.

## 🏗️ Arquitetura

O serviço implementa:
- Gerenciamento de favoritos por usuário
- Categorização de favoritos
- Recomendações baseadas em favoritos
- Sincronização entre dispositivos
- Analytics de uso
- Cache para performance

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança
- **Spring Cache** - Cache de favoritos
- **PostgreSQL** - Banco de dados
- **Bean Validation** - Validação de dados
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

## 📡 Endpoints da API

### Gerenciamento de Favoritos

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/favorites` | Listar favoritos do usuário |
| `GET` | `/favorites/{id}` | Buscar favorito por ID |
| `POST` | `/favorites` | Adicionar aos favoritos |
| `DELETE` | `/favorites/{id}` | Remover dos favoritos |
| `PUT` | `/favorites/{id}` | Atualizar favorito |

### Favoritos por Categoria

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/favorites/services` | Serviços favoritos |
| `GET` | `/favorites/documents` | Documentos favoritos |
| `GET` | `/favorites/orders` | Pedidos favoritos |
| `GET` | `/favorites/categories` | Listar categorias |
| `GET` | `/favorites/category/{category}` | Favoritos por categoria |

### Recomendações

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/favorites/recommendations` | Recomendações personalizadas |
| `GET` | `/favorites/popular` | Itens mais favoritados |
| `GET` | `/favorites/recent` | Favoritos recentes |
| `GET` | `/favorites/trending` | Favoritos em alta |

### Estatísticas

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/favorites/stats` | Estatísticas do usuário |
| `GET` | `/favorites/stats/global` | Estatísticas globais |
| `GET` | `/favorites/analytics` | Analytics detalhados |

### Sincronização

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `GET` | `/favorites/sync` | Sincronizar favoritos |
| `POST` | `/favorites/sync/import` | Importar favoritos |
| `POST` | `/favorites/sync/export` | Exportar favoritos |

## 🔧 Configuração

### Variáveis de Ambiente

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:9433/simple_favorites
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Cache Configuration
SPRING_CACHE_TYPE=caffeine
CACHE_EXPIRY_MINUTES=30
CACHE_MAX_SIZE=10000

# JWT Configuration
JWT_SECRET_KEY=your-secret-key

# Service Integration
CITIZEN_SERVICE_URL=http://localhost:9081
ORDER_SERVICE_URL=http://localhost:9083
CONFIG_SERVICE_URL=http://localhost:9084

# Recommendations
RECOMMENDATION_ENABLED=true
RECOMMENDATION_MAX_ITEMS=10
RECOMMENDATION_ALGORITHM=collaborative

# Analytics
ANALYTICS_ENABLED=true
ANALYTICS_BATCH_SIZE=100
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
      spec: maximumSize=10000,expireAfterWrite=30m
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
docker build -t simple-favorites-service .

# Executar container
docker run -p 9085:8085 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:9433/simple_favorites \
  -e JWT_SECRET_KEY=your-secret-key \
  simple-favorites-service
```

### Docker Compose

```bash
# Executar serviço específico
docker-compose up favorites-service

# Executar com dependências
docker-compose up favorites-service db
```

## 🗄️ Modelo de Dados

### Entidades Principais

#### Favorite
```java
@Entity
public class Favorite {
    private Long id;
    private String userId;
    private String itemId;
    private FavoriteType type;
    private String category;
    private String title;
    private String description;
    private String url;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessedAt;
    private Integer accessCount;
    private boolean isActive;
    private String tags;
}
```

#### FavoriteCategory
```java
@Entity
public class FavoriteCategory {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private String color;
    private Integer sortOrder;
    private boolean isActive;
    private LocalDateTime createdAt;
}
```

#### UserFavoriteStats
```java
@Entity
public class UserFavoriteStats {
    private Long id;
    private String userId;
    private Integer totalFavorites;
    private Integer favoritesThisMonth;
    private String mostUsedCategory;
    private LocalDateTime lastActivity;
    private Map<String, Integer> categoryStats;
}
```

#### FavoriteRecommendation
```java
@Entity
public class FavoriteRecommendation {
    private Long id;
    private String userId;
    private String itemId;
    private FavoriteType type;
    private String reason;
    private Double score;
    private boolean isViewed;
    private boolean isAccepted;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
```

### Enums

```java
public enum FavoriteType {
    SERVICE, DOCUMENT, ORDER, PAGE, FORM, CONTACT, LOCATION
}

public enum RecommendationReason {
    POPULAR, SIMILAR_USERS, RECENT_ACTIVITY, CATEGORY_BASED, SEASONAL
}

public enum FavoriteStatus {
    ACTIVE, ARCHIVED, DELETED
}
```

## 📊 Funcionalidades

### Sistema de Favoritos

```java
// Adicionar favorito
@PostMapping("/favorites")
public ResponseEntity<Favorite> addFavorite(@RequestBody CreateFavoriteRequest request) {
    Favorite favorite = favoriteService.addFavorite(
        getCurrentUserId(), 
        request.getItemId(), 
        request.getType(),
        request.getMetadata()
    );
    return ResponseEntity.ok(favorite);
}

// Verificar se é favorito
@GetMapping("/favorites/check/{itemId}")
public ResponseEntity<Boolean> isFavorite(@PathVariable String itemId) {
    boolean isFavorite = favoriteService.isFavorite(getCurrentUserId(), itemId);
    return ResponseEntity.ok(isFavorite);
}
```

### Sistema de Recomendações

```java
// Algoritmo colaborativo
public List<Recommendation> getCollaborativeRecommendations(String userId) {
    List<String> similarUsers = findSimilarUsers(userId);
    return generateRecommendationsFromSimilarUsers(userId, similarUsers);
}

// Recomendações baseadas em categoria
public List<Recommendation> getCategoryBasedRecommendations(String userId) {
    Map<String, Integer> userCategories = getUserCategoryPreferences(userId);
    return generateCategoryRecommendations(userCategories);
}
```

### Analytics e Métricas

```java
// Rastrear acesso
@EventListener
public void handleFavoriteAccess(FavoriteAccessEvent event) {
    favoriteService.incrementAccessCount(event.getFavoriteId());
    analyticsService.recordAccess(event.getUserId(), event.getItemId());
}

// Gerar estatísticas
public UserFavoriteStats generateUserStats(String userId) {
    return UserFavoriteStats.builder()
        .userId(userId)
        .totalFavorites(countUserFavorites(userId))
        .favoritesThisMonth(countFavoritesThisMonth(userId))
        .mostUsedCategory(getMostUsedCategory(userId))
        .categoryStats(getCategoryStats(userId))
        .build();
}
```

## 📊 Monitoramento

### Endpoints do Actuator

- **Health Check**: `GET /actuator/health`
- **Métricas**: `GET /actuator/metrics`
- **Info**: `GET /actuator/info`
- **Cache Stats**: `GET /actuator/caches`

### Métricas Customizadas

- Total de favoritos por usuário
- Favoritos mais populares
- Taxa de aceitação de recomendações
- Distribuição por categoria
- Atividade por período

## 🔒 Segurança

### Autenticação
- Validação de tokens JWT
- Identificação do usuário logado

### Autorização

| Role | Permissões |
|------|------------|
| **CITIZEN** | Gerenciar próprios favoritos |
| **ATTENDANT** | Visualizar estatísticas |
| **ADMIN** | Acesso total, analytics globais |

### Privacidade

```java
// Dados pessoais protegidos
@JsonIgnore
private String userId; // Não exposto na API

// Anonimização para analytics
public void anonymizeUserData(String userId) {
    String hashedUserId = hashService.hash(userId);
    // Usar hash para analytics
}
```

## 🧪 Testes

```bash
# Executar todos os testes
./mvnw test

# Testes de integração
./mvnw test -Dtest=*IntegrationTest

# Testes de recomendação
./mvnw test -Dtest=*RecommendationTest

# Cobertura de código
./mvnw jacoco:report
```

### Testes de Exemplo

```bash
# Adicionar favorito
curl -X POST http://localhost:9085/favorites \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "itemId": "service-123",
    "type": "SERVICE",
    "title": "Licença de Construção",
    "category": "BUILDING"
  }'

# Listar favoritos
curl -X GET http://localhost:9085/favorites \
  -H "Authorization: Bearer $JWT_TOKEN"

# Obter recomendações
curl -X GET http://localhost:9085/favorites/recommendations \
  -H "Authorization: Bearer $JWT_TOKEN"
```

## 📝 Logs

### Eventos Logados

- Adição/remoção de favoritos
- Acesso a favoritos
- Geração de recomendações
- Sincronização de dados
- Erros de validação

### Configuração de Logs

```yaml
logging:
  level:
    com.simple.favorites: DEBUG
    org.springframework.cache: INFO
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

## 🔧 Desenvolvimento

### Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/simple/favorites/
│   │   ├── config/          # Configurações Spring
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── exception/      # Tratamento de exceções
│   │   ├── repository/     # Repositórios JPA
│   │   ├── service/        # Serviços de negócio
│   │   ├── recommendation/ # Sistema de recomendações
│   │   ├── analytics/      # Analytics e métricas
│   │   ├── cache/          # Configurações de cache
│   │   └── util/           # Utilitários
│   └── resources/
│       ├── application.yml
│       └── db/migration/   # Scripts Flyway
└── test/
    └── java/              # Testes unitários e integração
```

### Implementando Nova Categoria

1. **Definir Enum**: Adicionar em `FavoriteType`
2. **Criar Metadata**: Definir estrutura de metadados
3. **Atualizar Recomendações**: Incluir na lógica
4. **Configurar Cache**: Definir estratégia
5. **Testar**: Criar testes específicos

## 💾 Cache

### Estratégias de Cache

```java
@Cacheable(value = "user-favorites", key = "#userId")
public List<Favorite> getUserFavorites(String userId) {
    return favoriteRepository.findByUserIdAndIsActiveTrue(userId);
}

@Cacheable(value = "popular-items", key = "#category")
public List<PopularItem> getPopularItems(String category) {
    return analyticsService.getPopularItemsByCategory(category);
}

@CacheEvict(value = "user-favorites", key = "#userId")
public void clearUserCache(String userId) {
    // Cache será recarregado na próxima consulta
}
```

### Configuração do Cache

```yaml
spring:
  cache:
    caffeine:
      spec: |
        user-favorites=maximumSize=1000,expireAfterWrite=30m
        popular-items=maximumSize=100,expireAfterWrite=60m
        recommendations=maximumSize=500,expireAfterWrite=15m
```

## 🔍 Busca e Filtros

### Critérios de Busca

```java
// Busca por categoria
GET /favorites/category/SERVICE

// Busca por texto
GET /favorites/search?q=licença

// Filtros combinados
GET /favorites?category=DOCUMENT&type=PDF&recent=true

// Ordenação
GET /favorites?sort=createdAt,desc&sort=accessCount,desc
```

### Paginação

```java
GET /favorites?page=0&size=20&sort=lastAccessedAt,desc
```

## 🚨 Troubleshooting

### Problemas Comuns

1. **Favoritos não aparecem**:
   ```bash
   # Verificar cache
   curl http://localhost:9085/actuator/caches
   
   # Limpar cache do usuário
   curl -X DELETE http://localhost:9085/favorites/cache
   ```

2. **Recomendações não funcionam**:
   ```bash
   # Verificar configuração
   curl http://localhost:9085/actuator/configprops
   
   # Verificar dados de treinamento
   curl http://localhost:9085/favorites/stats/global
   ```

3. **Performance lenta**:
   - Verificar índices do banco
   - Analisar cache hit ratio
   - Otimizar queries

### Debug

```bash
# Habilitar logs de debug
export LOGGING_LEVEL_COM_SIMPLE_FAVORITES=DEBUG

# Verificar métricas
curl http://localhost:9085/actuator/metrics/favorites.total
```

## 📊 Analytics

### Métricas Coletadas

```java
// Eventos de favoritos
public class FavoriteEvent {
    private String userId;
    private String itemId;
    private FavoriteType type;
    private String action; // ADD, REMOVE, ACCESS
    private LocalDateTime timestamp;
    private Map<String, Object> context;
}

// Agregações
public class FavoriteAnalytics {
    private Map<String, Long> favoritesByCategory;
    private Map<String, Long> favoritesByType;
    private List<PopularItem> mostFavorited;
    private Map<String, Double> categoryGrowth;
}
```

### Relatórios

```java
// Relatório de uso
GET /favorites/analytics/usage?period=monthly

// Relatório de popularidade
GET /favorites/analytics/popular?category=SERVICE

// Relatório de recomendações
GET /favorites/analytics/recommendations?success=true
```

## 🔄 Sincronização

### Sincronização entre Dispositivos

```java
@PostMapping("/favorites/sync")
public ResponseEntity<SyncResult> syncFavorites(
    @RequestBody SyncRequest request) {
    
    SyncResult result = syncService.synchronize(
        getCurrentUserId(),
        request.getDeviceId(),
        request.getLastSyncTime(),
        request.getFavorites()
    );
    
    return ResponseEntity.ok(result);
}
```

### Resolução de Conflitos

```java
public class ConflictResolver {
    public Favorite resolveConflict(Favorite local, Favorite remote) {
        // Estratégia: último modificado ganha
        return local.getUpdatedAt().isAfter(remote.getUpdatedAt()) 
            ? local : remote;
    }
}
```

## 📚 Documentação Adicional

- [Spring Cache](https://spring.io/guides/gs/caching/)
- [Recommendation Systems](https://developers.google.com/machine-learning/recommendation)
- [User Experience Best Practices](https://uxplanet.org/favorites-and-bookmarks-ux-design-patterns-7b2f7b5b7e8f)

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Implemente as mudanças
4. Adicione testes de favoritos
5. Envie um pull request

---

**Favorites Service** - Personalizando a experiência do usuário ⭐