# Simple - Backend

Backend do sistema de gestão de pedidos de serviços municipais.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.0
- Spring Security com JWT
- Spring Data JPA
- PostgreSQL
- Lombok
- SpringDoc OpenAPI (Swagger)

## Estrutura do Projeto

- `config`: Configurações do Spring Boot, Spring Security, JWT, etc.
- `controller`: Controladores REST
- `dto`: Objetos de transferência de dados
- `entity`: Entidades JPA
- `exception`: Exceções personalizadas
- `repository`: Repositórios JPA
- `security`: Classes relacionadas à segurança
- `service`: Serviços de negócio
- `util`: Classes utilitárias

## Principais Funcionalidades

- Autenticação e autorização com JWT
- Gestão de usuários e perfis
- Gestão de cidadãos
- Gestão de pedidos de serviços municipais
- Acompanhamento de pedidos
- Gestão de documentos
- Gestão de pagamentos
- Gestão de vistorias

## Fluxos de Processos Suportados

1. Legalização
2. Compra de Lote
3. Pedido de Eventos
4. Pedido de Pagamento em Prestação
5. Mudança de Nome
6. Atualização de Planta
7. Emissão de Planta
8. Licenciamento Comercial (1ª Vez e Renovação)
9. Aprovação de projetos
10. Licença de Construção (1ª Vez e Renovação)

## Executando o Projeto

### Pré-requisitos

- Java 17
- PostgreSQL

### Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL chamado `simple`
2. Execute o script SQL em `database/schema.sql`

### Executando a Aplicação

```bash
./mvnw spring-boot:run
```

### Compilando o Projeto

```bash
./mvnw clean package
```

### Executando os Testes

```bash
./mvnw test
```

## Documentação da API

A documentação da API está disponível através do Swagger UI:

```
http://localhost:8080/api/swagger-ui.html
```

## Autores

- Equipe de Desenvolvimento
