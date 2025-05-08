/**
 * Serviços de API para o sistema Simple
 * Este arquivo contém as funções para interagir com o backend
 */

// First install axios: npm install axios @types/axios
import axios from 'axios';
import type { AxiosError, AxiosRequestConfig, InternalAxiosRequestConfig } from 'axios';
import { API_BASE, DEFAULT_HEADERS, DEFAULT_TIMEOUT } from './api-config';
import {
  ApiError,
  AuthRequest,
  AuthResponse,
  CidadaoRequest,
  CidadaoResponse,
  DashboardItem,
  PaginatedResponse,
  PedidoRequest,
  PedidoResponse,
  RequestStatus,
  RequestType,
  TipoServico,
  UsuarioRequest,
  UsuarioResponse
} from './types';

// Cliente axios configurado
const apiClient = axios.create({
  baseURL: API_BASE,
  timeout: DEFAULT_TIMEOUT,
  headers: DEFAULT_HEADERS,
});

// Interceptor para adicionar token de autenticação
apiClient.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  if (typeof window !== 'undefined') {
    // Não adicionar token de autenticação para a rota de login
    if (config.url === '/auth/login') {
      return config;
    }

    const token = localStorage.getItem('token');
    if (token) {
      if (!config.headers) config.headers = new axios.AxiosHeaders();
      config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
});

/**
 * Trata erros da API de forma padronizada
 */
const handleApiError = (error: AxiosError): ApiError => {
  if (error.response) {
    // O servidor respondeu com um status de erro
    const data = error.response.data as any;
    return {
      message: data.message || data.error || 'Erro desconhecido',
      status: error.response.status,
      timestamp: data.timestamp,
    };
  } else if (error.request) {
    // A requisição foi feita mas não houve resposta
    return {
      message: 'Não foi possível conectar ao servidor',
      status: 0,
    };
  } else {
    // Erro ao configurar a requisição
    return {
      message: error.message || 'Erro ao processar a requisição',
      status: 0,
    };
  }
};

/**
 * Realiza login no sistema
 */
export const login = async (credentials: AuthRequest): Promise<AuthResponse> => {
  try {
    const response = await apiClient.post<AuthResponse>('/auth/login', credentials);
    // Armazena o token para uso futuro
    if (typeof window !== 'undefined') {
      localStorage.setItem('token', response.data.token);
      // Salva o token como cookie
      document.cookie = `token=${response.data.token}; path=/; max-age=${60 * 60 * 24 * 7}; SameSite=Lax`; // Expira em 7 dias
    }
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Realiza logout do sistema
 */
export const logout = (): void => {
  if (typeof window !== 'undefined') {
    localStorage.removeItem('token');
    // Remove o cookie do token
    document.cookie = 'token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT; SameSite=Lax';
  }
};

/**
 * Registra um novo usuário
 */
export const registerUser = async (user: UsuarioRequest): Promise<UsuarioResponse> => {
  try {
    const response = await apiClient.post<UsuarioResponse>('/auth/register', user);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Obtém o usuário atual
 */
export const getCurrentUser = async (): Promise<UsuarioResponse> => {
  try {
    const response = await apiClient.get<UsuarioResponse>('/users/me');
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Atualiza o perfil do usuário atual
 */
export const updateUserProfile = async (userData: Partial<UsuarioRequest>): Promise<UsuarioResponse> => {
  try {
    const response = await apiClient.put<UsuarioResponse>('/users/me', userData);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Lista todos os cidadãos (paginado)
 */
export const listCidadaos = async (
  page: number = 0,
  size: number = 10
): Promise<PaginatedResponse<CidadaoResponse>> => {
  try {
    const response = await apiClient.get<PaginatedResponse<CidadaoResponse>>(
      `/cidadaos?page=${page}&size=${size}`
    );
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Obtém um cidadão pelo ID
 */
export const getCidadaoById = async (id: string): Promise<CidadaoResponse> => {
  try {
    const response = await apiClient.get<CidadaoResponse>(`/cidadaos/${id}`);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Obtém um cidadão pelo documento
 */
export const getCidadaoByDocumento = async (
  tipoDocumento: string,
  numeroDocumento: string
): Promise<CidadaoResponse> => {
  try {
    const response = await apiClient.get<CidadaoResponse>(
      `/cidadaos/documento?tipoDocumento=${tipoDocumento}&numeroDocumento=${numeroDocumento}`
    );
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Cria um novo cidadão
 */
export const createCidadao = async (cidadao: CidadaoRequest): Promise<CidadaoResponse> => {
  try {
    const response = await apiClient.post<CidadaoResponse>('/cidadaos', cidadao);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Atualiza um cidadão existente
 */
export const updateCidadao = async (
  id: string,
  cidadao: CidadaoRequest
): Promise<CidadaoResponse> => {
  try {
    const response = await apiClient.put<CidadaoResponse>(`/cidadaos/${id}`, cidadao);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Lista todos os pedidos (paginado)
 */
export const listPedidos = async (
  page: number = 0,
  size: number = 10
): Promise<PaginatedResponse<PedidoResponse>> => {
  try {
    const response = await apiClient.get<PaginatedResponse<PedidoResponse>>(
      `/pedidos?page=${page}&size=${size}`
    );
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Obtém um pedido pelo ID
 */
export const getPedidoById = async (id: string): Promise<PedidoResponse> => {
  try {
    const response = await apiClient.get<PedidoResponse>(`/pedidos/${id}`);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Obtém um pedido pelo código de acompanhamento
 */
export const getPedidoByCodigo = async (codigo: string): Promise<PedidoResponse> => {
  try {
    const response = await apiClient.get<PedidoResponse>(`/pedidos/codigo/${codigo}`);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Lista pedidos por cidadão (paginado)
 */
export const listPedidosByCidadao = async (
  cidadaoId: string,
  page: number = 0,
  size: number = 10
): Promise<PaginatedResponse<PedidoResponse>> => {
  try {
    const response = await apiClient.get<PaginatedResponse<PedidoResponse>>(
      `/pedidos/cidadao/${cidadaoId}?page=${page}&size=${size}`
    );
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Cria um novo pedido
 */
export const createPedido = async (pedido: PedidoRequest): Promise<PedidoResponse> => {
  try {
    const response = await apiClient.post<PedidoResponse>('/pedidos', pedido);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Atualiza o status de um pedido
 */
export const updatePedidoStatus = async (
  id: string,
  statusId: number
): Promise<PedidoResponse> => {
  try {
    const response = await apiClient.patch<PedidoResponse>(`/pedidos/${id}/status/${statusId}`);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Lista os tipos de serviços disponíveis
 */
export const listTiposServicos = async (): Promise<TipoServico[]> => {
  try {
    const response = await apiClient.get<TipoServico[]>('/tipos-servicos');
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Lista os tipos de serviços por categoria
 */
/**
 * Cria um novo tipo de serviço
 */
export const createTipoServico = async (serviceData: Omit<TipoServico, 'id'>): Promise<TipoServico> => {
  try {
    const response = await apiClient.post<TipoServico>('/tipos-servicos', serviceData);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Lista os tipos de serviços por categoria
 */
export const listTiposServicosByCategoria = async (categoriaId: number): Promise<TipoServico[]> => {
  try {
    const response = await apiClient.get<TipoServico[]>(`/tipos-servicos/categoria/${categoriaId}`);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Lista os serviços favoritos do usuário
 */
export const listServicosFavoritos = async (): Promise<TipoServico[]> => {
  try {
    const response = await apiClient.get<TipoServico[]>('/favoritos');
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Adiciona um serviço aos favoritos
 */
export const addServicoFavorito = async (servicoId: number): Promise<void> => {
  try {
    await apiClient.post(`/favoritos/${servicoId}`);
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Remove um serviço dos favoritos
 */
export const removeServicoFavorito = async (servicoId: number): Promise<void> => {
  try {
    await apiClient.delete(`/favoritos/${servicoId}`);
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Adiciona ou remove um serviço dos favoritos do usuário atual
 */
export const toggleFavorito = async (tipoServicoId: number): Promise<void> => {
  try {
    // O endpoint exato pode variar, ajuste conforme a API do backend
    // Pode ser um POST para adicionar e um DELETE para remover, ou um único endpoint que faz o toggle.
    // Aqui, vamos assumir um endpoint POST que faz o toggle.
    // Se o backend retornar os favoritos atualizados, você pode ajustar o tipo de retorno.
    await apiClient.post(`/usuarios/me/favoritos/${tipoServicoId}`);
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Lista os itens do dashboard (paginado)
 */
export const listPedidosDoUsuarioLogado = async (
  page: number = 0,
  size: number = 10
): Promise<PaginatedResponse<PedidoResponse>> => {
  try {
    const response = await apiClient.get<PaginatedResponse<PedidoResponse>>(
      `/pedidos/meus-pedidos?page=${page}&size=${size}`
    );
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

export const listDashboardItems = async (
  page: number = 0,
  size: number = 10
): Promise<PaginatedResponse<DashboardItem>> => {
  try {
    const response = await apiClient.get<PaginatedResponse<DashboardItem>>(
      `/dashboard?page=${page}&size=${size}`
    );
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

// Re-exportar os tipos para facilitar o uso
export type {
  ApiError,
  AuthRequest,
  AuthResponse,
  CidadaoRequest,
  CidadaoResponse,
  DashboardItem,
  PaginatedResponse,
  PedidoRequest,
  PedidoResponse,
  RequestStatus,
  RequestType,
  TipoServico,
  UsuarioRequest,
  UsuarioResponse
};

