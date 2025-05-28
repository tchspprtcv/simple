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
  CategoriaServico, // Adicionado CategoriaServico para importação
  UsuarioRequest,
  UsuarioResponse,
  ConfiguracaoResponse
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
    // Não adicionar token de autenticação para a rota de login ou registro
    if (config.url === '/auth/login' || config.url === '/auth/register') {
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

// Interceptor para tratar respostas globais, incluindo erros de autenticação
apiClient.interceptors.response.use(
  (response) => response, // Passa as respostas de sucesso diretamente
  (error: AxiosError) => {
    const currentPath = typeof window !== 'undefined' ? window.location.pathname : '';
    // Verifica se o erro é 401 (Não Autorizado) OU 403 (Proibido)
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      // Evita loops de redirecionamento se já estiver na página de login ou registro
      if (currentPath && currentPath !== '/login' && currentPath !== '/register') {
        logout(); // Limpa o token e cookies
        // Redireciona para a página de login
        if (typeof window !== 'undefined') {
          window.location.href = '/login';
        }
        // Retorna uma promessa que nunca resolve para evitar que o chamador original
        // tente atualizar o estado de um componente desmontado.
        return new Promise(() => {});
      }
    }
    // Para todos os outros erros, ou se já estiver em /login ou /register, rejeita a promessa
    // para que o tratamento de erro local possa ocorrer.
    return Promise.reject(error);
  }
);

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
 * Atualiza um tipo de serviço existente
 */
export const updateTipoServico = async (id: number, serviceData: Partial<TipoServico>): Promise<TipoServico> => {
  try {
    const response = await apiClient.put<TipoServico>(`/tipos-servicos/${id}`, serviceData);
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
 * Registra um novo utilizador
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
 * Obtém o utilizador atual
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
 * Atualiza o perfil do utilizador atual
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
 * Lista todos os utentes (paginado)
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
 * Obtém um utente pelo ID
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
 * Obtém um utente pelo documento
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
 * Cria um novo utente
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
 * Atualiza um utente existente
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
 * Lista pedidos por utente (paginado)
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
  
// Funções CRUD para CategoriaServico
/**
 * Lista todas as categorias de serviços
 */
export const listCategoriasServicos = async (): Promise<CategoriaServico[]> => {
  try {
    const response = await apiClient.get<CategoriaServico[]>('/categorias-servicos');
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Cria uma nova categoria de serviço
 */
export const createCategoriaServico = async (categoriaData: Omit<CategoriaServico, 'id'>): Promise<CategoriaServico> => {
  try {
    const response = await apiClient.post<CategoriaServico>('/categorias-servicos', categoriaData);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Obtém uma categoria de serviço pelo ID
 */
export const getCategoriaServicoById = async (id: number): Promise<CategoriaServico> => {
  try {
    const response = await apiClient.get<CategoriaServico>(`/categorias-servicos/${id}`);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Atualiza uma categoria de serviço existente
 */
export const updateCategoriaServico = async (id: number, categoriaData: Partial<CategoriaServico>): Promise<CategoriaServico> => {
  try {
    const response = await apiClient.put<CategoriaServico>(`/categorias-servicos/${id}`, categoriaData);
    return response.data;
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Deleta uma categoria de serviço
 */
export const deleteCategoriaServico = async (id: number): Promise<void> => {
  try {
    await apiClient.delete(`/categorias-servicos/${id}`);
  } catch (error) {
    throw handleApiError(error as AxiosError);
  }
};

/**
 * Lista os tipos de serviços por categoria ID
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
 * Lista os serviços favoritos do utilizador
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
 * Adiciona ou remove um serviço dos favoritos do utilizador atual
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

/**
 * Obtém todas as configurações do sistema
 */
export const getConfiguracoes = async (): Promise<ConfiguracaoResponse> => {
  try {
    const response = await apiClient.get<ConfiguracaoResponse>('/configuracoes');
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
  CategoriaServico,
  UsuarioRequest,
  UsuarioResponse,
  ConfiguracaoResponse // Adicionado ConfiguracaoResponse para re-exportação
};



 

