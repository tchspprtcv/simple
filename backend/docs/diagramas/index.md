# Diagramas do Sistema Simple

Este documento apresenta os diagramas que ilustram a arquitetura, estrutura e funcionamento do sistema Simple, uma aplicação de gestão de pedidos de serviços municipais desenvolvida em Java com Spring Boot 3.2.0.

## 1. Diagrama de Arquitetura

Este diagrama mostra as camadas da aplicação e como elas se relacionam, seguindo o padrão MVC adaptado para APIs REST.

![Diagrama de Arquitetura](./Arquitetura%20do%20Sistema%20Simple.png)

O sistema está organizado nas seguintes camadas:
- **Camada de Apresentação (Controllers)**: Recebe as requisições HTTP e delega o processamento para os serviços.
- **Camada de Negócio (Services)**: Contém a lógica de negócio da aplicação.
- **Camada de Persistência (Repositories)**: Responsável pela persistência e recuperação de dados.
- **Camada de Domínio (Entities e DTOs)**: Representa as entidades do banco de dados e os objetos de transferência de dados.
- **Segurança**: Implementa autenticação baseada em JWT e autorização baseada em roles.

## 2. Diagrama de Entidade-Relacionamento (ER)

Este diagrama mostra as entidades principais do sistema e seus relacionamentos.

![Diagrama ER](./Diagrama%20ER%20do%20Sistema%20Simple.png)

As principais entidades do sistema são:
- **Usuario**: Representa os usuários do sistema (administradores, atendentes, técnicos, etc.).
- **Perfil**: Representa os perfis de acesso dos usuários.
- **Cidadao**: Representa os cidadãos que solicitam serviços.
- **Pedido**: Representa os pedidos de serviços municipais.
- **StatusPedido**: Representa os possíveis status de um pedido.
- **TipoServico**: Representa os tipos de serviços oferecidos.
- **CategoriaServico**: Representa as categorias de serviços.
- **Favorito**: Representa os tipos de serviços favoritados pelos usuários.

## 3. Diagrama de Fluxo de Dados

Este diagrama mostra como os dados fluem através do sistema para os principais casos de uso.

![Diagrama de Fluxo de Dados](./Fluxo%20de%20Dados%20do%20Sistema%20Simple.png)

Os principais fluxos de dados representados são:
- **Autenticação**: Processo de login e validação de token.
- **Gestão de Pedidos**: Criação, consulta e atualização de pedidos.
- **Gestão de Cidadãos**: Cadastro e consulta de cidadãos.
- **Configuração**: Gerenciamento de tipos de serviço e categorias.

## 4. Diagrama de Componentes

Este diagrama mostra os principais componentes do sistema e suas dependências.

![Diagrama de Componentes](./Diagrama%20de%20Componentes%20do%20Sistema%20Simple.png)

O sistema está organizado nos seguintes módulos:
- **Módulo de Autenticação**: Responsável pelo controle de acesso ao sistema.
- **Módulo de Gestão de Pedidos**: Responsável pelo gerenciamento dos pedidos de serviços.
- **Módulo de Gestão de Cidadãos**: Responsável pelo cadastro e gerenciamento de cidadãos.
- **Módulo de Configuração**: Responsável pelas configurações gerais do sistema.
- **Módulo de Favoritos**: Responsável pelo gerenciamento de favoritos.
- **Infraestrutura**: Componentes de suporte como tratamento de exceções, configuração de CORS e Swagger.

## 5. Diagrama de Sequência - Processo de Autenticação

Este diagrama mostra a sequência de interações entre os componentes durante o processo de autenticação.

![Diagrama de Sequência - Autenticação](./Diagrama%20de%20Sequência%20-%20Processo%20de%20Autenticação.png)

O diagrama ilustra:
- O processo de login, desde a requisição do usuário até a geração do token JWT.
- O uso do token em requisições subsequentes para autorização.
