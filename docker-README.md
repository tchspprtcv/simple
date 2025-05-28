# Guia de Deploy da Aplicação Simple com Docker no Windows

Este guia fornece instruções detalhadas para configurar e executar a aplicação Simple usando Docker no Windows. A aplicação é composta por três componentes principais: banco de dados PostgreSQL, backend Spring Boot e frontend Next.js.

image.png

## Pré-requisitos

1. **Docker Desktop para Windows**
   - Baixe e instale o [Docker Desktop para Windows](https://www.docker.com/products/docker-desktop)
   - Requisitos mínimos:
     - Windows 10 64-bit: Pro, Enterprise, ou Education (Build 16299 ou posterior)
     - Habilitar o recurso Hyper-V e Containers do Windows
     - 4GB de RAM no mínimo

2. **Git** (opcional, caso precise clonar o repositório)
   - Baixe e instale o [Git para Windows](https://git-scm.com/download/win)

## Configuração e Execução

### 1. Preparação do Ambiente

1. Clone o repositório (se ainda não tiver feito):
   ```
   git clone <URL_DO_REPOSITÓRIO>
   cd simple
   ```

2. Verifique se o Docker Desktop está em execução:
   - Procure pelo ícone do Docker na barra de tarefas
   - Ou abra o Docker Desktop a partir do menu Iniciar

### 2. Construção e Execução dos Containers

1. Abra o Prompt de Comando ou PowerShell como administrador

2. Navegue até o diretório raiz do projeto:
   ```
   cd caminho\para\simple
   ```

3. Execute o comando para construir e iniciar todos os containers:
   ```
   docker-compose up -d
   ```
   
   Este comando:
   - Constrói as imagens Docker para o backend e frontend
   - Baixa a imagem do PostgreSQL
   - Cria e inicia todos os containers em modo detached (-d)
   - Configura a rede para comunicação entre os containers

4. Verifique se todos os containers estão em execução:
   ```
   docker-compose ps
   ```

### 3. Acessando a Aplicação

Após a inicialização bem-sucedida de todos os containers, você pode acessar:

- **Frontend**: http://localhost:3000
- **Backend API (Monolith)**: http://localhost:9081 (This is the direct access to the monolith backend)
- **API Gateway**: http://localhost:9080 (This is the entry point for microservices, used by the frontend)
- **Banco de dados**: Acessível na porta 5433 do host (o contêiner continua usando a porta 5432 internamente). Use um cliente PostgreSQL como pgAdmin ou DBeaver para conectar-se a `localhost:5433`.

### 4. Gerenciamento dos Containers

- **Parar todos os containers**:
  ```
  docker-compose stop
  ```

- **Iniciar containers parados**:
  ```
  docker-compose start
  ```

- **Parar e remover todos os containers**:
  ```
  docker-compose down
  ```

- **Parar, remover containers e volumes** (apaga dados do banco):
  ```
  docker-compose down -v
  ```

- **Reconstruir as imagens** (após alterações no código):
  ```
  docker-compose build
  ```
  ou
  ```
  docker-compose up -d --build
  ```

### 5. Configurações Personalizadas

#### Alterando Variáveis de Ambiente

Você pode modificar as variáveis de ambiente no arquivo `docker-compose.yml`:

- **Banco de dados**:
  - `POSTGRES_DB`: Nome do banco de dados
  - `POSTGRES_USER`: Nome de utilizador
  - `POSTGRES_PASSWORD`: Senha do utilizador

- **Backend**:
  - `SPRING_DATASOURCE_URL`: URL de conexão com o banco de dados
  - `SPRING_DATASOURCE_USERNAME`: Nome de utilizador do banco
  - `SPRING_DATASOURCE_PASSWORD`: Senha do banco

- **Frontend**:
  - `NEXT_PUBLIC_API_URL`: URL da API do backend

#### Alterando Portas

Para alterar as portas mapeadas, modifique as configurações de `ports` no arquivo `docker-compose.yml`:

```yaml
ports:
  - "NOVA_PORTA_HOST:PORTA_CONTAINER"
```

### 6. Solução de Problemas

#### Verificando Logs

Para visualizar os logs de um container específico:
```
docker-compose logs [serviço]
```

Exemplo:
```
docker-compose logs backend
```

Para acompanhar os logs em tempo real:
```
docker-compose logs -f [serviço]
```

#### Problemas Comuns

1. **Erro de conexão com o banco de dados**:
   - Verifique se o container do PostgreSQL está em execução
   - Verifique as credenciais e URL de conexão no arquivo `docker-compose.yml`
   - Aguarde alguns segundos após iniciar os containers para que o PostgreSQL esteja pronto

2. **Erro de porta já em uso**:
   - Verifique se as portas 3000 (frontend), 8080 (backend) e 5433 (PostgreSQL no host) não estão sendo usadas por outros aplicativos.
   - Altere o mapeamento de portas no arquivo `docker-compose.yml` se necessário

3. **Problemas de permissão no Windows**:
   - Execute o Prompt de Comando ou PowerShell como administrador
   - Verifique as permissões do Docker Desktop

4. **Containers não iniciam corretamente**:
   - Verifique os logs para identificar o problema
   - Tente reconstruir as imagens: `docker-compose build --no-cache`

## Estrutura do Projeto Docker

- `backend/Dockerfile`: Configuração para construir a imagem do backend Spring Boot
- `frontend/Dockerfile`: Configuração para construir a imagem do frontend Next.js
- `docker-compose.yml`: Configuração dos serviços, redes e volumes

## Notas Adicionais

- Os dados do PostgreSQL são persistidos em um volume Docker, o que significa que eles não serão perdidos quando os containers forem reiniciados.
- Para aplicar alterações no código-fonte, você precisará reconstruir as imagens Docker usando `docker-compose up -d --build`.
- Em ambiente de produção, considere configurar variáveis de ambiente mais seguras e utilizar secrets do Docker para senhas.


