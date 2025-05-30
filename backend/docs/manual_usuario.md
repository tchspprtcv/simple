# Manual do Utilizador - Sistema Simple

## Sumário
1. [Introdução ao Sistema](#1-introdução-ao-sistema)
2. [Como Acessar o Sistema](#2-como-acessar-o-sistema)
3. [Navegação Básica e Interface do Utilizador](#3-navegação-básica-e-interface-do-usuário)
4. [Funcionalidades por Perfil de Utilizador](#4-funcionalidades-por-perfil-de-usuário)
   - [Administrador](#41-administrador)
   - [Atendente](#42-atendente)
   - [Gestor](#43-gestor)
   - [Técnico](#44-técnico)
   - [Fiscal](#45-fiscal)
5. [Guias Passo a Passo](#5-guias-passo-a-passo)
   - [Criar e Gerenciar Pedidos](#51-criar-e-gerenciar-pedidos)
   - [Gerenciar Cidadãos](#52-gerenciar-cidadãos)
   - [Atribuir Pedidos a Técnicos](#53-atribuir-pedidos-a-técnicos)
   - [Acompanhar Status de Pedidos](#54-acompanhar-status-de-pedidos)
   - [Configurar o Sistema](#55-configurar-o-sistema)
6. [Dicas e Melhores Práticas](#6-dicas-e-melhores-práticas)
7. [Troubleshooting e FAQs](#7-troubleshooting-e-faqs)

---

## 1. Introdução ao Sistema

O **Simple** é um sistema de gestão de pedidos de serviços municipais desenvolvido para otimizar e facilitar o atendimento às solicitações dos cidadãos. Através de uma interface intuitiva e funcional, o sistema permite o registro, acompanhamento e resolução de pedidos de serviços públicos.

### Principais Benefícios

- **Centralização de informações**: Todos os pedidos são registrados em um único sistema
- **Rastreabilidade**: Acompanhamento do status de cada pedido em tempo real
- **Organização de fluxos de trabalho**: Distribuição eficiente de tarefas entre os técnicos
- **Transparência**: Cidadãos e gestores podem acompanhar o andamento dos serviços
- **Análise de dados**: Relatórios e estatísticas para tomada de decisões

### Visão Geral do Fluxo de Trabalho

1. Utente solicita um serviço
2. Atendente registra o pedido no sistema
3. Gestor analisa e atribui o pedido a um técnico
4. Técnico executa o serviço e atualiza o status
5. Fiscal verifica a qualidade do serviço realizado
6. Pedido é finalizado e o cidadão é notificado

![Fluxo de trabalho do sistema](images/fluxo_trabalho.png)

---

## 2. Como Acessar o Sistema

### Requisitos de Sistema

- Navegador web atualizado (Google Chrome, Mozilla Firefox, Microsoft Edge ou Safari)
- Conexão com a internet
- Credenciais de acesso fornecidas pelo administrador do sistema

### Processo de Login

1. Abra seu navegador e acesse o endereço do sistema (fornecido pelo administrador)
2. Na tela de login, insira seu nome de usuário e senha
3. Selecione o perfil de acesso (caso tenha múltiplos perfis)
4. Clique no botão "Entrar"

![Tela de login](images/login.png)

### Primeiro Acesso

Se este for seu primeiro acesso ao sistema:

1. Use as credenciais temporárias fornecidas pelo administrador
2. Ao entrar, você será solicitado a alterar sua senha
3. Crie uma nova senha seguindo os requisitos de segurança
4. Confirme a nova senha
5. Clique em "Salvar"

### Recuperação de Senha

Caso esqueça sua senha:

1. Na tela de login, clique em "Esqueci minha senha"
2. Informe seu e-mail cadastrado no sistema
3. Siga as instruções enviadas para seu e-mail para redefinir sua senha

---

## 3. Navegação Básica e Interface do Utilizador

### Estrutura da Interface

Após o login, você terá acesso à interface principal do sistema, composta por:

- **Barra superior**: Contém o logo do sistema, nome do usuário logado, perfil ativo e botão de logout
- **Menu lateral**: Acesso às principais funcionalidades do sistema
- **Área de trabalho**: Exibe o conteúdo da funcionalidade selecionada
- **Barra de status**: Informações sobre o sistema e notificações

![Interface principal](images/interface_principal.png)

### Menu Lateral

O menu lateral apresenta as opções disponíveis de acordo com o seu perfil de acesso:

- **Dashboard**: Visão geral e estatísticas
- **Pedidos**: Gerenciamento de pedidos de serviços
- **Cidadãos**: Cadastro e consulta de cidadãos
- **Técnicos**: Gerenciamento de técnicos (visível para perfis específicos)
- **Relatórios**: Geração de relatórios e estatísticas
- **Configurações**: Configurações do sistema (visível para administradores)

### Elementos Comuns da Interface

- **Botões de ação**: Geralmente localizados no topo ou no final das páginas
- **Filtros de busca**: Permitem localizar informações específicas
- **Tabelas de dados**: Exibem listas de informações com opções de ordenação
- **Formulários**: Para inserção e edição de dados
- **Ícones de ação**: Representam ações como editar, excluir, visualizar, etc.

### Navegação Entre Telas

- Utilize o menu lateral para acessar as diferentes funcionalidades
- Use o "breadcrumb" (caminho de navegação) no topo da área de trabalho para retornar a telas anteriores
- Botões "Voltar" e "Avançar" estão disponíveis em formulários de múltiplas etapas

---

## 4. Funcionalidades por Perfil de Utilizador

O sistema Simple possui diferentes perfis de usuários, cada um com acesso a funcionalidades específicas de acordo com suas responsabilidades.

### 4.1 Administrador

O Administrador é responsável pela configuração e manutenção do sistema.

#### Principais Funcionalidades:

- **Gerenciamento de Usuários**:
  - Cadastrar, editar e desativar usuários
  - Atribuir perfis de acesso
  - Resetar senhas

- **Configurações do Sistema**:
  - Definir parâmetros gerais
  - Configurar integrações
  - Gerenciar tabelas de domínio (tipos de serviços, categorias, etc.)

- **Monitoramento**:
  - Visualizar logs do sistema
  - Acompanhar desempenho
  - Verificar estatísticas de uso

![Painel do Administrador](images/painel_admin.png)

### 4.2 Atendente

O Atendente é responsável pelo primeiro contato com o cidadão e registro de pedidos.

#### Principais Funcionalidades:

- **Atendimento ao Utente**:
  - Cadastrar e atualizar dados de cidadãos
  - Registrar pedidos de serviços
  - Consultar status de pedidos existentes

- **Gestão de Pedidos**:
  - Registrar novos pedidos
  - Anexar documentos e evidências
  - Encaminhar pedidos para análise

- **Comunicação**:
  - Enviar notificações aos cidadãos
  - Registrar contatos e interações

![Painel do Atendente](images/painel_atendente.png)

### 4.3 Gestor

O Gestor é responsável pela análise, priorização e distribuição dos pedidos.

#### Principais Funcionalidades:

- **Gestão de Demandas**:
  - Analisar pedidos recebidos
  - Definir prioridades
  - Atribuir pedidos aos técnicos

- **Monitoramento de Atividades**:
  - Acompanhar o andamento dos pedidos
  - Verificar prazos e SLAs
  - Identificar gargalos no atendimento

- **Relatórios Gerenciais**:
  - Gerar relatórios de desempenho
  - Analisar indicadores
  - Exportar dados para análise

![Painel do Gestor](images/painel_gestor.png)

### 4.4 Técnico

O Técnico é responsável pela execução dos serviços solicitados.

#### Principais Funcionalidades:

- **Gestão de Tarefas**:
  - Visualizar pedidos atribuídos
  - Atualizar status de execução
  - Registrar atividades realizadas

- **Registro de Execução**:
  - Documentar serviços executados
  - Anexar fotos e evidências
  - Registrar materiais utilizados

- **Agenda de Trabalho**:
  - Visualizar programação de serviços
  - Gerenciar disponibilidade
  - Receber notificações de novos pedidos

![Painel do Técnico](images/painel_tecnico.png)

### 4.5 Fiscal

O Fiscal é responsável pela verificação da qualidade dos serviços executados.

#### Principais Funcionalidades:

- **Fiscalização**:
  - Verificar serviços concluídos
  - Avaliar qualidade da execução
  - Aprovar ou solicitar correções

- **Documentação**:
  - Registrar pareceres de fiscalização
  - Anexar evidências fotográficas
  - Emitir relatórios de conformidade

- **Acompanhamento**:
  - Monitorar indicadores de qualidade
  - Identificar padrões de problemas
  - Sugerir melhorias nos processos

![Painel do Fiscal](images/painel_fiscal.png)

---

## 5. Guias Passo a Passo

### 5.1 Criar e Gerenciar Pedidos

#### Criar um Novo Pedido

1. No menu lateral, clique em "Pedidos" e depois em "Novo Pedido"
2. Preencha os dados do solicitante:
   - Se o cidadão já estiver cadastrado, pesquise pelo nome, CNI ou e-mail
   - Se for um novo cidadão, clique em "Cadastrar Novo Utente" e preencha os dados
3. Selecione o tipo de serviço solicitado na lista disponível
4. Preencha os detalhes do pedido:
   - Descrição da solicitação
   - Endereço do serviço
   - Prioridade (normal, urgente, emergencial)
5. Anexe documentos ou fotos, se necessário, clicando em "Anexar Arquivo"
6. Clique em "Salvar" para registrar o pedido ou "Salvar e Encaminhar" para já enviar para análise

![Criação de pedido](images/criar_pedido.png)

#### Consultar Pedidos

1. No menu lateral, clique em "Pedidos" e depois em "Consultar Pedidos"
2. Utilize os filtros disponíveis para localizar pedidos específicos:
   - Número do pedido
   - Nome do solicitante
   - Tipo de serviço
   - Status
   - Período
3. Clique em "Pesquisar" para exibir os resultados
4. Na lista de resultados, você pode:
   - Clicar no número do pedido para visualizar detalhes
   - Utilizar os ícones de ação para editar, cancelar ou imprimir

![Consulta de pedidos](images/consultar_pedidos.png)

#### Atualizar Status de um Pedido

1. Localize o pedido desejado através da consulta
2. Clique no número do pedido para abrir os detalhes
3. Na tela de detalhes, clique no botão "Atualizar Status"
4. Selecione o novo status na lista disponível
5. Adicione observações sobre a atualização, se necessário
6. Clique em "Salvar" para confirmar a alteração

### 5.2 Gerenciar Cidadãos

#### Cadastrar Novo Utente

1. No menu lateral, clique em "Cidadãos" e depois em "Novo Cadastro"
2. Preencha os dados pessoais:
   - Nome completo
   - CNI/CNPJ
   - Data de nascimento
   - Gênero
3. Informe os dados de contato:
   - Telefone
   - E-mail
   - Endereço completo
4. Defina as preferências de contato (e-mail, SMS, telefone)
5. Clique em "Salvar" para concluir o cadastro

![Cadastro de cidadão](images/cadastro_cidadao.png)

#### Consultar e Editar Cadastro de Utente

1. No menu lateral, clique em "Cidadãos" e depois em "Consultar"
2. Utilize os filtros para localizar o cidadão:
   - Nome
   - CNI/CNPJ
   - E-mail
   - Telefone
3. Clique em "Pesquisar" para exibir os resultados
4. Na lista de resultados, clique no ícone de edição (lápis) para modificar o cadastro
5. Atualize as informações necessárias
6. Clique em "Salvar" para confirmar as alterações

#### Visualizar Histórico de Pedidos do Utente

1. Localize o cidadão através da consulta
2. Clique no nome do cidadão para abrir os detalhes
3. Na tela de detalhes, acesse a aba "Histórico de Pedidos"
4. Visualize todos os pedidos associados ao cidadão, com status e datas
5. Clique no número de qualquer pedido para ver seus detalhes completos

### 5.3 Atribuir Pedidos a Técnicos

#### Atribuir um Pedido Individual

1. No menu lateral, clique em "Pedidos" e depois em "Pendentes de Atribuição"
2. Localize o pedido desejado na lista
3. Clique no botão "Atribuir" ao lado do pedido
4. Na janela de atribuição:
   - Selecione o técnico na lista disponível
   - Defina a data prevista para execução
   - Adicione observações, se necessário
5. Clique em "Confirmar" para finalizar a atribuição

![Atribuição de pedido](images/atribuir_pedido.png)

#### Atribuição em Lote

1. No menu lateral, clique em "Pedidos" e depois em "Atribuição em Lote"
2. Utilize os filtros para selecionar os pedidos a serem atribuídos:
   - Tipo de serviço
   - Região
   - Prioridade
3. Marque os pedidos desejados na lista de resultados
4. Clique no botão "Atribuir Selecionados"
5. Na janela de atribuição:
   - Selecione o técnico na lista disponível
   - Defina a data prevista para execução
   - Adicione observações, se necessário
6. Clique em "Confirmar" para finalizar a atribuição em lote

#### Reatribuir um Pedido

1. Localize o pedido que deseja reatribuir através da consulta
2. Clique no número do pedido para abrir os detalhes
3. Na tela de detalhes, clique no botão "Reatribuir"
4. Selecione o novo técnico responsável
5. Informe o motivo da reatribuição
6. Clique em "Confirmar" para finalizar a reatribuição

### 5.4 Acompanhar Status de Pedidos

#### Visualizar Dashboard de Status

1. No menu lateral, clique em "Dashboard"
2. Visualize os gráficos e indicadores de status dos pedidos:
   - Total de pedidos por status
   - Pedidos por tipo de serviço
   - Tempo médio de atendimento
   - Pedidos atrasados
3. Utilize os filtros de período para ajustar a visualização
4. Clique em qualquer gráfico para ver detalhes específicos

![Dashboard de status](images/dashboard_status.png)

#### Consultar Pedidos por Status

1. No menu lateral, clique em "Pedidos" e depois em "Consultar Pedidos"
2. No filtro de status, selecione o status desejado:
   - Aberto
   - Em análise
   - Atribuído
   - Em execução
   - Concluído
   - Cancelado
3. Clique em "Pesquisar" para exibir os resultados
4. Visualize a lista de pedidos no status selecionado

#### Acompanhar Pedidos Atrasados

1. No menu lateral, clique em "Pedidos" e depois em "Pedidos Atrasados"
2. Visualize a lista de pedidos que estão com prazo vencido ou próximos do vencimento
3. Utilize os filtros para refinar a busca:
   - Dias de atraso
   - Tipo de serviço
   - Técnico responsável
4. Clique no número do pedido para ver detalhes e tomar as providências necessárias

### 5.5 Configurar o Sistema

#### Gerenciar Usuários (Perfil Administrador)

1. No menu lateral, clique em "Configurações" e depois em "Usuários"
2. Para adicionar um novo usuário:
   - Clique no botão "Novo Utilizador"
   - Preencha os dados pessoais e de contato
   - Defina o login e senha inicial
   - Selecione o(s) perfil(is) de acesso
   - Clique em "Salvar"
3. Para editar um usuário existente:
   - Localize o usuário na lista
   - Clique no ícone de edição (lápis)
   - Atualize as informações necessárias
   - Clique em "Salvar"
4. Para desativar um usuário:
   - Localize o usuário na lista
   - Clique no ícone de desativação
   - Confirme a ação na janela de confirmação

![Gerenciamento de usuários](images/gerenciar_usuarios.png)

#### Configurar Tipos de Serviço

1. No menu lateral, clique em "Configurações" e depois em "Tipos de Serviço"
2. Para adicionar um novo tipo:
   - Clique no botão "Novo Tipo de Serviço"
   - Informe o nome e a descrição
   - Defina a categoria
   - Configure o SLA (prazo de atendimento)
   - Defina os campos específicos necessários
   - Clique em "Salvar"
3. Para editar um tipo existente:
   - Localize o tipo na lista
   - Clique no ícone de edição (lápis)
   - Atualize as informações necessárias
   - Clique em "Salvar"

#### Configurar Parâmetros do Sistema

1. No menu lateral, clique em "Configurações" e depois em "Parâmetros"
2. Na tela de parâmetros, você pode configurar:
   - Informações da instituição
   - Configurações de e-mail
   - Configurações de notificação
   - Parâmetros de integração
   - Regras de negócio
3. Altere os valores conforme necessário
4. Clique em "Salvar" para aplicar as alterações

---

## 6. Dicas e Melhores Práticas

### Otimizando o Registro de Pedidos

- **Utilize os atalhos de teclado**: Aprenda os atalhos disponíveis para agilizar o registro (Alt+N para novo pedido, Alt+S para salvar, etc.)
- **Mantenha uma base de cidadãos atualizada**: Isso facilita o registro de novos pedidos
- **Use os modelos de descrição**: Para tipos comuns de pedidos, utilize os modelos pré-configurados
- **Anexe evidências visuais**: Fotos e documentos ajudam na compreensão do problema

### Gerenciamento Eficiente de Pedidos

- **Priorize corretamente**: Utilize os critérios estabelecidos para definir a prioridade real dos pedidos
- **Distribua a carga de trabalho**: Evite sobrecarregar alguns técnicos enquanto outros estão ociosos
- **Monitore os prazos**: Acompanhe diariamente os pedidos próximos do vencimento
- **Documente todas as ações**: Registre observações e justificativas para facilitar o acompanhamento

### Comunicação Efetiva

- **Mantenha o cidadão informado**: Utilize as notificações automáticas para informar sobre mudanças de status
- **Use linguagem clara**: Evite termos técnicos nas comunicações com os cidadãos
- **Registre todos os contatos**: Anote no sistema todas as interações com o cidadão
- **Estabeleça expectativas realistas**: Informe prazos factíveis para a resolução dos pedidos

### Uso de Relatórios e Análises

- **Consulte regularmente o dashboard**: Identifique tendências e problemas recorrentes
- **Utilize relatórios para planejamento**: Baseie decisões em dados concretos
- **Analise indicadores de desempenho**: Identifique oportunidades de melhoria
- **Compartilhe insights**: Discuta os dados relevantes com a equipe

---

## 7. Troubleshooting e FAQs

### Problemas Comuns e Soluções

#### O sistema está lento

**Possíveis causas e soluções:**
- **Conexão com a internet**: Verifique sua conexão de internet
- **Navegador desatualizado**: Atualize seu navegador para a versão mais recente
- **Cache do navegador**: Limpe o cache e os cookies do navegador
- **Muitas abas abertas**: Feche abas e aplicativos desnecessários
- **Horário de pico**: Em horários de grande utilização, o sistema pode ficar mais lento

#### Não consigo fazer login

**Possíveis causas e soluções:**
- **Credenciais incorretas**: Verifique se está digitando corretamente usuário e senha
- **Caps Lock ativado**: Verifique se a tecla Caps Lock está desativada
- **Conta bloqueada**: Após várias tentativas incorretas, a conta pode ser bloqueada. Contate o administrador
- **Senha expirada**: Se sua senha expirou, use a opção "Esqueci minha senha"
- **Perfil desativado**: Contate o administrador para verificar o status do seu perfil

#### Não encontro um pedido que deveria estar no sistema

**Possíveis causas e soluções:**
- **Filtros de busca**: Verifique se os filtros de busca estão limitando os resultados
- **Pedido recente**: Pedidos recém-cadastrados podem levar alguns minutos para aparecer em todas as consultas
- **Permissões de acesso**: Verifique se você tem permissão para visualizar o pedido em questão
- **Pedido arquivado**: Verifique na seção de pedidos arquivados

### Perguntas Frequentes (FAQs)

#### Gerais

**Como alterar minha senha?**
1. Clique no seu nome de usuário no canto superior direito
2. Selecione "Meu Perfil"
3. Clique na aba "Segurança"
4. Clique em "Alterar Senha"
5. Informe a senha atual e a nova senha
6. Clique em "Salvar"

**Como configurar notificações?**
1. Clique no seu nome de usuário no canto superior direito
2. Selecione "Meu Perfil"
3. Clique na aba "Notificações"
4. Selecione quais notificações deseja receber e por qual canal (e-mail, sistema)
5. Clique em "Salvar"

**Como exportar dados do sistema?**
1. Acesse a tela que contém os dados que deseja exportar
2. Utilize os filtros para selecionar os dados desejados
3. Clique no botão "Exportar" (geralmente representado por um ícone de download)
4. Selecione o formato desejado (Excel, CSV, PDF)
5. Clique em "Confirmar"

#### Específicas por Perfil

**Atendente: Como cancelar um pedido já registrado?**
1. Localize o pedido através da consulta
2. Abra os detalhes do pedido
3. Clique no botão "Cancelar Pedido"
4. Informe o motivo do cancelamento
5. Clique em "Confirmar"

**Gestor: Como visualizar a produtividade dos técnicos?**
1. No menu lateral, clique em "Relatórios"
2. Selecione "Produtividade por Técnico"
3. Defina o período de análise
4. Clique em "Gerar Relatório"
5. Visualize os indicadores de cada técnico

**Técnico: Como informar impedimentos na execução de um serviço?**
1. Acesse o pedido em questão
2. Clique no botão "Registrar Impedimento"
3. Selecione o tipo de impedimento
4. Descreva detalhadamente a situação
5. Anexe fotos, se necessário
6. Clique em "Salvar"

**Fiscal: Como aprovar um serviço concluído?**
1. No menu lateral, clique em "Pedidos" e depois em "Pendentes de Fiscalização"
2. Selecione o pedido a ser fiscalizado
3. Revise as informações e evidências do serviço executado
4. Preencha o formulário de fiscalização
5. Selecione "Aprovado" no campo de resultado
6. Clique em "Finalizar Fiscalização"

---

## Suporte Técnico

Em caso de dúvidas ou problemas não resolvidos por este manual, entre em contato com a equipe de suporte:

- **E-mail**: suporte.simple@municipio.gov.br
- **Telefone**: (XX) XXXX-XXXX
- **Horário de atendimento**: Segunda a sexta, das 8h às 18h

Ao contatar o suporte, tenha em mãos:
- Seu nome de usuário
- Descrição detalhada do problema
- Capturas de tela do erro (se possível)
- Ações que estava realizando quando o problema ocorreu

---

*Este manual está sujeito a atualizações conforme novas funcionalidades sejam implementadas no sistema.*

*Última atualização: Maio de 2025*