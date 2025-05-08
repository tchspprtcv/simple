"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useToast } from "@/components/ui/use-toast"
import { getPedidoByCodigo, PedidoResponse } from "@/lib/api-services"
import { useState } from "react"

export default function TrackingPage() {
  const [trackingCode, setTrackingCode] = useState("")
  const [request, setRequest] = useState<PedidoResponse | null>(null)
  const [isLoading, setIsLoading] = useState(false)
  const { toast } = useToast()

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)
    
    try {
      const response = await getPedidoByCodigo(trackingCode)
      setRequest(response)
    } catch (error: any) {
      toast({
        title: "Erro ao buscar solicitação",
        description: error.message || "Verifique o código de rastreamento e tente novamente.",
        variant: "destructive",
      })
      setRequest(null)
    } finally {
      setIsLoading(false)
    }
  }

  // Função para formatar a data
  const formatDate = (dateString: string | undefined) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString)
    return date.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  // Função para traduzir o status
  const translateStatus = (status: string) => {
    const statusMap: Record<string, string> = {
      'NOVO': 'Novo',
      'EM_ANALISE': 'Em Análise',
      'AGUARDANDO_DOC': 'Aguardando Documentação',
      'AGUARDANDO_PAG': 'Aguardando Pagamento',
      'AGENDADO': 'Agendado',
      'EM_VISTORIA': 'Em Vistoria',
      'APROVADO': 'Aprovado',
      'REPROVADO': 'Reprovado',
      'CONCLUIDO': 'Concluído',
      'CANCELADO': 'Cancelado'
    }
    return statusMap[status] || status
  }

  // Função para traduzir o tipo (ajustar conforme necessário)
  const translateType = (type: string) => {
    // Mapeamento simplificado, pode precisar de mais detalhes do backend
    const typeMap: Record<string, string> = {
      'LEGALIZACAO': 'Legalização',
      'COMPRA_LOTE': 'Compra de Lote',
      'PEDIDO_EVENTOS': 'Pedido de Eventos',
      'PAGAMENTO_PRESTACAO': 'Pagamento de Prestação',
      'MUDANCA_NOME': 'Mudança de Nome',
      'ATUALIZACAO_PLANTA': 'Atualização de Planta',
      'EMISSAO_PLANTA': 'Emissão de Planta',
      'LICENCIAMENTO_COMERCIAL': 'Licenciamento Comercial',
      'APROVACAO_PROJETOS': 'Aprovação de Projetos',
      'LICENCA_CONSTRUCAO': 'Licença de Construção',
    }
    return typeMap[type] || type
  }

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Acompanhamento de Solicitação</h1>
      <Card className="max-w-md mx-auto">
        <CardHeader>
          <CardTitle>Consultar Solicitação</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSearch} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="tracking-code">Código de Rastreamento</Label>
              <Input
                id="tracking-code"
                value={trackingCode}
                onChange={(e) => setTrackingCode(e.target.value)}
                placeholder="Digite o código de rastreamento"
                disabled={isLoading}
                required
              />
            </div>
            <Button type="submit" className="w-full" disabled={isLoading}>
              {isLoading ? "Buscando..." : "Buscar"}
            </Button>
          </form>

          {request && (
            <div className="mt-6 space-y-4">
              <h2 className="text-xl font-semibold">Detalhes da Solicitação</h2>
              <div className="space-y-2">
                <p><strong>Código:</strong> {request.codigoAcompanhamento}</p>
                <p><strong>Tipo de Serviço:</strong> {translateType(request.tipoServico)}</p>
                <p><strong>Status:</strong> <span className={ // Ajustar classes de cor conforme necessário
                  request.status === 'CONCLUIDO' ? 'text-green-600' :
                  request.status === 'APROVADO' ? 'text-green-600' :
                  request.status === 'EM_ANALISE' ? 'text-blue-600' :
                  request.status === 'NOVO' ? 'text-yellow-600' :
                  request.status === 'AGUARDANDO_DOC' ? 'text-orange-600' :
                  request.status === 'AGUARDANDO_PAG' ? 'text-orange-600' :
                  request.status === 'REPROVADO' ? 'text-red-600' :
                  request.status === 'CANCELADO' ? 'text-red-600' :
                  'text-gray-600'
                }>{translateStatus(request.status)}</span></p>
                <p><strong>Data de Início:</strong> {formatDate(request.dataInicio)}</p>
                <p><strong>Previsão de Conclusão:</strong> {formatDate(request.dataPrevisao)}</p>
                <p><strong>Data de Conclusão:</strong> {formatDate(request.dataConclusao)}</p>
                {request.observacoes && <p><strong>Observações:</strong> {request.observacoes}</p>}
                {/* Adicionar outros campos relevantes de PedidoResponse se necessário */}
              </div>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
