-- Dados de exemplo para a aplicação 'Simple' de gestão de pedidos de serviços municipais
-- Criado em: 06/05/2025

BEGIN;

-- Nota: Os perfis básicos já foram inseridos no schema.sql, mas vamos incluí-los aqui para completude

-- Perfis (já existentes no schema, mas incluídos aqui para referência)
-- INSERT INTO perfis (nome, descricao, permissoes) VALUES
-- ('Administrador', 'Acesso total ao sistema', '{"admin": true, "all": true}'),
-- ('Atendente', 'Atendimento ao público e registro de pedidos', '{"pedidos": {"criar": true, "ler": true, "atualizar": true}, "cidadaos": {"criar": true, "ler": true, "atualizar": true}}'),
-- ('Técnico', 'Análise técnica de processos', '{"pedidos": {"ler": true, "atualizar": true}, "vistorias": {"criar": true, "ler": true, "atualizar": true}}'),
-- ('Fiscal', 'Realização de vistorias', '{"vistorias": {"criar": true, "ler": true, "atualizar": true}}'),
-- ('Gestor', 'Gestão de processos e relatórios', '{"pedidos": {"ler": true, "atualizar": true}, "relatorios": {"ler": true}}');

-- Usuários (funcionários)
INSERT INTO usuarios (id, nome, email, senha, perfil_id, ativo, ultimo_acesso) VALUES
('11111111-1111-1111-1111-111111111111', 'João Silva', 'joao.silva@municipio.gov', crypt('senha123', gen_salt('bf')), 1, true, NOW() - INTERVAL '2 hours'),
('22222222-2222-2222-2222-222222222222', 'Maria Oliveira', 'maria.oliveira@municipio.gov', crypt('senha456', gen_salt('bf')), 2, true, NOW() - INTERVAL '1 day'),
('33333333-3333-3333-3333-333333333333', 'Carlos Santos', 'carlos.santos@municipio.gov', crypt('senha789', gen_salt('bf')), 3, true, NOW() - INTERVAL '3 days'),
('44444444-4444-4444-4444-444444444444', 'Ana Pereira', 'ana.pereira@municipio.gov', crypt('senha321', gen_salt('bf')), 4, true, NOW() - INTERVAL '5 hours'),
('55555555-5555-5555-5555-555555555555', 'Roberto Almeida', 'roberto.almeida@municipio.gov', crypt('senha654', gen_salt('bf')), 5, true, NOW() - INTERVAL '2 days'),
('66666666-6666-6666-6666-666666666666', 'Fernanda Lima', 'fernanda.lima@municipio.gov', crypt('senha987', gen_salt('bf')), 2, true, NOW() - INTERVAL '4 hours'),
('77777777-7777-7777-7777-777777777777', 'Paulo Mendes', 'paulo.mendes@municipio.gov', crypt('senha147', gen_salt('bf')), 3, true, NOW() - INTERVAL '1 week'),
('88888888-8888-8888-8888-888888888888', 'Luciana Costa', 'luciana.costa@municipio.gov', crypt('senha258', gen_salt('bf')), 4, false, NOW() - INTERVAL '3 months');

-- Cidadãos (solicitantes)
INSERT INTO cidadaos (id, nome, tipo_documento, numero_documento, email, telefone, endereco) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'José Ferreira', 'BI', '123456789', 'jose.ferreira@email.com', '999-888-777', 'Rua das Flores, 123, Centro'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Antônia Sousa', 'BI', '987654321', 'antonia.sousa@email.com', '888-777-666', 'Avenida Principal, 456, Bairro Novo'),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Manuel Rodrigues', 'PASSAPORTE', 'AB123456', 'manuel.rodrigues@email.com', '777-666-555', 'Rua dos Girassóis, 789, Jardim'),
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Carla Mendonça', 'BI', '456789123', 'carla.mendonca@email.com', '666-555-444', 'Travessa das Acácias, 321, Vila Nova'),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Francisco Gomes', 'BI', '789123456', 'francisco.gomes@email.com', '555-444-333', 'Rua dos Ipês, 654, Centro'),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Mariana Santos', 'PASSAPORTE', 'CD789012', 'mariana.santos@email.com', '444-333-222', 'Avenida das Palmeiras, 987, Jardim'),
('gggggggg-gggg-gggg-gggg-gggggggggggg', 'Pedro Alves', 'BI', '321654987', 'pedro.alves@email.com', '333-222-111', 'Rua dos Pinheiros, 159, Bairro Alto'),
('hhhhhhhh-hhhh-hhhh-hhhh-hhhhhhhhhhhh', 'Sofia Martins', 'BI', '654987321', 'sofia.martins@email.com', '222-111-000', 'Avenida Central, 753, Centro');

-- Categorias de serviços (já existentes no schema, mas incluídos aqui para referência)
-- INSERT INTO categorias_servicos (nome, descricao, icone, cor, ordem) VALUES
-- ('Legalização', 'Serviços de legalização de imóveis', 'home', '#3498db', 1),
-- ('Lotes', 'Serviços relacionados a lotes', 'map', '#2ecc71', 2),
-- ('Eventos', 'Autorização para eventos', 'calendar', '#e74c3c', 3),
-- ('Financeiro', 'Serviços financeiros', 'money-bill', '#f39c12', 4),
-- ('Licenciamento', 'Licenciamento comercial e de construção', 'building', '#9b59b6', 5),
-- ('Plantas', 'Emissão e atualização de plantas', 'file-alt', '#1abc9c', 6);

-- Tipos de serviços
INSERT INTO tipos_servicos (categoria_id, codigo, nome, descricao, prazo_estimado, valor_base, requer_vistoria, requer_analise_tecnica, requer_aprovacao, disponivel_portal, ativo) VALUES
(1, 'LEG-IMOVEL', 'Legalização de Imóvel', 'Processo para legalização de imóveis irregulares', 30, 250.00, true, true, true, true, true),
(2, 'COMP-LOTE', 'Compra de Lote', 'Processo para aquisição de lotes municipais', 45, 500.00, true, true, true, false, true),
(3, 'AUT-EVENTO', 'Autorização para Evento', 'Autorização para realização de eventos públicos', 15, 100.00, true, false, true, true, true),
(4, 'PAG-PREST', 'Pagamento em Prestação', 'Solicitação de pagamento parcelado de taxas e impostos', 10, 50.00, false, true, true, true, true),
(5, 'MUD-NOME', 'Mudança de Nome', 'Atualização de nome em registros municipais', 5, 30.00, false, false, true, true, true),
(6, 'ATU-PLANTA', 'Atualização de Planta', 'Atualização de planta de imóvel', 20, 150.00, true, true, true, false, true),
(6, 'EMIS-PLANTA', 'Emissão de Planta', 'Emissão de nova planta de imóvel', 15, 200.00, false, true, true, true, true),
(5, 'LIC-COMER', 'Licenciamento Comercial', 'Licença para estabelecimentos comerciais', 25, 300.00, true, true, true, false, true),
(5, 'APROV-PROJ', 'Aprovação de Projetos', 'Aprovação de projetos de construção', 30, 350.00, false, true, true, false, true),
(5, 'LIC-CONST', 'Licença de Construção', 'Licença para construção de imóveis', 20, 250.00, true, true, true, true, true);

-- Etapas de processo (workflow)
INSERT INTO etapas_processo (tipo_servico_id, codigo, nome, descricao, ordem, tempo_estimado, requer_documento, requer_pagamento, requer_aprovacao, perfil_responsavel_id, etapa_anterior_id) VALUES
-- Legalização de Imóvel
(1, 'RECEP', 'Recepção', 'Recepção e verificação inicial de documentos', 1, 2, true, false, false, 2, NULL),
(1, 'ANALIS', 'Análise Documental', 'Análise da documentação apresentada', 2, 24, false, false, true, 3, 1),
(1, 'VIST', 'Vistoria', 'Vistoria no local do imóvel', 3, 8, true, false, false, 4, 2),
(1, 'APROVA', 'Aprovação', 'Aprovação do processo de legalização', 4, 16, false, false, true, 5, 3),
(1, 'PAGTO', 'Pagamento', 'Pagamento das taxas de legalização', 5, 2, true, true, false, 2, 4),
(1, 'EMISS', 'Emissão de Documentos', 'Emissão dos documentos de legalização', 6, 8, false, false, false, 3, 5),

-- Compra de Lote
(2, 'RECEP', 'Recepção', 'Recepção e verificação inicial de documentos', 1, 2, true, false, false, 2, NULL),
(2, 'ANALIS', 'Análise Documental', 'Análise da documentação apresentada', 2, 24, false, false, true, 3, 7),
(2, 'VIST', 'Vistoria', 'Vistoria no local do lote', 3, 8, true, false, false, 4, 8),
(2, 'APROVA', 'Aprovação', 'Aprovação do processo de compra', 4, 16, false, false, true, 5, 9),
(2, 'PAGTO', 'Pagamento', 'Pagamento do lote', 5, 2, true, true, false, 2, 10),
(2, 'EMISS', 'Emissão de Documentos', 'Emissão dos documentos de compra', 6, 8, false, false, false, 3, 11),

-- Autorização para Evento
(3, 'RECEP', 'Recepção', 'Recepção e verificação inicial de documentos', 1, 2, true, false, false, 2, NULL),
(3, 'ANALIS', 'Análise de Viabilidade', 'Análise da viabilidade do evento', 2, 16, false, false, true, 3, 13),
(3, 'VIST', 'Vistoria', 'Vistoria no local do evento', 3, 4, true, false, false, 4, 14),
(3, 'PAGTO', 'Pagamento', 'Pagamento das taxas de autorização', 4, 2, true, true, false, 2, 15),
(3, 'EMISS', 'Emissão de Autorização', 'Emissão da autorização para o evento', 5, 4, false, false, false, 3, 16);

-- Tipos de documentos
INSERT INTO tipos_documentos (codigo, nome, descricao, formato_permitido, tamanho_maximo, obrigatorio, ativo) VALUES
('BI', 'Bilhete de Identidade', 'Documento de identificação pessoal', 'pdf,jpg,png', 5000, true, true),
('COMP-RES', 'Comprovante de Residência', 'Comprovante de endereço residencial', 'pdf,jpg,png', 5000, true, true),
('CERT-NEG', 'Certidão Negativa', 'Certidão negativa de débitos municipais', 'pdf', 2000, true, true),
('ESCRIT', 'Escritura', 'Escritura do imóvel', 'pdf', 10000, true, true),
('PLANTA', 'Planta do Imóvel', 'Planta baixa do imóvel', 'pdf,dwg', 20000, true, true),
('PROJ-ARQ', 'Projeto Arquitetônico', 'Projeto arquitetônico completo', 'pdf,dwg', 30000, true, true),
('COMP-PAG', 'Comprovante de Pagamento', 'Comprovante de pagamento de taxas', 'pdf,jpg,png', 3000, true, true),
('FOTO-IMOVEL', 'Fotos do Imóvel', 'Fotografias atuais do imóvel', 'jpg,png', 10000, true, true),
('PROC', 'Procuração', 'Procuração para representação', 'pdf', 5000, false, true),
('ART', 'ART/RRT', 'Anotação/Registro de Responsabilidade Técnica', 'pdf', 3000, true, true);

-- Documentos necessários por tipo de serviço
INSERT INTO documentos_servico (tipo_servico_id, tipo_documento_id, etapa_processo_id, obrigatorio) VALUES
-- Legalização de Imóvel
(1, 1, 1, true), -- BI na recepção
(1, 2, 1, true), -- Comprovante de Residência na recepção
(1, 3, 1, true), -- Certidão Negativa na recepção
(1, 5, 1, true), -- Planta do Imóvel na recepção
(1, 8, 3, true), -- Fotos do Imóvel na vistoria
(1, 7, 5, true), -- Comprovante de Pagamento no pagamento

-- Compra de Lote
(2, 1, 7, true), -- BI na recepção
(2, 2, 7, true), -- Comprovante de Residência na recepção
(2, 3, 7, true), -- Certidão Negativa na recepção
(2, 7, 11, true), -- Comprovante de Pagamento no pagamento

-- Autorização para Evento
(3, 1, 13, true), -- BI na recepção
(3, 5, 13, true), -- Planta do local na recepção
(3, 7, 16, true), -- Comprovante de Pagamento no pagamento

-- Licenciamento Comercial
(8, 1, NULL, true), -- BI
(8, 2, NULL, true), -- Comprovante de Residência
(8, 3, NULL, true), -- Certidão Negativa
(8, 5, NULL, true), -- Planta do estabelecimento
(8, 6, NULL, true), -- Projeto Arquitetônico
(8, 10, NULL, true); -- ART/RRT

-- Status de pedido (já existentes no schema, mas incluídos aqui para referência)
-- INSERT INTO status_pedido (codigo, nome, descricao, cor, ordem, visivel_portal) VALUES
-- ('NOVO', 'Novo', 'Pedido recém-criado', '#3498db', 1, true),
-- ('EM_ANALISE', 'Em Análise', 'Pedido em análise pelos técnicos', '#f39c12', 2, true),
-- ('AGUARDANDO_DOC', 'Aguardando Documentos', 'Aguardando documentos adicionais', '#e67e22', 3, true),
-- ('AGUARDANDO_PAG', 'Aguardando Pagamento', 'Aguardando pagamento de taxas', '#e74c3c', 4, true),
-- ('AGENDADO', 'Vistoria Agendada', 'Vistoria agendada', '#9b59b6', 5, true),
-- ('EM_VISTORIA', 'Em Vistoria', 'Vistoria em andamento', '#8e44ad', 6, true),
-- ('APROVADO', 'Aprovado', 'Pedido aprovado', '#2ecc71', 7, true),
-- ('REPROVADO', 'Reprovado', 'Pedido reprovado', '#c0392b', 8, true),
-- ('CONCLUIDO', 'Concluído', 'Processo concluído', '#27ae60', 9, true),
-- ('CANCELADO', 'Cancelado', 'Processo cancelado', '#7f8c8d', 10, true);

-- Lotes
INSERT INTO lotes (id, codigo, quadra, numero, area, endereco, bairro, cidade, estado, cep, latitude, longitude, situacao, valor_base) VALUES
('11111111-aaaa-bbbb-cccc-111111111111', 'L001-Q05', 'Q05', '001', 250.00, 'Rua das Acácias, s/n', 'Jardim Primavera', 'Cidade Padrão', 'Estado Padrão', '12345-678', -15.123456, -47.123456, 'DISPONIVEL', 50000.00),
('22222222-aaaa-bbbb-cccc-222222222222', 'L002-Q05', 'Q05', '002', 300.00, 'Rua das Acácias, s/n', 'Jardim Primavera', 'Cidade Padrão', 'Estado Padrão', '12345-678', -15.123457, -47.123457, 'RESERVADO', 60000.00),
('33333333-aaaa-bbbb-cccc-333333333333', 'L003-Q05', 'Q05', '003', 280.00, 'Rua das Acácias, s/n', 'Jardim Primavera', 'Cidade Padrão', 'Estado Padrão', '12345-678', -15.123458, -47.123458, 'VENDIDO', 56000.00),
('44444444-aaaa-bbbb-cccc-444444444444', 'L001-Q06', 'Q06', '001', 320.00, 'Rua dos Ipês, s/n', 'Jardim Primavera', 'Cidade Padrão', 'Estado Padrão', '12345-678', -15.123459, -47.123459, 'DISPONIVEL', 64000.00),
('55555555-aaaa-bbbb-cccc-555555555555', 'L002-Q06', 'Q06', '002', 350.00, 'Rua dos Ipês, s/n', 'Jardim Primavera', 'Cidade Padrão', 'Estado Padrão', '12345-678', -15.123460, -47.123460, 'DISPONIVEL', 70000.00),
('66666666-aaaa-bbbb-cccc-666666666666', 'L001-Q07', 'Q07', '001', 400.00, 'Rua das Palmeiras, s/n', 'Jardim Primavera', 'Cidade Padrão', 'Estado Padrão', '12345-678', -15.123461, -47.123461, 'IRREGULAR', 80000.00);

-- Pedidos
INSERT INTO pedidos (id, codigo_acompanhamento, tipo_servico_id, cidadao_id, usuario_criacao_id, usuario_responsavel_id, etapa_atual_id, status_id, data_inicio, data_previsao, data_conclusao, observacoes, valor_total, origem, prioridade) VALUES
-- Nota: O código de acompanhamento será gerado automaticamente pelo trigger, mas incluímos aqui para clareza
('aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 'SMP-250001', 1, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '22222222-2222-2222-2222-222222222222', '33333333-3333-3333-3333-333333333333', 2, 2, NOW() - INTERVAL '10 days', NOW() + INTERVAL '20 days', NULL, 'Cliente solicitou urgência na análise', 250.00, 'PRESENCIAL', 1),
('bbbbbbbb-1111-2222-3333-bbbbbbbbbbbb', 'SMP-250002', 2, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', '33333333-3333-3333-3333-333333333333', 8, 2, NOW() - INTERVAL '15 days', NOW() + INTERVAL '30 days', NULL, 'Lote com pendências de documentação', 500.00, 'PRESENCIAL', 0),
('cccccccc-1111-2222-3333-cccccccccccc', 'SMP-250003', 3, 'cccccccc-cccc-cccc-cccc-cccccccccccc', '66666666-6666-6666-6666-666666666666', '44444444-4444-4444-4444-444444444444', 15, 5, NOW() - INTERVAL '5 days', NOW() + INTERVAL '10 days', NULL, 'Evento de grande porte, requer vistoria detalhada', 150.00, 'PORTAL', 0),
('dddddddd-1111-2222-3333-dddddddddddd', 'SMP-250004', 4, 'dddddddd-dddd-dddd-dddd-dddddddddddd', '22222222-2222-2222-2222-222222222222', '55555555-5555-5555-5555-555555555555', NULL, 4, NOW() - INTERVAL '3 days', NOW() + INTERVAL '7 days', NULL, 'Solicitação de parcelamento em 5x', 50.00, 'PORTAL', 0),
('eeeeeeee-1111-2222-3333-eeeeeeeeeeee', 'SMP-250005', 5, 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '66666666-6666-6666-6666-666666666666', '22222222-2222-2222-2222-222222222222', NULL, 7, NOW() - INTERVAL '20 days', NOW() - INTERVAL '15 days', NOW() - INTERVAL '15 days', 'Processo aprovado sem pendências', 30.00, 'PRESENCIAL', 0),
('ffffffff-1111-2222-3333-ffffffffffff', 'SMP-250006', 6, 'ffffffff-ffff-ffff-ffff-ffffffffffff', '22222222-2222-2222-2222-222222222222', '77777777-7777-7777-7777-777777777777', NULL, 3, NOW() - INTERVAL '8 days', NOW() + INTERVAL '12 days', NULL, 'Aguardando planta atualizada', 150.00, 'PRESENCIAL', 0),
('gggggggg-1111-2222-3333-gggggggggggg', 'SMP-250007', 7, 'gggggggg-gggg-gggg-gggg-gggggggggggg', '66666666-6666-6666-6666-666666666666', '33333333-3333-3333-3333-333333333333', NULL, 9, NOW() - INTERVAL '30 days', NOW() - INTERVAL '15 days', NOW() - INTERVAL '10 days', 'Planta emitida e entregue ao solicitante', 200.00, 'PORTAL', 0),
('hhhhhhhh-1111-2222-3333-hhhhhhhhhhhh', 'SMP-250008', 8, 'hhhhhhhh-hhhh-hhhh-hhhh-hhhhhhhhhhhh', '22222222-2222-2222-2222-222222222222', '77777777-7777-7777-7777-777777777777', NULL, 6, NOW() - INTERVAL '12 days', NOW() + INTERVAL '13 days', NULL, 'Vistoria em andamento no estabelecimento', 300.00, 'PRESENCIAL', 0),
('iiiiiiii-1111-2222-3333-iiiiiiiiiiii', 'SMP-250009', 9, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '66666666-6666-6666-6666-666666666666', '33333333-3333-3333-3333-333333333333', NULL, 2, NOW() - INTERVAL '7 days', NOW() + INTERVAL '23 days', NULL, 'Projeto em análise pela equipe técnica', 350.00, 'PRESENCIAL', 0),
('jjjjjjjj-1111-2222-3333-jjjjjjjjjjjj', 'SMP-250010', 10, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', '44444444-4444-4444-4444-444444444444', NULL, 8, NOW() - INTERVAL '25 days', NOW() - INTERVAL '5 days', NULL, 'Projeto reprovado por desconformidade com o código de obras', 250.00, 'PORTAL', 0);

-- Histórico de pedidos
INSERT INTO historico_pedidos (pedido_id, etapa_processo_id, status_id, usuario_id, data_ocorrencia, observacao, tempo_execucao) VALUES
('aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 1, 1, '22222222-2222-2222-2222-222222222222', NOW() - INTERVAL '10 days', 'Pedido recebido e registrado', 30),
('aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 2, 2, '33333333-3333-3333-3333-333333333333', NOW() - INTERVAL '8 days', 'Documentação em análise', 120),
('bbbbbbbb-1111-2222-3333-bbbbbbbbbbbb', 7, 1, '22222222-2222-2222-2222-222222222222', NOW() - INTERVAL '15 days', 'Pedido recebido e registrado', 45),
('bbbbbbbb-1111-2222-3333-bbbbbbbbbbbb', 8, 2, '33333333-3333-3333-3333-333333333333', NOW() - INTERVAL '12 days', 'Documentação em análise', 180),
('cccccccc-1111-2222-3333-cccccccccccc', 13, 1, '66666666-6666-6666-6666-666666666666', NOW() - INTERVAL '5 days', 'Pedido recebido e registrado', 25),
('cccccccc-1111-2222-3333-cccccccccccc', 14, 2, '44444444-4444-4444-4444-444444444444', NOW() - INTERVAL '4 days', 'Análise de viabilidade concluída', 240),
('cccccccc-1111-2222-3333-cccccccccccc', 15, 5, '44444444-4444-4444-4444-444444444444', NOW() - INTERVAL '2 days', 'Vistoria agendada para ' || (NOW() + INTERVAL '2 days')::DATE, 30),
('dddddddd-1111-2222-3333-dddddddddddd', NULL, 1, '22222222-2222-2222-2222-222222222222', NOW() - INTERVAL '3 days', 'Pedido recebido e registrado', 20),
('dddddddd-1111-2222-3333-dddddddddddd', NULL, 4, '55555555-5555-5555-5555-555555555555', NOW() - INTERVAL '2 days', 'Aguardando pagamento da primeira parcela', 60),
('eeeeeeee-1111-2222-3333-eeeeeeeeeeee', NULL, 1, '66666666-6666-6666-6666-666666666666', NOW() - INTERVAL '20 days', 'Pedido recebido e registrado', 15),
('eeeeeeee-1111-2222-3333-eeeeeeeeeeee', NULL, 2, '22222222-2222-2222-2222-222222222222', NOW() - INTERVAL '19 days', 'Documentação em análise', 120),
('eeeeeeee-1111-2222-3333-eeeeeeeeeeee', NULL, 7, '22222222-2222-2222-2222-222222222222', NOW() - INTERVAL '15 days', 'Processo aprovado', 30);

-- Documentos anexados aos pedidos
INSERT INTO documentos_pedido (id, pedido_id, tipo_documento_id, etapa_processo_id, usuario_id, nome_arquivo, caminho_arquivo, tamanho_arquivo, tipo_mime, observacao) VALUES
('doc11111-aaaa-bbbb-cccc-111111111111', 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 1, 1, '22222222-2222-2222-2222-222222222222', 'bi_jose.pdf', '/arquivos/2025/05/bi_jose.pdf', 1024, 'application/pdf', 'Documento de identificação'),
('doc22222-aaaa-bbbb-cccc-222222222222', 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 2, 1, '22222222-2222-2222-2222-222222222222', 'comp_res_jose.pdf', '/arquivos/2025/05/comp_res_jose.pdf', 2048, 'application/pdf', 'Comprovante de residência'),
('doc33333-aaaa-bbbb-cccc-333333333333', 'bbbbbbbb-1111-2222-3333-bbbbbbbbbbbb', 1, 7, '22222222-2222-2222-2222-222222222222', 'bi_antonia.pdf', '/arquivos/2025/04/bi_antonia.pdf', 1536, 'application/pdf', 'Documento de identificação'),
('doc44444-aaaa-bbbb-cccc-444444444444', 'cccccccc-1111-2222-3333-cccccccccccc', 1, 13, '66666666-6666-6666-6666-666666666666', 'passaporte_manuel.pdf', '/arquivos/2025/05/passaporte_manuel.pdf', 3072, 'application/pdf', 'Passaporte'),
('doc55555-aaaa-bbbb-cccc-555555555555', 'cccccccc-1111-2222-3333-cccccccccccc', 5, 13, '66666666-6666-6666-6666-666666666666', 'planta_evento.pdf', '/arquivos/2025/05/planta_evento.pdf', 5120, 'application/pdf', 'Planta do local do evento'),
('doc66666-aaaa-bbbb-cccc-666666666666', 'eeeeeeee-1111-2222-3333-eeeeeeeeeeee', 1, NULL, '66666666-6666-6666-6666-666666666666', 'bi_francisco.pdf', '/arquivos/2025/04/bi_francisco.pdf', 1280, 'application/pdf', 'Documento de identificação'),
('doc77777-aaaa-bbbb-cccc-777777777777', 'gggggggg-1111-2222-3333-gggggggggggg', 5, NULL, '66666666-6666-6666-6666-666666666666', 'planta_pedro.dwg', '/arquivos/2025/04/planta_pedro.dwg', 8192, 'application/acad', 'Planta em formato CAD'),
('doc88888-aaaa-bbbb-cccc-888888888888', 'hhhhhhhh-1111-2222-3333-hhhhhhhhhhhh', 6, NULL, '22222222-2222-2222-2222-222222222222', 'projeto_comercial.pdf', '/arquivos/2025/04/projeto_comercial.pdf', 10240, 'application/pdf', 'Projeto arquitetônico completo');

-- Pagamentos
INSERT INTO pagamentos (id, pedido_id, etapa_processo_id, tipo, descricao, valor, data_vencimento, data_pagamento, codigo_barras, status, parcelado, numero_parcelas) VALUES
('pag11111-aaaa-bbbb-cccc-111111111111', 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 5, 'TAXA', 'Taxa de legalização de imóvel', 250.00, CURRENT_DATE + INTERVAL '15 days', NULL, '12345678901234567890123456789012345678901234', 'PENDENTE', false, NULL),
('pag22222-aaaa-bbbb-cccc-222222222222', 'bbbbbbbb-1111-2222-3333-bbbbbbbbbbbb', 11, 'TAXA', 'Taxa de compra de lote', 500.00, CURRENT_DATE + INTERVAL '20 days', NULL, '23456789012345678901234567890123456789012345', 'PENDENTE', false, NULL),
('pag33333-aaaa-bbbb-cccc-333333333333', 'cccccccc-1111-2222-3333-cccccccccccc', 16, 'TAXA', 'Taxa de autorização para evento', 150.00, CURRENT_DATE + INTERVAL '5 days', NULL, '34567890123456789012345678901234567890123456', 'PENDENTE', false, NULL),
('pag44444-aaaa-bbbb-cccc-444444444444', 'dddddddd-1111-2222-3333-dddddddddddd', NULL, 'IMPOSTO', 'Parcelamento de IPTU', 1000.00, CURRENT_DATE + INTERVAL '10 days', NULL, '45678901234567890123456789012345678901234567', 'PENDENTE', true, 5),
('pag55555-aaaa-bbbb-cccc-555555555555', 'eeeeeeee-1111-2222-3333-eeeeeeeeeeee', NULL, 'TAXA', 'Taxa de mudança de nome', 30.00, CURRENT_DATE - INTERVAL '15 days', CURRENT_DATE - INTERVAL '15 days', '56789012345678901234567890123456789012345678', 'PAGO', false, NULL);

-- Parcelas de pagamento
INSERT INTO parcelas_pagamento (id, pagamento_id, numero_parcela, valor, data_vencimento, data_pagamento, codigo_barras, status) VALUES
('par11111-aaaa-bbbb-cccc-111111111111', 'pag44444-aaaa-bbbb-cccc-444444444444', 1, 200.00, CURRENT_DATE + INTERVAL '10 days', NULL, '45678901234567890123456789012345678901234561', 'PENDENTE'),
('par22222-aaaa-bbbb-cccc-222222222222', 'pag44444-aaaa-bbbb-cccc-444444444444', 2, 200.00, CURRENT_DATE + INTERVAL '40 days', NULL, '45678901234567890123456789012345678901234562', 'PENDENTE'),
('par33333-aaaa-bbbb-cccc-333333333333', 'pag44444-aaaa-bbbb-cccc-444444444444', 3, 200.00, CURRENT_DATE + INTERVAL '70 days', NULL, '45678901234567890123456789012345678901234563', 'PENDENTE'),
('par44444-aaaa-bbbb-cccc-444444444444', 'pag44444-aaaa-bbbb-cccc-444444444444', 4, 200.00, CURRENT_DATE + INTERVAL '100 days', NULL, '45678901234567890123456789012345678901234564', 'PENDENTE'),
('par55555-aaaa-bbbb-cccc-555555555555', 'pag44444-aaaa-bbbb-cccc-444444444444', 5, 200.00, CURRENT_DATE + INTERVAL '130 days', NULL, '45678901234567890123456789012345678901234565', 'PENDENTE');

-- Vistorias
INSERT INTO vistorias (id, pedido_id, etapa_processo_id, usuario_responsavel_id, data_agendada, data_realizada, status, resultado, observacoes, latitude, longitude) VALUES
('vis11111-aaaa-bbbb-cccc-111111111111', 'cccccccc-1111-2222-3333-cccccccccccc', 15, '44444444-4444-4444-4444-444444444444', CURRENT_DATE + INTERVAL '2 days', NULL, 'AGENDADA', NULL, 'Vistoria para verificar condições do local do evento', -15.123456, -47.123456),
('vis22222-aaaa-bbbb-cccc-222222222222', 'hhhhhhhh-1111-2222-3333-hhhhhhhhhhhh', NULL, '44444444-4444-4444-4444-444444444444', CURRENT_DATE - INTERVAL '1 day', CURRENT_DATE - INTERVAL '1 day', 'REALIZADA', 'APROVADA', 'Estabelecimento atende às normas de segurança', -15.234567, -47.234567),
('vis33333-aaaa-bbbb-cccc-333333333333', 'jjjjjjjj-1111-2222-3333-jjjjjjjjjjjj', NULL, '44444444-4444-4444-4444-444444444444', CURRENT_DATE - INTERVAL '5 days', CURRENT_DATE - INTERVAL '5 days', 'REALIZADA', 'REPROVADA', 'Construção em desacordo com o projeto aprovado', -15.345678, -47.345678);

-- Fotos de vistoria
INSERT INTO fotos_vistoria (id, vistoria_id, nome_arquivo, caminho_arquivo, tamanho_arquivo, tipo_mime, descricao, latitude, longitude, data_captura) VALUES
('fot11111-aaaa-bbbb-cccc-111111111111', 'vis22222-aaaa-bbbb-cccc-222222222222', 'foto_frente.jpg', '/arquivos/2025/05/vistorias/foto_frente.jpg', 2048, 'image/jpeg', 'Fachada do estabelecimento', -15.234567, -47.234567, CURRENT_DATE - INTERVAL '1 day'),
('fot22222-aaaa-bbbb-cccc-222222222222', 'vis22222-aaaa-bbbb-cccc-222222222222', 'foto_interior.jpg', '/arquivos/2025/05/vistorias/foto_interior.jpg', 1536, 'image/jpeg', 'Interior do estabelecimento', -15.234567, -47.234567, CURRENT_DATE - INTERVAL '1 day'),
('fot33333-aaaa-bbbb-cccc-333333333333', 'vis33333-aaaa-bbbb-cccc-333333333333', 'foto_irregularidade.jpg', '/arquivos/2025/05/vistorias/foto_irregularidade.jpg', 2048, 'image/jpeg', 'Irregularidade na construção', -15.345678, -47.345678, CURRENT_DATE - INTERVAL '5 days');

-- Pedidos de lotes
INSERT INTO pedidos_lotes (pedido_id, lote_id, tipo_relacao) VALUES
('bbbbbbbb-1111-2222-3333-bbbbbbbbbbbb', '11111111-aaaa-bbbb-cccc-111111111111', 'COMPRA'),
('aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', '66666666-aaaa-bbbb-cccc-666666666666', 'LEGALIZACAO');

-- Plantas emitidas
INSERT INTO plantas_emitidas (id, pedido_id, lote_id, tipo, numero_documento, data_emissao, data_validade, usuario_emissor_id, documento_id, observacoes) VALUES
('pla11111-aaaa-bbbb-cccc-111111111111', 'gggggggg-1111-2222-3333-gggggggggggg', NULL, 'PLANTA', 'PL-2025-0001', CURRENT_DATE - INTERVAL '10 days', CURRENT_DATE + INTERVAL '5 years', '33333333-3333-3333-3333-333333333333', 'doc77777-aaaa-bbbb-cccc-777777777777', 'Planta emitida conforme solicitado');

-- Eventos
INSERT INTO eventos (id, pedido_id, nome, descricao, data_inicio, data_fim, local, capacidade, tipo_evento, responsavel, contato_responsavel, status, observacoes) VALUES
('evt11111-aaaa-bbbb-cccc-111111111111', 'cccccccc-1111-2222-3333-cccccccccccc', 'Festival de Música', 'Festival de música ao ar livre', CURRENT_DATE + INTERVAL '10 days', CURRENT_DATE + INTERVAL '12 days', 'Praça Central', 5000, 'CULTURAL', 'Manuel Rodrigues', '777-666-555', 'SOLICITADO', 'Evento de grande porte, requer segurança reforçada');

-- Favoritos dos usuários
INSERT INTO favoritos (usuario_id, tipo_servico_id, ordem) VALUES
('22222222-2222-2222-2222-222222222222', 1, 1),
('22222222-2222-2222-2222-222222222222', 2, 2),
('22222222-2222-2222-2222-222222222222', 5, 3),
('66666666-6666-6666-6666-666666666666', 3, 1),
('66666666-6666-6666-6666-666666666666', 7, 2),
('33333333-3333-3333-3333-333333333333', 1, 1),
('33333333-3333-3333-3333-333333333333', 6, 2),
('33333333-3333-3333-3333-333333333333', 9, 3);

-- Notificações
INSERT INTO notificacoes (id, usuario_id, cidadao_id, pedido_id, titulo, mensagem, tipo, lida, data_leitura, enviada_email, enviada_sms) VALUES
('not11111-aaaa-bbbb-cccc-111111111111', '33333333-3333-3333-3333-333333333333', NULL, 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 'Novo pedido atribuído', 'Você recebeu um novo pedido de legalização para análise', 'INFO', true, CURRENT_DATE - INTERVAL '8 days', true, false),
('not22222-aaaa-bbbb-cccc-222222222222', NULL, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', 'Pedido em análise', 'Seu pedido de legalização está em análise pela equipe técnica', 'INFO', false, NULL, true, true),
('not33333-aaaa-bbbb-cccc-333333333333', '44444444-4444-4444-4444-444444444444', NULL, 'cccccccc-1111-2222-3333-cccccccccccc', 'Vistoria agendada', 'Vistoria agendada para o evento Festival de Música', 'INFO', true, CURRENT_DATE - INTERVAL '1 day', true, false),
('not44444-aaaa-bbbb-cccc-444444444444', NULL, 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'dddddddd-1111-2222-3333-dddddddddddd', 'Pagamento pendente', 'Sua solicitação está aguardando pagamento', 'ALERTA', false, NULL, true, true),
('not55555-aaaa-bbbb-cccc-555555555555', NULL, 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'eeeeeeee-1111-2222-3333-eeeeeeeeeeee', 'Processo concluído', 'Seu processo de mudança de nome foi concluído com sucesso', 'SUCESSO', true, CURRENT_DATE - INTERVAL '14 days', true, true);

-- Configurações do sistema
INSERT INTO configuracoes (chave, valor, descricao, tipo, grupo, editavel) VALUES
('sistema.nome', 'Simple - Gestão de Serviços Municipais', 'Nome do sistema', 'STRING', 'SISTEMA', false),
('sistema.versao', '1.0.0', 'Versão atual do sistema', 'STRING', 'SISTEMA', false),
('email.servidor', 'smtp.municipio.gov', 'Servidor SMTP para envio de emails', 'STRING', 'EMAIL', true),
('email.porta', '587', 'Porta do servidor SMTP', 'INTEGER', 'EMAIL', true),
('email.usuario', 'notificacoes@municipio.gov', 'Usuário para autenticação SMTP', 'STRING', 'EMAIL', true),
('prazo.padrao', '30', 'Prazo padrão em dias para conclusão de processos', 'INTEGER', 'PRAZOS', true),
('notificacao.email', 'true', 'Habilitar notificações por email', 'BOOLEAN', 'NOTIFICACOES', true),
('notificacao.sms', 'true', 'Habilitar notificações por SMS', 'BOOLEAN', 'NOTIFICACOES', true),
('portal.url', 'https://servicos.municipio.gov', 'URL do portal de serviços', 'STRING', 'PORTAL', true),
('vistoria.prazo_minimo', '2', 'Prazo mínimo em dias para agendamento de vistorias', 'INTEGER', 'VISTORIAS', true);

-- Logs do sistema
INSERT INTO logs_sistema (usuario_id, acao, tabela, registro_id, dados, ip, user_agent) VALUES
('11111111-1111-1111-1111-111111111111', 'LOGIN', NULL, NULL, '{"sucesso": true}', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'),
('22222222-2222-2222-2222-222222222222', 'CRIAR', 'pedidos', 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', '{"tipo_servico_id": 1, "cidadao_id": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"}', '192.168.1.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'),
('33333333-3333-3333-3333-333333333333', 'ATUALIZAR', 'pedidos', 'aaaaaaaa-1111-2222-3333-aaaaaaaaaaaa', '{"status_id": 2, "etapa_atual_id": 2}', '192.168.1.102', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Safari/605.1.15'),
('44444444-4444-4444-4444-444444444444', 'CRIAR', 'vistorias', 'vis11111-aaaa-bbbb-cccc-111111111111', '{"pedido_id": "cccccccc-1111-2222-3333-cccccccccccc", "data_agendada": "' || (CURRENT_DATE + INTERVAL '2 days')::TEXT || '"}', '192.168.1.103', 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'),
('55555555-5555-5555-5555-555555555555', 'ATUALIZAR', 'pedidos', 'eeeeeeee-1111-2222-3333-eeeeeeeeeeee', '{"status_id": 7}', '192.168.1.104', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'),
('66666666-6666-6666-6666-666666666666', 'CRIAR', 'pedidos', 'gggggggg-1111-2222-3333-gggggggggggg', '{"tipo_servico_id": 7, "cidadao_id": "gggggggg-gggg-gggg-gggg-gggggggggggg"}', '192.168.1.105', 'Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1'),
('77777777-7777-7777-7777-777777777777', 'ATUALIZAR', 'pedidos', 'jjjjjjjj-1111-2222-3333-jjjjjjjjjjjj', '{"status_id": 8}', '192.168.1.106', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'),
('11111111-1111-1111-1111-111111111111', 'LOGOUT', NULL, NULL, '{"sucesso": true}', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36');

COMMIT;
