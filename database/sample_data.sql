-- Arquivo de dados de exemplo válidos para o banco de dados 'Simple'
-- Criado em: 07/05/2025
-- Corrigido em: 07/05/2025 - Correção de violações de chave estrangeira

BEGIN;

-- Inserção de dados em perfis (já existem no schema original, mas vamos garantir)
INSERT INTO perfis (nome, descricao, permissoes) VALUES
('Administrador', 'Acesso total ao sistema', '{"admin": true, "all": true}'),
('Atendente', 'Atendimento ao público e registro de pedidos', '{"pedidos": {"criar": true, "ler": true, "atualizar": true}, "cidadaos": {"criar": true, "ler": true, "atualizar": true}}'),
('Técnico', 'Análise técnica de processos', '{"pedidos": {"ler": true, "atualizar": true}, "vistorias": {"criar": true, "ler": true, "atualizar": true}}'),
('Fiscal', 'Realização de vistorias', '{"vistorias": {"criar": true, "ler": true, "atualizar": true}}'),
('Gestor', 'Gestão de processos e relatórios', '{"pedidos": {"ler": true, "atualizar": true}, "relatorios": {"ler": true}}')
ON CONFLICT (nome) DO NOTHING;

-- Inserção de dados em status_pedido (já existem no schema original, mas vamos garantir)
INSERT INTO status_pedido (codigo, nome, descricao, cor, ordem, visivel_portal) VALUES
('NOVO', 'Novo', 'Pedido recém-criado', '#3498db', 1, true),
('EM_ANALISE', 'Em Análise', 'Pedido em análise pelos técnicos', '#f39c12', 2, true),
('AGUARDANDO_DOC', 'Aguardando Documentos', 'Aguardando documentos adicionais', '#e67e22', 3, true),
('AGUARDANDO_PAG', 'Aguardando Pagamento', 'Aguardando pagamento de taxas', '#e74c3c', 4, true),
('AGENDADO', 'Vistoria Agendada', 'Vistoria agendada', '#9b59b6', 5, true),
('EM_VISTORIA', 'Em Vistoria', 'Vistoria em andamento', '#8e44ad', 6, true),
('APROVADO', 'Aprovado', 'Pedido aprovado', '#2ecc71', 7, true),
('REPROVADO', 'Reprovado', 'Pedido reprovado', '#c0392b', 8, true),
('CONCLUIDO', 'Concluído', 'Processo concluído', '#27ae60', 9, true),
('CANCELADO', 'Cancelado', 'Processo cancelado', '#7f8c8d', 10, true)
ON CONFLICT (codigo) DO NOTHING;

-- Inserção de dados em categorias_servicos (já existem no schema original, mas vamos garantir)
INSERT INTO categorias_servicos (nome, descricao, icone, cor, ordem) VALUES
('Legalização', 'Serviços de legalização de imóveis', 'home', '#3498db', 1),
('Lotes', 'Serviços relacionados a lotes', 'map', '#2ecc71', 2),
('Eventos', 'Autorização para eventos', 'calendar', '#e74c3c', 3),
('Financeiro', 'Serviços financeiros', 'money-bill', '#f39c12', 4),
('Licenciamento', 'Licenciamento comercial e de construção', 'building', '#9b59b6', 5),
('Plantas', 'Emissão e atualização de plantas', 'file-alt', '#1abc9c', 6)
ON CONFLICT (nome) DO NOTHING;

-- Inserção de dados em tipos_servicos
INSERT INTO tipos_servicos (categoria_id, codigo, nome, descricao, prazo_estimado, valor_base, requer_vistoria, requer_analise_tecnica, requer_aprovacao, disponivel_portal, ativo) VALUES
(1, 'LEG-IMOVEL', 'Legalização de Imóvel', 'Processo para legalização de imóveis irregulares', 30, 250.00, true, true, true, true, true),
(2, 'COMP-LOTE', 'Compra de Lote', 'Processo para aquisição de lotes municipais', 45, 500.00, true, true, true, false, true),
(3, 'AUT-EVENTO', 'Autorização para Evento', 'Autorização para realização de eventos públicos', 15, 150.00, true, false, true, true, true),
(5, 'LIC-COMERC', 'Licença Comercial', 'Licenciamento para estabelecimentos comerciais', 20, 200.00, true, true, true, true, true),
(6, 'EMIS-PLANTA', 'Emissão de Planta', 'Emissão de planta cadastral do imóvel', 10, 100.00, false, true, true, true, true);

-- Inserção de dados em tipos_documentos
INSERT INTO tipos_documentos (codigo, nome, descricao, formato_permitido, tamanho_maximo, obrigatorio, ativo) VALUES
('BI', 'BI', 'Bilhete de Identidade', 'pdf,jpg,png', 5000, true, true),
('CNI', 'CNI', 'Cartão Nacional de Identificação', 'pdf,jpg,png', 5000, true, true),
('TRE', 'TRE', 'Titulo de Residencia de Estrangeiros', 'pdf,jpg,png', 5000, true, true),
('ESCR-IMOVEL', 'Escritura do Imóvel', 'Escritura ou contrato de compra e venda', 'pdf', 10000, true, true),
('PROJ-ARQUIT', 'Projeto Arquitetônico', 'Projeto arquitetônico do imóvel', 'pdf', 20000, false, true),
('COMP-PAG', 'Comprovante de Pagamento', 'Comprovante de pagamento de taxas', 'pdf,jpg,png', 5000, true, true);

-- Inserção de dados em etapas_processo
INSERT INTO etapas_processo (tipo_servico_id, codigo, nome, descricao, ordem, tempo_estimado, requer_documento, requer_pagamento, requer_aprovacao, perfil_responsavel_id) VALUES
(1, 'ANALISE-DOC', 'Análise Documental', 'Análise dos documentos apresentados', 1, 24, true, false, true, 3),
(1, 'VISTORIA', 'Vistoria Técnica', 'Vistoria no local do imóvel', 2, 48, false, false, true, 4),
(1, 'PAGAMENTO', 'Pagamento de Taxas', 'Pagamento das taxas de legalização', 3, 24, false, true, false, 2),
(1, 'EMISSAO-DOC', 'Emissão de Documentos', 'Emissão da documentação final', 4, 48, false, false, true, 3),
(2, 'ANALISE-CADAS', 'Análise Cadastral', 'Análise dos dados cadastrais do solicitante', 1, 24, true, false, true, 3),
(2, 'VISTORIA-LOTE', 'Vistoria do Lote', 'Vistoria no lote a ser adquirido', 2, 48, false, false, true, 4),
(2, 'AVALIACAO', 'Avaliação do Imóvel', 'Avaliação do valor do lote', 3, 72, false, false, true, 3),
(2, 'CONTRATO', 'Elaboração de Contrato', 'Elaboração do contrato de compra e venda', 4, 48, false, true, true, 5),
(3, 'ANALISE-EVENTO', 'Análise do Evento', 'Análise do tipo e porte do evento', 1, 24, true, false, true, 3),
(3, 'VISTORIA-LOCAL', 'Vistoria do Local', 'Vistoria no local do evento', 2, 24, false, false, true, 4),
(3, 'PAGAMENTO-TAXA', 'Pagamento de Taxa', 'Pagamento da taxa de autorização', 3, 24, false, true, false, 2),
(3, 'EMISSAO-AUT', 'Emissão de Autorização', 'Emissão da autorização para o evento', 4, 24, false, false, true, 5);

-- Inserção de dados em documentos_servico
INSERT INTO documentos_servico (tipo_servico_id, tipo_documento_id, etapa_processo_id, obrigatorio) VALUES
(1, 1, 1, true),  -- Documento de Identidade para Legalização de Imóvel
(1, 2, 1, true),  -- CPF para Legalização de Imóvel
(1, 3, 1, true),  -- Comprovante de Residência para Legalização de Imóvel
(1, 4, 1, true),  -- Escritura do Imóvel para Legalização de Imóvel
(1, 5, 1, false), -- Projeto Arquitetônico para Legalização de Imóvel
(1, 6, 3, true),  -- Comprovante de Pagamento para Legalização de Imóvel
(2, 1, 5, true),  -- Documento de Identidade para Compra de Lote
(2, 2, 5, true),  -- CPF para Compra de Lote
(2, 3, 5, true),  -- Comprovante de Residência para Compra de Lote
(2, 6, 8, true),  -- Comprovante de Pagamento para Compra de Lote
(3, 1, 9, true),  -- Documento de Identidade para Autorização para Evento
(3, 2, 9, true),  -- CPF para Autorização para Evento
(3, 6, 11, true); -- Comprovante de Pagamento para Autorização para Evento

-- Inserção de dados em usuarios (usando UUIDs válidos)
INSERT INTO usuarios (id, nome, email, senha, perfil_id, ativo, ultimo_acesso) VALUES
('ecbed590-5882-4c2d-a47c-9009e0c87fc6', 'João Silva', 'joao.silva@simple.cv', crypt('senha123', gen_salt('bf')), 1, true, CURRENT_TIMESTAMP - INTERVAL '2 days'),
('e639c09b-72d0-468b-a296-634187bae05f', 'Maria Oliveira', 'maria.oliveira@simple.cv', crypt('senha123', gen_salt('bf')), 2, true, CURRENT_TIMESTAMP - INTERVAL '1 day'),
('61eed75a-32dd-452c-a160-e79f44cb7778', 'Carlos Santos', 'carlos.santos@simple.cv', crypt('senha123', gen_salt('bf')), 3, true, CURRENT_TIMESTAMP - INTERVAL '3 hours'),
('14174f2b-78d4-43e0-94c7-1c90345773cf', 'Ana Pereira', 'ana.pereira@simple.cv', crypt('senha123', gen_salt('bf')), 4, true, CURRENT_TIMESTAMP - INTERVAL '5 hours'),
('31ecc117-43a8-4aa2-ba95-f61601b4a92e', 'Roberto Almeida', 'roberto.almeida@simple.cv', crypt('senha123', gen_salt('bf')), 5, true, CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- Inserção de dados em cidadaos (usando UUIDs válidos)
INSERT INTO cidadaos (id, nome, tipo_documento, numero_documento, email, telefone, endereco) VALUES
('eb9a0fb9-87ce-435e-849d-f507cfeaa490', 'Pedro Souza', 'BI', '123456', 'pedro.souza@email.com', '(11) 98765-4321', 'Rua das Flores, 123, Centro'),
('34099715-7083-4ca1-bce1-3e441bb15ede', 'Lucia Ferreira', 'CNI', '12345679M001C', 'lucia.ferreira@email.com', '(11) 91234-5678', 'Av. Principal, 456, Jardim Europa'),
('1e439083-e39b-4471-b8b0-3c9baf9d184c', 'Marcos Ribeiro', 'PASSPORTE', 'P0123456', 'marcos.ribeiro@email.com', '(11) 92345-6789', 'Rua dos Pinheiros, 789, Vila Nova');

-- Inserção de dados em lotes (usando UUIDs válidos)
INSERT INTO lotes (id, codigo, quadra, numero, area, endereco, bairro, cidade, estado, cep, latitude, longitude, situacao, valor_base) VALUES
('3ff1375b-393c-4a7f-9d72-e14317e79527', 'LT-A123', 'A', '123', 250.00, 'Rua das Palmeiras, s/n', 'Jardim América', 'Cidade Padrão', 'Estado Padrão', '12345-678', -23.5505, -46.6333, 'DISPONIVEL', 75000.00),
('ec61cc13-d659-42fe-9a4e-942e66cdd89d', 'LT-B456', 'B', '456', 300.00, 'Av. dos Ipês, s/n', 'Vila Verde', 'Cidade Padrão', 'Estado Padrão', '12345-679', -23.5605, -46.6433, 'DISPONIVEL', 90000.00),
('20592c4e-72df-42bf-b4eb-1fd986f1458e', 'LT-C789', 'C', '789', 200.00, 'Rua dos Jacarandás, s/n', 'Parque das Árvores', 'Cidade Padrão', 'Estado Padrão', '12345-680', -23.5705, -46.6533, 'RESERVADO', 60000.00);

-- Inserção de dados em pedidos (usando UUIDs válidos)
-- Nota: O código_acompanhamento será gerado automaticamente pelo trigger
INSERT INTO pedidos (id, tipo_servico_id, cidadao_id, usuario_criacao_id, usuario_responsavel_id, etapa_atual_id, status_id, data_inicio, data_previsao, origem, prioridade, codigo_acompanhamento) VALUES
('929efadd-9503-4137-91a9-abd3761c2946', 1, 'eb9a0fb9-87ce-435e-849d-f507cfeaa490', 'e639c09b-72d0-468b-a296-634187bae05f', '61eed75a-32dd-452c-a160-e79f44cb7778', 1, 2, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP + INTERVAL '20 days', 'PRESENCIAL', 0, 'SMP-250001'),
('bf047097-0a35-4a1f-a3f1-eea149dc868a', 2, '34099715-7083-4ca1-bce1-3e441bb15ede', 'e639c09b-72d0-468b-a296-634187bae05f', '61eed75a-32dd-452c-a160-e79f44cb7778', 5, 2, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP + INTERVAL '40 days', 'PORTAL', 0, 'SMP-250002'),
('765f57da-eb24-4a5e-8ea0-cfa6f7b6ad6e', 3, '1e439083-e39b-4471-b8b0-3c9baf9d184c', 'e639c09b-72d0-468b-a296-634187bae05f', '14174f2b-78d4-43e0-94c7-1c90345773cf', 9, 3, CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP + INTERVAL '12 days', 'PRESENCIAL', 1, 'SMP-250003');

-- Inserção de dados em historico_pedidos
INSERT INTO historico_pedidos (pedido_id, etapa_processo_id, status_id, usuario_id, observacao, dados_anteriores, dados_novos) VALUES
('929efadd-9503-4137-91a9-abd3761c2946', 1, 1, 'e639c09b-72d0-468b-a296-634187bae05f', 'Pedido criado', '{}', '{"status": "NOVO"}'),
('929efadd-9503-4137-91a9-abd3761c2946', 1, 2, '61eed75a-32dd-452c-a160-e79f44cb7778', 'Iniciada análise documental', '{"status": "NOVO"}', '{"status": "EM_ANALISE"}'),
('bf047097-0a35-4a1f-a3f1-eea149dc868a', 5, 1, 'e639c09b-72d0-468b-a296-634187bae05f', 'Pedido criado', '{}', '{"status": "NOVO"}'),
('bf047097-0a35-4a1f-a3f1-eea149dc868a', 5, 2, '61eed75a-32dd-452c-a160-e79f44cb7778', 'Iniciada análise cadastral', '{"status": "NOVO"}', '{"status": "EM_ANALISE"}'),
('765f57da-eb24-4a5e-8ea0-cfa6f7b6ad6e', 9, 1, 'e639c09b-72d0-468b-a296-634187bae05f', 'Pedido criado', '{}', '{"status": "NOVO"}'),
('765f57da-eb24-4a5e-8ea0-cfa6f7b6ad6e', 9, 2, '14174f2b-78d4-43e0-94c7-1c90345773cf', 'Iniciada análise do evento', '{"status": "NOVO"}', '{"status": "EM_ANALISE"}'),
('765f57da-eb24-4a5e-8ea0-cfa6f7b6ad6e', 9, 3, '14174f2b-78d4-43e0-94c7-1c90345773cf', 'Aguardando documentação adicional', '{"status": "EM_ANALISE"}', '{"status": "AGUARDANDO_DOC"}');

-- Inserção de dados em documentos_pedido (usando UUIDs válidos)
-- CORREÇÃO: Substituído o UUID do utente pelo UUID de um utilizador válido na tabela usuarios
INSERT INTO documentos_pedido (id, pedido_id, tipo_documento_id, etapa_processo_id, usuario_id, nome_arquivo, caminho_arquivo, tamanho_arquivo, tipo_mime, hash_arquivo) VALUES
('64e8df7d-83b5-47f3-9632-c8610b9409d2', '929efadd-9503-4137-91a9-abd3761c2946', 1, 1, 'e639c09b-72d0-468b-a296-634187bae05f', 'rg_pedro.pdf', '/arquivos/documentos/2025/05/rg_pedro.pdf', 1024, 'application/pdf', 'a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0'),
('c140bbe9-4d90-41c1-9912-e5fbb7f2593e', '929efadd-9503-4137-91a9-abd3761c2946', 2, 1, 'e639c09b-72d0-468b-a296-634187bae05f', 'cpf_pedro.pdf', '/arquivos/documentos/2025/05/cpf_pedro.pdf', 512, 'application/pdf', 'b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1'),
('ffc22468-e515-4147-8d18-84f07fe0498e', '929efadd-9503-4137-91a9-abd3761c2946', 3, 1, 'e639c09b-72d0-468b-a296-634187bae05f', 'comprovante_residencia_pedro.pdf', '/arquivos/documentos/2025/05/comprovante_residencia_pedro.pdf', 768, 'application/pdf', 'c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2'),
('3ec772a2-2011-4a57-a643-1f38d0ed32cf', 'bf047097-0a35-4a1f-a3f1-eea149dc868a', 1, 5, 'e639c09b-72d0-468b-a296-634187bae05f', 'rg_lucia.pdf', '/arquivos/documentos/2025/05/rg_lucia.pdf', 1024, 'application/pdf', 'd4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3'),
('bb69a076-665f-47a7-8d0e-323d59ed8629', 'bf047097-0a35-4a1f-a3f1-eea149dc868a', 2, 5, 'e639c09b-72d0-468b-a296-634187bae05f', 'cpf_lucia.pdf', '/arquivos/documentos/2025/05/cpf_lucia.pdf', 512, 'application/pdf', 'e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4');

-- Inserção de dados em pedidos_lotes
INSERT INTO pedidos_lotes (pedido_id, lote_id, tipo_relacao) VALUES
('bf047097-0a35-4a1f-a3f1-eea149dc868a', '3ff1375b-393c-4a7f-9d72-e14317e79527', 'COMPRA');

-- Inserção de dados em pagamentos (usando UUIDs válidos)
INSERT INTO pagamentos (id, pedido_id, etapa_processo_id, tipo, descricao, valor, data_vencimento, status) VALUES
('2df5aa7b-7023-447e-ae8a-55ccb42a717d', '929efadd-9503-4137-91a9-abd3761c2946', 3, 'TAXA', 'Taxa de legalização de imóvel', 250.00, CURRENT_DATE + INTERVAL '15 days', 'PENDENTE');

-- Inserção de dados em vistorias (usando UUIDs válidos)
INSERT INTO vistorias (id, pedido_id, etapa_processo_id, usuario_responsavel_id, data_agendada, status) VALUES
('14174f2b-78d4-43e0-94c7-1c90345773cf', '929efadd-9503-4137-91a9-abd3761c2946', 2, '14174f2b-78d4-43e0-94c7-1c90345773cf', CURRENT_TIMESTAMP + INTERVAL '5 days', 'AGENDADA');

-- Inserção de dados em eventos (usando UUIDs válidos)
INSERT INTO eventos (id, pedido_id, nome, descricao, data_inicio, data_fim, local, capacidade, tipo_evento, responsavel, contato_responsavel, status) VALUES
('31ecc117-43a8-4aa2-ba95-f61601b4a92e', '765f57da-eb24-4a5e-8ea0-cfa6f7b6ad6e', 'Festa Junina Comunitária', 'Evento tradicional de festa junina para a comunidade', CURRENT_TIMESTAMP + INTERVAL '20 days', CURRENT_TIMESTAMP + INTERVAL '20 days' + INTERVAL '8 hours', 'Praça Central', 500, 'CULTURAL', 'Marcos Ribeiro', '(11) 92345-6789', 'SOLICITADO');

-- Inserção de dados em notificacoes (usando UUIDs válidos)
-- CORREÇÃO: Substituído os UUIDs de utilizadores por novos UUIDs para as notificações
INSERT INTO notificacoes (id, usuario_id, cidadao_id, pedido_id, titulo, mensagem, tipo, lida) VALUES
('a1b2c3d4-e5f6-47a7-8d0e-323d59ed8629', '61eed75a-32dd-452c-a160-e79f44cb7778', NULL, '929efadd-9503-4137-91a9-abd3761c2946', 'Novo pedido para análise', 'Você tem um novo pedido de legalização de imóvel para analisar', 'INFO', false);

-- Inserção de dados em configuracoes
INSERT INTO configuracoes (chave, valor, descricao, tipo, grupo, editavel) VALUES
('SISTEMA_NOME', 'Simple - Sistema Municipal de Pedidos', 'Nome do sistema', 'STRING', 'GERAL', false),
('EMAIL_REMETENTE', 'noreply@simple.cv', 'Email remetente para notificações', 'STRING', 'EMAIL', true),
('PRAZO_PADRAO', '30', 'Prazo padrão em dias para conclusão de pedidos', 'INTEGER', 'PEDIDOS', true),
('NOTIFICACAO_EMAIL', 'true', 'Habilitar notificações por email', 'BOOLEAN', 'NOTIFICACOES', true);

COMMIT;
