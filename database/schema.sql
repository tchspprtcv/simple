-- Schema para a aplicação 'Simple' de gestão de pedidos de serviços municipais
-- Criado em: 02/05/2025

-- Configurações iniciais
SET client_encoding = 'UTF8';

-- Extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Tabela de perfis de utilizador
CREATE TABLE perfis (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao TEXT,
    permissoes JSONB NOT NULL DEFAULT '{}',
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de utilizadores do sistema (funcionários)
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    perfil_id INTEGER NOT NULL REFERENCES perfis(id),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    ultimo_acesso TIMESTAMP WITH TIME ZONE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE,
    CONSTRAINT email_check CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Tabela de utentes (solicitantes)
CREATE TABLE cidadaos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(30) NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(20),
    endereco TEXT,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE,
    CONSTRAINT email_check CHECK (email IS NULL OR email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT documento_unique UNIQUE (tipo_documento, numero_documento)
);

-- Tabela de categorias de serviços
CREATE TABLE categorias_servicos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    icone VARCHAR(50),
    cor VARCHAR(20),
    ordem INTEGER NOT NULL DEFAULT 0,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de tipos de serviços
CREATE TABLE tipos_servicos (
    id SERIAL PRIMARY KEY,
    categoria_id INTEGER NOT NULL REFERENCES categorias_servicos(id),
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    prazo_estimado INTEGER, -- em dias
    valor_base DECIMAL(10, 2),
    requer_vistoria BOOLEAN NOT NULL DEFAULT FALSE,
    requer_analise_tecnica BOOLEAN NOT NULL DEFAULT FALSE,
    requer_aprovacao BOOLEAN NOT NULL DEFAULT TRUE,
    disponivel_portal BOOLEAN NOT NULL DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de etapas de processo (workflow)
CREATE TABLE etapas_processo (
    id SERIAL PRIMARY KEY,
    tipo_servico_id INTEGER NOT NULL REFERENCES tipos_servicos(id),
    codigo VARCHAR(20) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    ordem INTEGER NOT NULL,
    tempo_estimado INTEGER, -- em horas
    requer_documento BOOLEAN NOT NULL DEFAULT FALSE,
    requer_pagamento BOOLEAN NOT NULL DEFAULT FALSE,
    requer_aprovacao BOOLEAN NOT NULL DEFAULT FALSE,
    perfil_responsavel_id INTEGER REFERENCES perfis(id),
    etapa_anterior_id INTEGER REFERENCES etapas_processo(id),
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE,
    UNIQUE (tipo_servico_id, codigo),
    UNIQUE (tipo_servico_id, ordem)
);

-- Tabela de tipos de documentos
CREATE TABLE tipos_documentos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    formato_permitido VARCHAR(255), -- ex: "pdf,jpg,png"
    tamanho_maximo INTEGER, -- em KB
    obrigatorio BOOLEAN NOT NULL DEFAULT TRUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de documentos necessários por tipo de serviço
CREATE TABLE documentos_servico (
    id SERIAL PRIMARY KEY,
    tipo_servico_id INTEGER NOT NULL REFERENCES tipos_servicos(id),
    tipo_documento_id INTEGER NOT NULL REFERENCES tipos_documentos(id),
    etapa_processo_id INTEGER REFERENCES etapas_processo(id),
    obrigatorio BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE,
    UNIQUE (tipo_servico_id, tipo_documento_id)
);

-- Tabela de status de pedido
CREATE TABLE status_pedido (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    cor VARCHAR(20),
    icone VARCHAR(50),
    ordem INTEGER NOT NULL DEFAULT 0,
    visivel_portal BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de pedidos
CREATE TABLE pedidos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    codigo_acompanhamento VARCHAR(20) NOT NULL UNIQUE,
    tipo_servico_id INTEGER NOT NULL REFERENCES tipos_servicos(id),
    cidadao_id UUID NOT NULL REFERENCES cidadaos(id),
    usuario_criacao_id UUID NOT NULL REFERENCES usuarios(id),
    usuario_responsavel_id UUID REFERENCES usuarios(id),
    etapa_atual_id INTEGER REFERENCES etapas_processo(id),
    status_id INTEGER NOT NULL REFERENCES status_pedido(id),
    data_inicio TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_previsao TIMESTAMP WITH TIME ZONE,
    data_conclusao TIMESTAMP WITH TIME ZONE,
    observacoes TEXT,
    valor_total DECIMAL(10, 2),
    origem VARCHAR(20) NOT NULL, -- 'PRESENCIAL', 'PORTAL', 'APP'
    prioridade INTEGER NOT NULL DEFAULT 0, -- 0=normal, 1=alta, 2=urgente
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de histórico de pedidos
CREATE TABLE historico_pedidos (
    id SERIAL PRIMARY KEY,
    pedido_id UUID NOT NULL REFERENCES pedidos(id),
    etapa_processo_id INTEGER REFERENCES etapas_processo(id),
    status_id INTEGER NOT NULL REFERENCES status_pedido(id),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    data_ocorrencia TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacao TEXT,
    dados_anteriores JSONB,
    dados_novos JSONB,
    tempo_execucao INTEGER, -- em minutos
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de documentos anexados aos pedidos
CREATE TABLE documentos_pedido (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    pedido_id UUID NOT NULL REFERENCES pedidos(id),
    tipo_documento_id INTEGER NOT NULL REFERENCES tipos_documentos(id),
    etapa_processo_id INTEGER REFERENCES etapas_processo(id),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    nome_arquivo VARCHAR(255) NOT NULL,
    caminho_arquivo VARCHAR(255) NOT NULL,
    tamanho_arquivo INTEGER NOT NULL, -- em KB
    tipo_mime VARCHAR(100) NOT NULL,
    hash_arquivo VARCHAR(64), -- SHA-256
    observacao TEXT,
    aprovado BOOLEAN,
    data_aprovacao TIMESTAMP WITH TIME ZONE,
    usuario_aprovacao_id UUID REFERENCES usuarios(id),
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de pagamentos
CREATE TABLE pagamentos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    pedido_id UUID NOT NULL REFERENCES pedidos(id),
    etapa_processo_id INTEGER REFERENCES etapas_processo(id),
    tipo VARCHAR(50) NOT NULL, -- 'TAXA', 'MULTA', 'IMPOSTO'
    descricao TEXT NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    codigo_barras VARCHAR(100),
    link_pagamento VARCHAR(255),
    status VARCHAR(20) NOT NULL, -- 'PENDENTE', 'PAGO', 'CANCELADO', 'VENCIDO'
    comprovante_id UUID REFERENCES documentos_pedido(id),
    parcelado BOOLEAN NOT NULL DEFAULT FALSE,
    numero_parcelas INTEGER,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de parcelas de pagamento
CREATE TABLE parcelas_pagamento (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    pagamento_id UUID NOT NULL REFERENCES pagamentos(id),
    numero_parcela INTEGER NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    codigo_barras VARCHAR(100),
    link_pagamento VARCHAR(255),
    status VARCHAR(20) NOT NULL, -- 'PENDENTE', 'PAGO', 'CANCELADO', 'VENCIDO'
    comprovante_id UUID REFERENCES documentos_pedido(id),
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE,
    UNIQUE (pagamento_id, numero_parcela)
);

-- Tabela de vistorias
CREATE TABLE vistorias (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    pedido_id UUID NOT NULL REFERENCES pedidos(id),
    etapa_processo_id INTEGER REFERENCES etapas_processo(id),
    usuario_responsavel_id UUID NOT NULL REFERENCES usuarios(id),
    data_agendada TIMESTAMP WITH TIME ZONE NOT NULL,
    data_realizada TIMESTAMP WITH TIME ZONE,
    status VARCHAR(20) NOT NULL, -- 'AGENDADA', 'REALIZADA', 'CANCELADA', 'REAGENDADA'
    resultado VARCHAR(20), -- 'APROVADA', 'REPROVADA', 'PENDENTE'
    observacoes TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de fotos de vistoria
CREATE TABLE fotos_vistoria (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vistoria_id UUID NOT NULL REFERENCES vistorias(id),
    nome_arquivo VARCHAR(255) NOT NULL,
    caminho_arquivo VARCHAR(255) NOT NULL,
    tamanho_arquivo INTEGER NOT NULL, -- em KB
    tipo_mime VARCHAR(100) NOT NULL,
    descricao TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    data_captura TIMESTAMP WITH TIME ZONE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de lotes (para processos de compra/legalização de lotes)
CREATE TABLE lotes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    codigo VARCHAR(50) NOT NULL UNIQUE,
    quadra VARCHAR(50),
    numero VARCHAR(50),
    area DECIMAL(10, 2), -- em m²
    endereco TEXT NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL DEFAULT 'Cidade Padrão',
    estado VARCHAR(50) NOT NULL DEFAULT 'Estado Padrão',
    cep VARCHAR(20),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    situacao VARCHAR(20) NOT NULL, -- 'DISPONIVEL', 'VENDIDO', 'RESERVADO', 'IRREGULAR'
    valor_base DECIMAL(10, 2),
    observacoes TEXT,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de relação entre pedidos e lotes
CREATE TABLE pedidos_lotes (
    id SERIAL PRIMARY KEY,
    pedido_id UUID NOT NULL REFERENCES pedidos(id),
    lote_id UUID NOT NULL REFERENCES lotes(id),
    tipo_relacao VARCHAR(50) NOT NULL, -- 'COMPRA', 'LEGALIZACAO', 'ATUALIZACAO'
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (pedido_id, lote_id)
);

-- Tabela de plantas/documentos emitidos
CREATE TABLE plantas_emitidas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    pedido_id UUID NOT NULL REFERENCES pedidos(id),
    lote_id UUID REFERENCES lotes(id),
    tipo VARCHAR(50) NOT NULL, -- 'PLANTA', 'CERTIDAO', 'LICENCA', 'CONTRATO'
    numero_documento VARCHAR(50) NOT NULL,
    data_emissao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_validade TIMESTAMP WITH TIME ZONE,
    usuario_emissor_id UUID NOT NULL REFERENCES usuarios(id),
    documento_id UUID REFERENCES documentos_pedido(id),
    observacoes TEXT,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de eventos (para pedidos de eventos)
CREATE TABLE eventos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    pedido_id UUID NOT NULL REFERENCES pedidos(id),
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    data_inicio TIMESTAMP WITH TIME ZONE NOT NULL,
    data_fim TIMESTAMP WITH TIME ZONE NOT NULL,
    local TEXT NOT NULL,
    capacidade INTEGER,
    tipo_evento VARCHAR(100) NOT NULL,
    responsavel VARCHAR(100) NOT NULL,
    contato_responsavel VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL, -- 'SOLICITADO', 'APROVADO', 'NEGADO', 'REALIZADO', 'CANCELADO'
    observacoes TEXT,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE,
    CONSTRAINT check_datas CHECK (data_fim > data_inicio)
);

-- Tabela de favoritos dos utilizadores
CREATE TABLE favoritos (
    id SERIAL PRIMARY KEY,
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    tipo_servico_id INTEGER NOT NULL REFERENCES tipos_servicos(id),
    ordem INTEGER NOT NULL DEFAULT 0,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (usuario_id, tipo_servico_id)
);

-- Tabela de notificações
CREATE TABLE notificacoes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID REFERENCES usuarios(id),
    cidadao_id UUID REFERENCES cidadaos(id),
    pedido_id UUID REFERENCES pedidos(id),
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- 'INFO', 'ALERTA', 'ERRO', 'SUCESSO'
    lida BOOLEAN NOT NULL DEFAULT FALSE,
    data_leitura TIMESTAMP WITH TIME ZONE,
    enviada_email BOOLEAN NOT NULL DEFAULT FALSE,
    enviada_sms BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de configurações do sistema
CREATE TABLE configuracoes (
    chave VARCHAR(100) PRIMARY KEY,
    valor TEXT NOT NULL,
    descricao TEXT,
    tipo VARCHAR(20) NOT NULL, -- 'STRING', 'INTEGER', 'BOOLEAN', 'JSON'
    grupo VARCHAR(50) NOT NULL,
    editavel BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Tabela de logs do sistema
CREATE TABLE logs_sistema (
    id SERIAL PRIMARY KEY,
    usuario_id UUID REFERENCES usuarios(id),
    acao VARCHAR(50) NOT NULL,
    tabela VARCHAR(50),
    registro_id VARCHAR(100),
    dados JSONB,
    ip VARCHAR(45),
    user_agent TEXT,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhorar a performance

-- Índices para buscas frequentes
CREATE INDEX idx_pedidos_codigo ON pedidos(codigo_acompanhamento);
CREATE INDEX idx_pedidos_cidadao ON pedidos(cidadao_id);
CREATE INDEX idx_pedidos_tipo_servico ON pedidos(tipo_servico_id);
CREATE INDEX idx_pedidos_status ON pedidos(status_id);
CREATE INDEX idx_pedidos_data_inicio ON pedidos(data_inicio);
CREATE INDEX idx_historico_pedido ON historico_pedidos(pedido_id);
CREATE INDEX idx_documentos_pedido ON documentos_pedido(pedido_id);
CREATE INDEX idx_pagamentos_pedido ON pagamentos(pedido_id);
CREATE INDEX idx_vistorias_pedido ON vistorias(pedido_id);
CREATE INDEX idx_cidadaos_documento ON cidadaos(tipo_documento, numero_documento);

-- Funções e triggers

-- Função para atualizar o campo atualizado_em automaticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.atualizado_em = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Aplicar o trigger em todas as tabelas com campo atualizado_em
DO $$
DECLARE
    t text;
BEGIN
    FOR t IN 
        SELECT table_name 
        FROM information_schema.columns 
        WHERE column_name = 'atualizado_em' 
        AND table_schema = 'public'
    LOOP
        EXECUTE format('CREATE TRIGGER set_updated_at
                        BEFORE UPDATE ON %I
                        FOR EACH ROW
                        EXECUTE FUNCTION update_updated_at_column()', t);
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Função para gerar código de acompanhamento
CREATE OR REPLACE FUNCTION generate_tracking_code() 
RETURNS TRIGGER AS $$
DECLARE
    ano VARCHAR(2);
    seq INTEGER;
    codigo VARCHAR(20);
BEGIN
    ano := to_char(CURRENT_DATE, 'YY');
    
    -- Obter o próximo número sequencial para o ano atual
    SELECT COALESCE(MAX(SUBSTRING(codigo_acompanhamento FROM 6 FOR 6)::INTEGER), 0) + 1
    INTO seq
    FROM pedidos
    WHERE SUBSTRING(codigo_acompanhamento FROM 1 FOR 4) = 'SMP-';
    
    -- Formatar o código: SMP-AANNNNNN (AA=ano, NNNNNN=sequencial)
    codigo := 'SMP-' || ano || LPAD(seq::TEXT, 6, '0');
    
    NEW.codigo_acompanhamento := codigo;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para gerar código de acompanhamento automaticamente
CREATE TRIGGER generate_tracking_code_trigger
BEFORE INSERT ON pedidos
FOR EACH ROW
EXECUTE FUNCTION generate_tracking_code();

-- Função para registrar histórico de alterações em pedidos
CREATE OR REPLACE FUNCTION log_pedido_changes()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO historico_pedidos (
        pedido_id,
        etapa_processo_id,
        status_id,
        usuario_id,
        observacao,
        dados_anteriores,
        dados_novos
    ) VALUES (
        NEW.id,
        NEW.etapa_atual_id,
        NEW.status_id,
        COALESCE(current_setting('app.usuario_id', true)::UUID, NEW.usuario_responsavel_id),
        'Atualização automática',
        row_to_json(OLD),
        row_to_json(NEW)
    );
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para registrar histórico de alterações em pedidos
CREATE TRIGGER log_pedido_changes_trigger
AFTER UPDATE ON pedidos
FOR EACH ROW
WHEN (OLD.status_id IS DISTINCT FROM NEW.status_id OR OLD.etapa_atual_id IS DISTINCT FROM NEW.etapa_atual_id)
EXECUTE FUNCTION log_pedido_changes();

-- Comentários nas tabelas para documentação
COMMENT ON TABLE usuarios IS 'Utilizadores do sistema (funcionários)';
COMMENT ON TABLE cidadaos IS 'Utentes que solicitam serviços';
COMMENT ON TABLE perfis IS 'Perfis de acesso dos utilizadores';
COMMENT ON TABLE tipos_servicos IS 'Tipos de serviços oferecidos';
COMMENT ON TABLE etapas_processo IS 'Etapas do fluxo de trabalho para cada tipo de serviço';
COMMENT ON TABLE pedidos IS 'Pedidos de serviços realizados pelos utentes';
COMMENT ON TABLE historico_pedidos IS 'Histórico de alterações nos pedidos';
COMMENT ON TABLE documentos_pedido IS 'Documentos anexados aos pedidos';
COMMENT ON TABLE pagamentos IS 'Pagamentos relacionados aos pedidos';
COMMENT ON TABLE vistorias IS 'Vistorias realizadas para os pedidos';
COMMENT ON TABLE lotes IS 'Cadastro de lotes';
COMMENT ON TABLE plantas_emitidas IS 'Plantas e documentos emitidos';
COMMENT ON TABLE eventos IS 'Eventos autorizados';
COMMENT ON TABLE favoritos IS 'Serviços favoritos dos utilizadores';