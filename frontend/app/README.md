# Simple - Frontend

Frontend do sistema de gestão de pedidos de serviços municipais.

## Tecnologias Utilizadas

- Next.js 14
- React 18
- TypeScript
- Tailwind CSS
- Shadcn UI
- Axios

## Estrutura do Projeto

- `app`: Código principal da aplicação
  - `app`: Páginas da aplicação (usando App Router)
  - `components`: Componentes reutilizáveis
  - `lib`: Utilitários, serviços e tipos
- `public`: Arquivos estáticos

## Principais Funcionalidades

- Autenticação e autorização com JWT
- Dashboard para atendedores e cidadãos
- Pesquisa de serviços
- Favoritos e serviços mais utilizados
- Acompanhamento de pedidos
- Registro de novos pedidos
- Gestão de cidadãos

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

- Node.js 18+
- Backend Spring Boot em execução

### Configuração

1. Crie um arquivo `.env.local` na raiz do projeto com:
```
NEXT_PUBLIC_API_BASE=http://localhost:8080
```

### Instalando Dependências

```bash
npm install
```

### Executando em Desenvolvimento

```bash
npm run dev
```

### Compilando para Produção

```bash
npm run build
```

### Executando em Produção

```bash
npm start
```

## Autores

- Equipe de Desenvolvimento
