/**
 * Tipos de dados compartilhados para o sistema Simple
 */
import { ReactNode } from 'react';

// Enums
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

export enum UserRole {
  ADMINISTRADOR = 'ADMINISTRADOR',
  ATENDENTE = 'ATENDENTE',
  TECNICO = 'TECNICO',
  FISCAL = 'FISCAL',
  GESTOR = 'GESTOR',
}

// Interfaces
export interface CategoriaServico {
  id: number;
  nome: string;
  descricao?: string;
  ativo: boolean;
}

export interface User {
  id: string;
  nome: string;
  email: string;
  perfil: string;
  ativo: boolean;
  ultimoAcesso?: string;
  criadoEm: string;
}

export interface Cidadao {
  id: string;
  nome: string;
  tipoDocumento: string;
  numeroDocumento: string;
  email?: string;
  telefone?: string;
  endereco?: string;
  criadoEm: string;
}

export interface TipoServico {
  id: number;
  codigo: string;
  nome: string;
  descricao?: string;
  categoriaId: number;
  categoriaNome: string;
  isFavorito: boolean;
  prazoEstimado?: number;
  valorBase?: number;
  requerVistoria: boolean;
  requerAnaliseTecnica: boolean;
  requerAprovacao: boolean;
  disponivelPortal: boolean;
  ativo: boolean;
}

export interface PedidoRequest {
  tipoServicoId: number;
  cidadaoId: string;
  origem: string;
  prioridade?: number;
  observacoes?: string;
}

export interface PedidoResponse {
  trackingCode: string;
  service: any;
  currentStatus(currentStatus: any): string | undefined;
  createdAt: string | number | Date;
  historicoStatus: Array<{
    status: string;
    dataHora: string;
    observacao?: string;
    usuario?: string;
  }>;
  id: string;
  codigoAcompanhamento: string;
  tipoServico: string;
  cidadao: string;
  usuarioCriacao: string;
  usuarioResponsavel?: string;
  etapaAtual?: string;
  status: string;
  dataInicio: string;
  dataPrevisao?: string;
  dataConclusao?: string;
  observacoes?: string;
  valorTotal?: number;
  origem: string;
  prioridade: number;
  criadoEm: string;
}

export interface DashboardItem {
  id: string;
  codigoAcompanhamento: string;
  tipoServico: string;
  status: string;
  dataInicio: string;
  dataPrevisao?: string;
  prioridade: number;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface AuthRequest {
  email: string;
  senha: string;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
}

export interface UsuarioRequest {
  nome: string;
  email: string;
  senha: string;
  perfilId: number;
}

export interface UsuarioResponse {
  id: string;
  nome: string;
  email: string;
  perfil: string;
  ativo: boolean;
  ultimoAcesso?: string;
  criadoEm: string;
}

export interface CidadaoRequest {
  nome: string;
  tipoDocumento: string;
  numeroDocumento: string;
  email?: string;
  telefone?: string;
  endereco?: string;
}

export interface CidadaoResponse {
  id: string;
  nome: string;
  tipoDocumento: string;
  numeroDocumento: string;
  email?: string;
  telefone?: string;
  endereco?: string;
  criadoEm: string;
}

export interface CategoriaServico {
  id: number;
  nome: string;
  descricao?: string;
  ativo: boolean;
}

export interface ApiError {
  message: string;
  status: number;
  timestamp?: string;
}



export interface StatusHistory {
  date: string;
  status: string;
  description: string;
}

export interface TrackingResponse {
  success: boolean;
  data?: RequestStatus;
  error?: string;
}

export interface StatusPedidoResponse {
  id: number;
  nome: string;
  descricao?: string;
  criadoEm: string;
  atualizadoEm: string;
}

export interface CategoriaServicoResponse {
  id: number;
  nome: string;
  descricao?: string;
  criadoEm: string;
  atualizadoEm: string;
}

export interface PerfilResponse {
  id: number;
  nome: string;
  descricao?: string;
  criadoEm: string;
  atualizadoEm: string;
}

export interface ConfiguracaoResponse {
  statusPedido: StatusPedidoResponse[];
  categoriasServico: CategoriaServicoResponse[];
  perfis: PerfilResponse[];
}