"use client"

import { useEffect, useState } from "react"
import { useParams } from "next/navigation"
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { getPedidoById } from "@/lib/api-services" // Assumindo que getPedidoById será criada em api-services
import { PedidoResponse } from "@/lib/types"
import { useToast } from "@/components/ui/use-toast"
import { Skeleton } from "@/components/ui/skeleton"
import Link from "next/link"
import { Badge } from "@/components/ui/badge"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"

export default function RequestDetailsPage() {
  const params = useParams()
  const pedidoId = params.id as string
  const [pedido, setPedido] = useState<PedidoResponse | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const { toast } = useToast()

  useEffect(() => {
    if (pedidoId) {
      const fetchPedidoDetails = async () => {
        setIsLoading(true)
        try {
          const data = await getPedidoById(pedidoId)
          setPedido(data)

        } catch (error: any) {
          toast({
            title: "Erro ao carregar detalhes do pedido",
            description: error.message || "Não foi possível carregar os detalhes do pedido.",
            variant: "destructive",
          })
          setPedido(null)
        } finally {
          setIsLoading(false)
        }
      }
      fetchPedidoDetails()
    }
  }, [pedidoId, toast])

  const getStatusColor = (status: string) => {
    const statusMap: Record<string, string> = {
      'NOVO': 'bg-blue-500',
      'EM_ANALISE': 'bg-yellow-500 text-black',
      'AGUARDANDO_DOC': 'bg-orange-500',
      'AGUARDANDO_PAG': 'bg-red-500',
      'AGENDADO': 'bg-purple-500',
      'EM_VISTORIA': 'bg-indigo-500',
      'APROVADO': 'bg-green-500',
      'REPROVADO': 'bg-red-700',
      'CONCLUIDO': 'bg-green-700',
      'CANCELADO': 'bg-gray-500',
    }
    return statusMap[status] || 'bg-gray-500'
  }

  const formatDate = (dateString: string | null | undefined) => {
    if (!dateString) return "N/A"
    try {
      return format(new Date(dateString), "dd/MM/yyyy 'às' HH:mm", { locale: ptBR })
    } catch (error) {
      return "Data inválida"
    }
  }

  if (isLoading) {
    return (
      <div className="container mx-auto py-8 space-y-4">
        <Skeleton className="h-8 w-1/4" />
        <Skeleton className="h-4 w-1/2" />
        <Card>
          <CardHeader>
            <Skeleton className="h-6 w-3/4" />
          </CardHeader>
          <CardContent className="space-y-2">
            <Skeleton className="h-4 w-full" />
            <Skeleton className="h-4 w-full" />
            <Skeleton className="h-4 w-2/3" />
          </CardContent>
        </Card>
      </div>
    )
  }

  if (!pedido) {
    return (
      <div className="container mx-auto py-8 text-center">
        <p className="text-xl text-muted-foreground">Pedido não encontrado.</p>
        <Link href="/dashboard">
          <Button variant="outline" className="mt-4">Voltar ao Dashboard</Button>
        </Link>
      </div>
    )
  }

  return (
    <div className="container mx-auto py-8">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-3xl font-bold">Detalhes do Pedido</h1>
          <p className="text-muted-foreground">Código: {pedido.codigoAcompanhamento}</p>
        </div>
        <Badge className={`${getStatusColor(pedido.status)} text-white px-3 py-1 text-sm`}>{pedido.status.replace("_", " ")}</Badge>
      </div>

      <div className="grid md:grid-cols-3 gap-6 mb-6">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Informações Gerais</CardTitle>
          </CardHeader>
          <CardContent className="text-sm space-y-1">
            <p><strong>Serviço:</strong> {pedido.tipoServico}</p>
            <p><strong>Utente:</strong> {pedido.cidadao}</p>
            <p><strong>Data de Início:</strong> {formatDate(pedido.dataInicio)}</p>
            <p><strong>Previsão:</strong> {formatDate(pedido.dataPrevisao)}</p>
            <p><strong>Conclusão:</strong> {formatDate(pedido.dataConclusao)}</p>
          </CardContent>
        </Card>
        <Card className="md:col-span-2">
          <CardHeader>
            <CardTitle className="text-lg">Observações</CardTitle>
          </CardHeader>
          <CardContent className="text-sm">
            <p>{pedido.observacoes || "Nenhuma observação registrada."}</p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Histórico de Status</CardTitle>
        </CardHeader>
        <CardContent>
          {pedido.historicoStatus && pedido.historicoStatus.length > 0 ? (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Data/Hora</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead>Observação</TableHead>
                  <TableHead>Usuário</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {pedido.historicoStatus.map((historico, index) => (
                  <TableRow key={index}>
                    <TableCell>{formatDate(historico.dataHora)}</TableCell>
                    <TableCell>
                      <Badge className={`${getStatusColor(historico.status)} text-white`}>
                        {historico.status.replace("_", " ")}
                      </Badge>
                    </TableCell>
                    <TableCell>{historico.observacao}</TableCell>
                    <TableCell>{historico.usuario}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          ) : (
            <p className="text-sm text-muted-foreground">Nenhum histórico de status disponível.</p>
          )}
        </CardContent>
      </Card>

      <div className="mt-8 flex justify-end">
        <Link href="/dashboard">
          <Button variant="outline">Voltar ao Dashboard</Button>
        </Link>
      </div>
    </div>
  )
}