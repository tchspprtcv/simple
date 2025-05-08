/**
 * Configuração da API para o sistema Simple
 * Este arquivo contém as configurações necessárias para conectar o frontend ao backend
 */

// URL base da API - usa variável de ambiente ou fallback para localhost
export const API_BASE = process.env.NEXT_PUBLIC_API_BASE ?? 'http://localhost:8080/api';

// Timeout padrão para requisições (em milissegundos)
export const DEFAULT_TIMEOUT = 30000;

// Headers padrão para requisições
export const DEFAULT_HEADERS = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
};

/**
 * Função utilitária para construir URLs da API
 * @param path - Caminho relativo da API
 * @returns URL completa da API
 */
export const buildApiUrl = (path: string): string => {
  // Remove barras iniciais duplicadas
  const normalizedPath = path.startsWith('/') ? path.substring(1) : path;
  return `${API_BASE}/${normalizedPath}`;
};

/**
 * Tipos de pedidos suportados pelo sistema
 */
export enum RequestType {
  LEGALIZACAO = 'LEGALIZACAO',
  COMPRA_LOTE = 'COMPRA_LOTE',
  PEDIDO_EVENTOS = 'PEDIDO_EVENTOS',
  PAGAMENTO_PRESTACAO = 'PAGAMENTO_PRESTACAO',
  MUDANCA_NOME = 'MUDANCA_NOME',
  ATUALIZACAO_PLANTA = 'ATUALIZACAO_PLANTA',
  EMISSAO_PLANTA = 'EMISSAO_PLANTA',
  LICENCIAMENTO_COMERCIAL = 'LICENCIAMENTO_COMERCIAL',
  APROVACAO_PROJETOS = 'APROVACAO_PROJETOS',
  LICENCA_CONSTRUCAO = 'LICENCA_CONSTRUCAO',
}

/**
 * Status possíveis para um pedido
 */
export enum RequestStatus {
  NOVO = 'NOVO',
  EM_ANALISE = 'EM_ANALISE',
  AGUARDANDO_DOC = 'AGUARDANDO_DOC',
  AGUARDANDO_PAG = 'AGUARDANDO_PAG',
  AGENDADO = 'AGENDADO',
  EM_VISTORIA = 'EM_VISTORIA',
  APROVADO = 'APROVADO',
  REPROVADO = 'REPROVADO',
  CONCLUIDO = 'CONCLUIDO',
  CANCELADO = 'CANCELADO',
}
