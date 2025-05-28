# Simple - Sistema de Gestão de Pedidos de Serviços Municipais

## Visão Geral

O **Simple** é uma aplicação completa para gestão de pedidos de serviços municipais, desenvolvida para facilitar o atendimento ao utente e otimizar os processos internos da administração municipal. A aplicação permite o registro, acompanhamento e gestão de diversos tipos de processos municipais, como legalização de imóveis, compra de lotes, pedidos de eventos, licenciamentos e emissão de plantas.

## Estrutura do Projeto

O projeto está organizado em três componentes principais:

### 1. Banco de Dados (PostgreSQL)

O banco de dados armazena todas as informações da aplicação, incluindo dados de utilizadores, utentes, pedidos, documentos, pagamentos e histórico de processos.

**Localização:** `~/simple/database/`

**Arquivos principais:**
- `schema.sql`: Define a estrutura completa do banco de dados, incluindo tabelas, índices, funções e triggers.
- `er_diagram.*`: Diagramas de entidade-relacionamento em vários formatos.

### 2. Backend (Spring Boot)

O backend é responsável pela lógica de negócio, processamento de dados e exposição de APIs RESTful para o frontend.

**Localização:** `~/simple/backend/`

**Estrutura principal:**
- `src/main/java/com/municipio/simple/`: Código-fonte Java
  - `config/`: Configurações do Spring Boot
  - `controller/`: Controladores REST
  - `dto/`: Objetos de transferência de dados
  - `entity/`: Entidades JPA
  - `exception/`: Tratamento de exceções
  - `repository/`: Repositórios de acesso a dados
  - `security/`: Configurações de segurança
  - `service/`: Serviços de negócio
  - `util/`: Classes utilitárias
- `src/main/resources/`: Arquivos de configuração e recursos

### 3. Frontend (Next.js)

O frontend fornece a interface de utilizador para utentes e funcionários municipais.

**Localização:** `~/simple/frontend/`

**Estrutura principal:**
- `app/`: Código-fonte do Next.js
  - `app/`: Rotas e páginas da aplicação
  - `components/`: Componentes React reutilizáveis
  - `hooks/`: React hooks personalizados
  - `lib/`: Bibliotecas e utilitários
  - `prisma/`: Cliente Prisma para acesso ao banco de dados

### 4. Configuração Docker

A aplicação pode ser facilmente implantada usando Docker, com configurações para todos os componentes.

**Localização:** Arquivos na raiz e dentro dos diretórios dos componentes

**Arquivos principais:**
- `docker-compose.yml`: Configuração dos serviços Docker
- `backend/Dockerfile`: Configuração para construir a imagem do backend
- `frontend/Dockerfile`: Configuração para construir a imagem do frontend
- `docker-README.md`: Instruções para deploy com Docker

## Tecnologias Utilizadas

### Backend
- **Linguagem**: Java
- **Framework**: Spring Boot
- **Persistência**: Spring Data JPA
- **Segurança**: Spring Security
- **Documentação API**: Swagger/OpenAPI

### Frontend
- **Framework**: Next.js (React)
- **Estilização**: Tailwind CSS
- **Gerenciamento de Estado**: React Context API
- **Cliente HTTP**: Axios

### Banco de Dados
- **SGBD**: PostgreSQL
- **Extensões**: uuid-ossp, pgcrypto

### DevOps
- **Containerização**: Docker
- **Orquestração**: Docker Compose

## Documentação Adicional

Para informações mais detalhadas sobre o projeto, consulte os seguintes documentos:

- [Arquitetura](./architecture.md): Descrição detalhada da arquitetura do sistema
- [Diagramas de Sequência](./sequence_diagrams.md): Fluxos principais da aplicação
- [Configuração do Ambiente](./setup.md): Guia para configurar o ambiente de desenvolvimento
- [Contribuição](./contributing.md): Diretrizes para contribuir com o projeto

## Contato

Para mais informações ou suporte, entre em contato com a equipe de desenvolvimento.
