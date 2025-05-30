"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useToast } from "@/components/ui/use-toast"
import { getPedidoByCodigo } from "@/lib/api-services"
import { PedidoResponse } from "@/lib/types"
import { useState } from "react"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"

export default function TrackPage() {
  const [codigo, setCodigo] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const [pedido, setPedido] = useState<PedidoResponse | null>(null)
  const { toast } = useToast()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)
    
    try {
      const result = await getPedidoByCodigo(codigo)
      setPedido(result)
    } catch (error: any) {
      toast({
        title: "Erro ao buscar pedido",
        description: error.message || "Não foi possível encontrar o pedido com o código informado.",
        variant: "destructive",
      })
      setPedido(null)
    } finally {
      setIsLoading(false)
    }
  }

  const getStatusColor = (status: string) => {
    const statusMap: Record<string, string> = {
      'NOVO': 'bg-blue-500',
      'EM_ANALISE': 'bg-yellow-500',
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

  const formatDate = (dateString: string) => {
    try {
      return format(new Date(dateString), "dd/MM/yyyy 'às' HH:mm", { locale: ptBR })
    } catch (error) {
      return "Data inválida"
    }
  }

  return (
    <div className="container mx-auto py-8">
      <Card className="max-w-md mx-auto mb-8">
        <CardHeader>
          <CardTitle>Acompanhar Pedido</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="codigo">Código de Acompanhamento</Label>
              <Input
                id="codigo"
                placeholder="Digite o código do seu pedido"
                type="text"
                value={codigo}
                onChange={(e) => setCodigo(e.target.value)}
                disabled={isLoading}
                required
              />
            </div>
            <Button type="submit" className="w-full" disabled={isLoading}>
              {isLoading ? "Consultando..." : "Consultar"}
            </Button>
          </form>
        </CardContent>
      </Card>

      {pedido && (
        <Card>
          <CardHeader>
            <CardTitle className="flex justify-between items-center">
              <span>Pedido: {pedido.codigoAcompanhamento}</span>
              <Badge className={getStatusColor(pedido.status)}>{pedido.status}</Badge>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Table>
              <TableBody>
                <TableRow>
                  <TableCell className="font-medium">Tipo de Serviço</TableCell>
                  <TableCell>{pedido.tipoServico}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell className="font-medium">Utente</TableCell>
                  <TableCell>{pedido.cidadao}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell className="font-medium">Data de Início</TableCell>
                  <TableCell>{formatDate(pedido.dataInicio)}</TableCell>
                </TableRow>
                {pedido.dataPrevisao && (
                  <TableRow>
                    <TableCell className="font-medium">Previsão de Conclusão</TableCell>
                    <TableCell>{formatDate(pedido.dataPrevisao)}</TableCell>
                  </TableRow>
                )}
                {pedido.dataConclusao && (
                  <TableRow>
                    <TableCell className="font-medium">Data de Conclusão</TableCell>
                    <TableCell>{formatDate(pedido.dataConclusao)}</TableCell>
                  </TableRow>
                )}
                <TableRow>
                  <TableCell className="font-medium">Etapa Atual</TableCell>
                  <TableCell>{pedido.etapaAtual || "Não definida"}</TableCell>
                </TableRow>
                {pedido.observacoes && (
                  <TableRow>
                    <TableCell className="font-medium">Observações</TableCell>
                    <TableCell>{pedido.observacoes}</TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
