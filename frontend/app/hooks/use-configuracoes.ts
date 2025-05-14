import { useQuery } from '@tanstack/react-query';
import { getConfiguracoes, ConfiguracaoResponse } from '@/lib/api-services';

export function useConfiguracoes() {
  const { data, isLoading, error } = useQuery<ConfiguracaoResponse>({
    queryKey: ['configuracoes'],
    queryFn: getConfiguracoes,
    staleTime: 1000 * 60 * 5, // 5 minutos
    gcTime: 1000 * 60 * 30, // 30 minutos
  });

  return {
    configuracoes: data,
    isLoading,
    error,
    statusPedidos: data?.statusPedido || [],
    categoriasServico: data?.categoriasServico || [],
    perfis: data?.perfis || []
  };
}