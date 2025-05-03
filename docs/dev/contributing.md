# Guia de Contribuição

Obrigado pelo interesse em contribuir com o projeto Simple! Este documento fornece diretrizes para contribuir com o desenvolvimento do sistema de gestão de pedidos de serviços municipais.

## Índice

1. [Código de Conduta](#código-de-conduta)
2. [Como Posso Contribuir?](#como-posso-contribuir)
3. [Configuração do Ambiente](#configuração-do-ambiente)
4. [Fluxo de Trabalho Git](#fluxo-de-trabalho-git)
5. [Padrões de Codificação](#padrões-de-codificação)
6. [Testes](#testes)
7. [Documentação](#documentação)
8. [Processo de Revisão](#processo-de-revisão)
9. [Relatando Bugs](#relatando-bugs)
10. [Sugerindo Melhorias](#sugerindo-melhorias)

## Código de Conduta

Este projeto adota um Código de Conduta que esperamos que todos os participantes sigam. Por favor, leia o [Código de Conduta](CODE_OF_CONDUCT.md) para entender quais ações são e não são toleradas.

## Como Posso Contribuir?

### Reportando Bugs

Antes de criar um relatório de bug, verifique se o problema já não foi reportado. Se você encontrar um problema que ainda não foi reportado, crie uma nova issue seguindo o [modelo para relatório de bugs](#relatando-bugs).

### Sugerindo Melhorias

Se você tem uma ideia para melhorar o projeto, crie uma nova issue seguindo o [modelo para sugestões de melhorias](#sugerindo-melhorias).

### Contribuindo com Código

1. Procure por issues abertas ou crie uma nova para discutir a mudança que você gostaria de fazer
2. Fork o repositório
3. Crie uma branch para sua feature ou correção
4. Faça suas alterações
5. Escreva ou atualize testes, se necessário
6. Atualize a documentação, se necessário
7. Envie um pull request

## Configuração do Ambiente

Consulte o [Guia de Configuração](setup.md) para instruções detalhadas sobre como configurar seu ambiente de desenvolvimento.

## Fluxo de Trabalho Git

Seguimos um fluxo de trabalho baseado em feature branches:

1. **Fork o repositório**: Crie uma cópia do repositório em sua conta GitHub

2. **Clone seu fork**:
   ```bash
   git clone https://github.com/seu-usuario/simple.git
   cd simple
   ```

3. **Adicione o repositório original como remote**:
   ```bash
   git remote add upstream https://github.com/organizacao/simple.git
   ```

4. **Crie uma branch para sua feature ou correção**:
   ```bash
   git checkout -b feature/nome-da-feature
   # ou
   git checkout -b fix/nome-do-bug
   ```

5. **Faça commits de suas alterações**:
   ```bash
   git add .
   git commit -m "Descrição clara da alteração"
   ```

6. **Mantenha sua branch atualizada**:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

7. **Envie sua branch para seu fork**:
   ```bash
   git push origin feature/nome-da-feature
   ```

8. **Crie um Pull Request** a partir da sua branch para o repositório original

### Convenções de Commit

Utilizamos [Conventional Commits](https://www.conventionalcommits.org/) para padronizar as mensagens de commit:

```
<tipo>[escopo opcional]: <descrição>

[corpo opcional]

[rodapé(s) opcional(is)]
```

Tipos comuns:
- **feat**: Uma nova funcionalidade
- **fix**: Uma correção de bug
- **docs**: Alterações na documentação
- **style**: Alterações que não afetam o significado do código (espaços em branco, formatação, etc)
- **refactor**: Uma alteração de código que não corrige um bug nem adiciona uma funcionalidade
- **test**: Adicionando testes ausentes ou corrigindo testes existentes
- **chore**: Alterações no processo de build ou ferramentas auxiliares

Exemplo:
```
feat(pedidos): adiciona filtro por data de criação

Implementa filtro por data de criação na listagem de pedidos.
Isso permite aos usuários encontrar pedidos mais facilmente.

Closes #123
```

## Padrões de Codificação

### Backend (Java)

- Seguimos as [Convenções de Código Java da Google](https://google.github.io/styleguide/javaguide.html)
- Utilizamos 4 espaços para indentação
- Limite de 100 caracteres por linha
- Documentação JavaDoc para classes e métodos públicos
- Nomes de classes em PascalCase
- Nomes de métodos e variáveis em camelCase
- Constantes em SNAKE_CASE_MAIÚSCULO

### Frontend (JavaScript/TypeScript)

- Seguimos o [Airbnb JavaScript Style Guide](https://github.com/airbnb/javascript)
- Utilizamos 2 espaços para indentação
- Limite de 80 caracteres por linha
- Utilizamos TypeScript para tipagem estática
- Componentes React em PascalCase
- Funções e variáveis em camelCase
- Utilizamos ESLint e Prettier para garantir a consistência do código

### SQL

- Palavras-chave SQL em MAIÚSCULAS
- Nomes de tabelas e colunas em snake_case
- Indentação consistente para consultas complexas
- Comentários para explicar consultas complexas

## Testes

### Backend

- Todos os novos recursos devem ter testes unitários
- Testes de integração para funcionalidades críticas
- Execute `./mvnw test` para rodar os testes
- Mantenha a cobertura de código acima de 80%

### Frontend

- Componentes devem ter testes unitários
- Testes de integração para fluxos críticos
- Execute `npm test` ou `yarn test` para rodar os testes
- Utilize mocks para APIs e serviços externos

## Documentação

- Atualize a documentação quando adicionar, alterar ou remover funcionalidades
- Documente APIs usando JavaDoc (backend) ou JSDoc (frontend)
- Mantenha os diagramas atualizados (use PlantUML ou Mermaid)
- Atualize o README.md quando necessário

## Processo de Revisão

1. Todos os pull requests devem ser revisados por pelo menos um mantenedor
2. Os revisores verificarão:
   - Funcionalidade: O código faz o que se propõe a fazer?
   - Qualidade: O código segue os padrões de codificação?
   - Testes: Existem testes adequados?
   - Documentação: A documentação foi atualizada?

3. Feedback será dado através de comentários no pull request
4. Após aprovação, um mantenedor fará o merge do pull request

## Relatando Bugs

Ao relatar um bug, inclua:

- **Título**: Descrição concisa do problema
- **Descrição**: Explicação detalhada do problema
- **Passos para reproduzir**: Lista numerada de passos para reproduzir o bug
- **Comportamento esperado**: O que deveria acontecer
- **Comportamento atual**: O que está acontecendo
- **Ambiente**: Sistema operacional, navegador, versão do software, etc.
- **Screenshots**: Se aplicável
- **Contexto adicional**: Qualquer informação que possa ajudar a resolver o problema

## Sugerindo Melhorias

Ao sugerir uma melhoria, inclua:

- **Título**: Descrição concisa da melhoria
- **Problema**: O problema que sua melhoria resolve
- **Solução**: Descrição detalhada da melhoria proposta
- **Benefícios**: Como essa melhoria beneficiará os usuários
- **Alternativas**: Outras soluções que você considerou
- **Contexto adicional**: Qualquer informação adicional sobre a melhoria

---

Agradecemos suas contribuições para tornar o Simple um sistema melhor para todos!
