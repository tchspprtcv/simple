# Guia de Configuração do Ambiente de Desenvolvimento

Este guia fornece instruções detalhadas para configurar o ambiente de desenvolvimento para o projeto Simple. Siga os passos abaixo para configurar cada componente do sistema.

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

1. **Java Development Kit (JDK)** - versão 17 ou superior
2. **Node.js** - versão 18 ou superior
3. **npm** ou **yarn** - gerenciadores de pacotes para JavaScript
4. **PostgreSQL** - versão 14 ou superior
5. **Git** - para controle de versão
6. **Docker** e **Docker Compose** (opcional, para desenvolvimento com containers)
7. **Maven** - para build do projeto Java
8. **IDE** - recomendamos IntelliJ IDEA para backend e VS Code para frontend

## 1. Configuração do Banco de Dados

### Instalação Local do PostgreSQL

1. Baixe e instale o PostgreSQL a partir do [site oficial](https://www.postgresql.org/download/)
2. Durante a instalação, defina uma senha para o usuário `postgres`
3. Após a instalação, crie um novo banco de dados e usuário:

```sql
CREATE DATABASE simple;
CREATE USER simple_user WITH ENCRYPTED PASSWORD 'simple_password';
GRANT ALL PRIVILEGES ON DATABASE simple TO simple_user;
```

4. Importe o esquema do banco de dados:

```bash
psql -U simple_user -d simple -f ~/simple/database/schema.sql
```

### Usando Docker para o Banco de Dados

Alternativamente, você pode usar Docker para executar o PostgreSQL:

```bash
cd ~/simple
docker-compose up -d postgres
```

Isso iniciará apenas o serviço de banco de dados definido no arquivo `docker-compose.yml`.

## 2. Configuração do Backend (Spring Boot)

### Configuração do Ambiente

1. Navegue até o diretório do backend:

```bash
cd ~/simple/backend
```

2. Configure as propriedades de conexão com o banco de dados. Crie ou edite o arquivo `src/main/resources/application-dev.properties`:

```properties
# Configuração do banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/simple
spring.datasource.username=simple_user
spring.datasource.password=simple_password
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Configuração do servidor
server.port=8080

# Configuração de logging
logging.level.com.municipio.simple=DEBUG
logging.level.org.springframework.web=INFO

# Configuração de segurança
jwt.secret=your_jwt_secret_key_here
jwt.expiration=86400000
```

### Compilação e Execução

#### Usando Maven

1. Compile o projeto:

```bash
./mvnw clean package -DskipTests
```

2. Execute a aplicação:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Usando sua IDE

1. Importe o projeto como um projeto Maven
2. Configure o perfil de execução para `dev`
3. Execute a classe principal `com.municipio.simple.SimpleApplication`

### Verificação

Acesse `http://localhost:8080/api/health` para verificar se a aplicação está em execução. Você deve receber uma resposta com status 200 OK.

## 3. Configuração do Frontend (Next.js)

### Instalação de Dependências

1. Navegue até o diretório do frontend:

```bash
cd ~/simple/frontend
```

2. Instale as dependências:

```bash
npm install
# ou
yarn install
```

### Configuração do Ambiente

Crie um arquivo `.env.local` no diretório do frontend com as seguintes variáveis:

```
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXTAUTH_URL=http://localhost:3000
NEXTAUTH_SECRET=your_nextauth_secret_here
```

### Execução em Modo de Desenvolvimento

```bash
npm run dev
# ou
yarn dev
```

Isso iniciará o servidor de desenvolvimento do Next.js na porta 3000. Acesse `http://localhost:3000` para visualizar a aplicação.

## 4. Desenvolvimento com Docker

Para desenvolver usando Docker, você pode usar o Docker Compose para iniciar todos os serviços:

```bash
cd ~/simple
docker-compose up -d
```

Isso iniciará o banco de dados, backend e frontend em containers separados.

### Desenvolvimento com Hot Reload

Para desenvolvimento com hot reload (recarregamento automático ao alterar o código), você pode modificar o `docker-compose.yml` para montar os diretórios de código como volumes:

```yaml
services:
  backend:
    # ... outras configurações ...
    volumes:
      - ./backend/src:/app/src
      - ./backend/pom.xml:/app/pom.xml
    environment:
      SPRING_DEVTOOLS_RESTART_ENABLED: "true"

  frontend:
    # ... outras configurações ...
    volumes:
      - ./frontend/app:/app/app
      - ./frontend/components:/app/components
      - ./frontend/hooks:/app/hooks
      - ./frontend/lib:/app/lib
      - ./frontend/public:/app/public
```

## 5. Configuração de Ferramentas de Desenvolvimento

### Configuração do ESLint e Prettier (Frontend)

1. O projeto já inclui configurações para ESLint e Prettier
2. Para executar a verificação de linting:

```bash
npm run lint
# ou
yarn lint
```

3. Para formatar o código automaticamente:

```bash
npm run format
# ou
yarn format
```

### Configuração do Checkstyle e SpotBugs (Backend)

1. O projeto inclui plugins Maven para Checkstyle e SpotBugs
2. Para executar a verificação de estilo:

```bash
./mvnw checkstyle:check
```

3. Para executar a análise estática de código:

```bash
./mvnw spotbugs:check
```

## 6. Configuração de Testes

### Testes de Backend

1. Os testes unitários e de integração estão localizados em `src/test/java`
2. Para executar os testes:

```bash
./mvnw test
```

3. Para executar os testes com cobertura:

```bash
./mvnw verify
```

O relatório de cobertura será gerado em `target/site/jacoco/index.html`.

### Testes de Frontend

1. Os testes estão localizados em arquivos `*.test.js` ou `*.test.tsx`
2. Para executar os testes:

```bash
npm test
# ou
yarn test
```

## 7. Depuração

### Depuração do Backend

1. Na sua IDE, configure um breakpoint no código
2. Inicie a aplicação em modo de depuração
   - No IntelliJ IDEA: clique com o botão direito na classe principal e selecione "Debug"
   - Com Maven: `./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"`

### Depuração do Frontend

1. Use as ferramentas de desenvolvedor do navegador (F12)
2. Para depuração do servidor Next.js, adicione a flag `--inspect`:

```bash
NODE_OPTIONS='--inspect' npm run dev
```

## 8. Configuração de Proxy Reverso (Opcional)

Para desenvolvimento com um único endpoint, você pode configurar um proxy reverso usando Nginx:

1. Instale o Nginx
2. Configure o arquivo `/etc/nginx/sites-available/simple.conf`:

```nginx
server {
    listen 80;
    server_name simple.local;

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location / {
        proxy_pass http://localhost:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

3. Crie um link simbólico e reinicie o Nginx:

```bash
sudo ln -s /etc/nginx/sites-available/simple.conf /etc/nginx/sites-enabled/
sudo systemctl restart nginx
```

4. Adicione `simple.local` ao seu arquivo `/etc/hosts`:

```
127.0.0.1 simple.local
```

Agora você pode acessar a aplicação em `http://simple.local`.

## Solução de Problemas Comuns

### Problemas de Conexão com o Banco de Dados

- Verifique se o PostgreSQL está em execução: `sudo systemctl status postgresql`
- Verifique as credenciais no arquivo `application-dev.properties`
- Teste a conexão manualmente: `psql -U simple_user -d simple -h localhost`

### Problemas com o Backend

- Verifique os logs: `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev > backend.log 2>&1`
- Verifique se as dependências foram baixadas corretamente: `./mvnw dependency:tree`
- Limpe o cache do Maven: `./mvnw clean`

### Problemas com o Frontend

- Limpe o cache do npm: `npm cache clean --force`
- Verifique se todas as dependências foram instaladas: `npm ls`
- Verifique se a variável `NEXT_PUBLIC_API_URL` está configurada corretamente

### Problemas com Docker

- Verifique o status dos containers: `docker-compose ps`
- Verifique os logs: `docker-compose logs -f [serviço]`
- Recrie os containers: `docker-compose down && docker-compose up -d`
