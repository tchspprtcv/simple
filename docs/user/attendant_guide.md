# Guia do Atendente

Este guia foi desenvolvido para auxiliar os atendentes municipais na utilização do sistema Simple. Aqui você encontrará instruções detalhadas sobre como realizar as principais tarefas de atendimento ao cidadão.

## Sumário

1. [Acesso ao Sistema](#acesso-ao-sistema)
2. [Visão Geral da Interface](#visão-geral-da-interface)
3. [Cadastro de Cidadãos](#cadastro-de-cidadãos)
4. [Registro de Novos Pedidos](#registro-de-novos-pedidos)
5. [Pesquisa e Consulta de Pedidos](#pesquisa-e-consulta-de-pedidos)
6. [Gerenciamento de Documentos](#gerenciamento-de-documentos)
7. [Acompanhamento de Processos](#acompanhamento-de-processos)
8. [Gerenciamento de Pagamentos](#gerenciamento-de-pagamentos)
9. [Agendamento de Vistorias](#agendamento-de-vistorias)
10. [Serviços Favoritos](#serviços-favoritos)
11. [Dicas e Boas Práticas](#dicas-e-boas-práticas)

## Acesso ao Sistema

### Login

1. Abra seu navegador e acesse o endereço do sistema (geralmente `http://localhost:3000` ou o endereço fornecido pelo administrador)
2. Na tela de login, insira seu e-mail e senha
3. Clique no botão **Entrar**

> **Nota**: Se for seu primeiro acesso, utilize a senha temporária fornecida pelo administrador. O sistema solicitará a alteração da senha no primeiro login.

### Recuperação de Senha

Caso tenha esquecido sua senha:

1. Na tela de login, clique em **Esqueci minha senha**
2. Insira seu e-mail de cadastro
3. Siga as instruções enviadas para seu e-mail

### Logout

Para sair do sistema com segurança:

1. Clique no seu nome/perfil no canto superior direito
2. Selecione a opção **Sair**

## Visão Geral da Interface

Após o login, você terá acesso ao painel do atendente, que contém:

### Barra Superior
- **Logo do Sistema**: Retorna à página inicial
- **Menu de Navegação**: Acesso às principais funcionalidades
- **Pesquisa Rápida**: Busca por código de acompanhamento
- **Perfil**: Acesso às configurações da conta e logout

### Menu Lateral
- **Dashboard**: Visão geral e estatísticas
- **Novo Pedido**: Registro de novos pedidos
- **Pedidos**: Consulta e gerenciamento de pedidos
- **Cidadãos**: Cadastro e consulta de cidadãos
- **Favoritos**: Acesso rápido aos serviços mais utilizados
- **Relatórios**: Geração de relatórios (se autorizado)

### Área Principal
- Exibe o conteúdo da funcionalidade selecionada
- Contém formulários, listas e detalhes dos processos

## Cadastro de Cidadãos

### Novo Cadastro

1. No menu lateral, clique em **Cidadãos**
2. Clique no botão **Novo Utente**
3. Preencha o formulário com os dados do cidadão:
   - **Nome Completo**: Nome completo do cidadão
   - **Tipo de Documento**: Selecione o tipo (BI, CNI, etc.)
   - **Número do Documento**: Número do documento selecionado
   - **E-mail**: E-mail para contato (opcional)
   - **Telefone**: Telefone para contato
   - **Endereço**: Endereço completo
4. Clique em **Salvar**

### Busca de Cidadãos

1. No menu lateral, clique em **Cidadãos**
2. Utilize o campo de pesquisa para buscar por:
   - Nome
   - Número de documento
   - E-mail
3. Clique no resultado desejado para visualizar os detalhes

### Edição de Cadastro

1. Busque o cidadão conforme instruções acima
2. Na página de detalhes, clique no botão **Editar**
3. Atualize as informações necessárias
4. Clique em **Salvar**

## Registro de Novos Pedidos

### Iniciando um Novo Pedido

1. No menu lateral, clique em **Novo Pedido**
2. Selecione a categoria de serviço desejada
3. Escolha o tipo específico de serviço

### Seleção do Utente

1. Busque o cidadão pelo nome ou número de documento
2. Se o cidadão não estiver cadastrado, clique em **Cadastrar Novo Utente**
3. Preencha os dados do cidadão conforme necessário
4. Clique em **Continuar**

### Preenchimento do Formulário

1. Preencha os campos específicos do tipo de serviço selecionado
2. Os campos obrigatórios são marcados com asterisco (*)
3. Alguns campos podem ter validações específicas
4. Clique em **Continuar**

### Anexo de Documentos

1. Verifique a lista de documentos necessários
2. Para cada documento:
   - Clique em **Selecionar Arquivo**
   - Localize o arquivo no computador
   - Clique em **Abrir**
   - Adicione uma descrição se necessário
3. Clique em **Continuar**

### Confirmação e Finalização

1. Revise todas as informações do pedido
2. Se necessário, clique em **Voltar** para corrigir alguma informação
3. Clique em **Finalizar Pedido**
4. O sistema exibirá o código de acompanhamento
5. Informe o código ao cidadão e/ou imprima o comprovante

## Pesquisa e Consulta de Pedidos

### Busca por Código de Acompanhamento

1. Na barra superior, utilize o campo de pesquisa rápida
2. Digite o código de acompanhamento (formato SMP-AANNNNNN)
3. Pressione Enter ou clique no ícone de busca

### Busca Avançada

1. No menu lateral, clique em **Pedidos**
2. Utilize os filtros disponíveis:
   - **Período**: Data de início e fim
   - **Tipo de Serviço**: Selecione um ou mais tipos
   - **Status**: Selecione um ou mais status
   - **Utente**: Nome ou documento do solicitante
3. Clique em **Buscar**
4. Os resultados serão exibidos em uma lista

### Visualização de Detalhes

1. Na lista de resultados, clique no pedido desejado
2. A página de detalhes exibirá:
   - Informações gerais do pedido
   - Dados do cidadão
   - Histórico de status
   - Documentos anexados
   - Pagamentos associados
   - Vistorias agendadas

## Gerenciamento de Documentos

### Visualização de Documentos

1. Na página de detalhes do pedido, localize a seção **Documentos**
2. Clique no nome do documento para visualizá-lo
3. O documento será aberto em uma nova janela ou baixado, dependendo do tipo

### Anexo de Novos Documentos

1. Na página de detalhes do pedido, localize a seção **Documentos**
2. Clique no botão **Adicionar Documento**
3. Selecione o tipo de documento
4. Clique em **Selecionar Arquivo**
5. Localize o arquivo no computador
6. Clique em **Abrir**
7. Adicione uma descrição se necessário
8. Clique em **Salvar**

### Substituição de Documentos

1. Na página de detalhes do pedido, localize a seção **Documentos**
2. Encontre o documento que deseja substituir
3. Clique no botão **Substituir**
4. Siga o mesmo processo de anexo de documentos

## Acompanhamento de Processos

### Atualização de Status

1. Na página de detalhes do pedido, clique no botão **Atualizar Status**
2. Selecione o novo status na lista suspensa
3. Adicione uma observação explicando a mudança
4. Clique em **Salvar**

### Visualização do Histórico

1. Na página de detalhes do pedido, localize a seção **Histórico**
2. Visualize a lista cronológica de alterações no pedido
3. Cada entrada mostra:
   - Data e hora da alteração
   - Utilizador responsável
   - Status anterior e novo
   - Observações

### Adição de Observações

1. Na página de detalhes do pedido, clique no botão **Adicionar Observação**
2. Digite a observação no campo de texto
3. Clique em **Salvar**
4. A observação será registrada no histórico do pedido

## Gerenciamento de Pagamentos

### Geração de Cobrança

1. Na página de detalhes do pedido, localize a seção **Pagamentos**
2. Clique no botão **Nova Cobrança**
3. Preencha o formulário:
   - **Tipo**: Selecione o tipo de cobrança
   - **Descrição**: Descreva o motivo da cobrança
   - **Valor**: Informe o valor
   - **Data de Vencimento**: Selecione a data
   - **Parcelamento**: Indique se permite parcelamento
   - **Número de Parcelas**: Se parcelado, informe o número de parcelas
4. Clique em **Gerar Cobrança**
5. O sistema gerará o código de barras ou link de pagamento

### Registro de Pagamento

1. Na página de detalhes do pedido, localize a seção **Pagamentos**
2. Encontre o pagamento que deseja registrar
3. Clique no botão **Registrar Pagamento**
4. Preencha o formulário:
   - **Data de Pagamento**: Selecione a data
   - **Comprovante**: Anexe o comprovante de pagamento
5. Clique em **Confirmar**

### Parcelamento

1. Na geração da cobrança, marque a opção **Permitir Parcelamento**
2. Informe o número máximo de parcelas
3. O sistema calculará automaticamente o valor de cada parcela
4. Após gerar a cobrança, o cidadão poderá escolher o número de parcelas desejado

## Agendamento de Vistorias

### Criação de Agendamento

1. Na página de detalhes do pedido, localize a seção **Vistorias**
2. Clique no botão **Agendar Vistoria**
3. Preencha o formulário:
   - **Data e Hora**: Selecione a data e horário
   - **Responsável**: Selecione o fiscal responsável
   - **Observações**: Adicione informações relevantes
4. Clique em **Agendar**

### Consulta de Agendamentos

1. No menu lateral, clique em **Vistorias** (se disponível)
2. Utilize os filtros para buscar vistorias específicas
3. Visualize a lista de vistorias agendadas

### Cancelamento ou Reagendamento

1. Na página de detalhes do pedido, localize a seção **Vistorias**
2. Encontre a vistoria que deseja alterar
3. Clique no botão **Cancelar** ou **Reagendar**
4. Siga as instruções na tela para concluir a operação

## Serviços Favoritos

### Adição de Favoritos

1. No menu lateral, clique em **Favoritos**
2. Clique no botão **Adicionar Favorito**
3. Selecione a categoria e o tipo de serviço
4. Clique em **Adicionar**

### Utilização de Favoritos

1. No menu lateral, clique em **Favoritos**
2. Clique no serviço desejado
3. O sistema iniciará automaticamente um novo pedido daquele tipo

### Reorganização de Favoritos

1. No menu lateral, clique em **Favoritos**
2. Clique no botão **Reorganizar**
3. Arraste os serviços para a ordem desejada
4. Clique em **Salvar**

## Dicas e Boas Práticas

### Atendimento Eficiente

- Mantenha os dados dos cidadãos sempre atualizados
- Verifique se todos os documentos estão legíveis antes de anexá-los
- Informe claramente ao cidadão sobre prazos e documentos necessários
- Utilize a função de favoritos para agilizar o atendimento

### Organização

- Adicione observações claras e objetivas nos processos
- Mantenha o histórico de atendimento atualizado
- Utilize a pesquisa avançada para encontrar processos rapidamente

### Segurança

- Nunca compartilhe sua senha com outros usuários
- Sempre faça logout ao se ausentar do computador
- Verifique cuidadosamente os documentos antes de aprová-los
- Confirme os dados do cidadão antes de iniciar um novo pedido

---

Se você tiver dúvidas adicionais ou encontrar problemas ao utilizar o sistema, entre em contato com o suporte técnico.
