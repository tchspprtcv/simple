"use client"

import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { listPedidosDoUsuarioLogado } from "@/lib/api-services"
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

export default function RequestsPage() {
  const [requests, setRequests] = useState<PedidoResponse[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [filter, setFilter] = useState({
    status: "",
    date: "",
  })
  const { toast } = useToast()

  useEffect(() => {
    fetchRequests()
  }, [filter])

  const fetchRequests = async () => {
    try {
      const response = await listPedidosDoUsuarioLogado()
      setRequests(response.content)
    } catch (error: any) {
      toast({
        title: "Erro ao carregar requisições",
        description: error.message || "Não foi possível carregar as requisições.",
        variant: "destructive",
      })
    } finally {
      setIsLoading(false)
    }
  }

  const getStatusColor = (status: string) => {
    const colors: { [key: string]: string } = {
      PENDING: "bg-yellow-200 text-yellow-800",
      IN_PROGRESS: "bg-blue-200 text-blue-800",
      COMPLETED: "bg-green-200 text-green-800",
      CANCELLED: "bg-red-200 text-red-800",
    }
    return colors[status] || "bg-gray-200 text-gray-800"
  }

  return (
    <div className="space-y-6 p-6 pb-16">
      <div className="flex flex-col space-y-4 md:flex-row md:items-center md:justify-between md:space-y-0">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Minhas Requisições</h2>
          <p className="text-muted-foreground">
            Gerencie e acompanhe todas as suas requisições em um só lugar.
          </p>
        </div>
        <Link href="/dashboard/new-request">
          <Button>Nova Requisição</Button>
        </Link>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Filtros</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-2">
            <Select
              value={filter.status}
              onValueChange={(value) => setFilter({ ...filter, status: value })}
            >
              <SelectTrigger>
                <SelectValue placeholder="Status" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                <SelectItem value="PENDING">Pendente</SelectItem>
                <SelectItem value="IN_PROGRESS">Em Andamento</SelectItem>
                <SelectItem value="COMPLETED">Concluído</SelectItem>
                <SelectItem value="CANCELLED">Cancelado</SelectItem>
              </SelectContent>
            </Select>
            <Input
              type="date"
              value={filter.date}
              onChange={(e) => setFilter({ ...filter, date: e.target.value })}
            />
          </div>
        </CardContent>
      </Card>

      <Card className="overflow-hidden">
        <CardContent className="p-0">
          {isLoading ? (
            <div className="p-6 space-y-4">
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-full" />
              <Skeleton className="h-4 w-full" />
            </div>
          ) : requests.length === 0 ? (
            <div className="flex flex-col items-center justify-center p-6 min-h-[300px] text-center">
              <p className="text-lg font-medium mb-2">Nenhuma requisição encontrada</p>
              <p className="text-sm text-muted-foreground mb-4">
                Você ainda não possui requisições. Que tal criar uma nova?
              </p>
              <Link href="/dashboard/new-request">
                <Button>Criar Nova Requisição</Button>
              </Link>
            </div>
          ) : (
            <div className="relative overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Código</TableHead>
                    <TableHead>Serviço</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Data</TableHead>
                    <TableHead className="text-right">Ações</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {requests.map((request) => (
                    <TableRow key={request.id}>
                      <TableCell className="font-medium">
                        {request.trackingCode}
                      </TableCell>
                      <TableCell>{request.service.name}</TableCell>
                      <TableCell>
                        <Badge className={getStatusColor(request.service.currentStatus)}>
                          {request.service.currentStatus}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        {format(new Date(request.createdAt), "dd/MM/yyyy", {
                          locale: ptBR,
                        })}
                      </TableCell>
                      <TableCell className="text-right">
                        <Link href={`/dashboard/requests/${request.id}`}>
                          <Button variant="outline" size="sm">
                            Detalhes
                          </Button>
                        </Link>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}