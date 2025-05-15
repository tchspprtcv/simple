"use client"

import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { listPedidosDoUsuarioLogado, listServicosFavoritos } from "@/lib/api-services"
import { PedidoResponse, TipoServico } from "@/lib/types"
import { useToast } from "@/components/ui/use-toast"
import { Skeleton } from "@/components/ui/skeleton"
import Link from "next/link"
import { useAuth } from "@/lib/auth-context"
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
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { StarIcon } from "lucide-react"

export default function DashboardPage() {
  const [dashboardItems, setDashboardItems] = useState<PedidoResponse[]>([])
  const [favoriteServices, setFavoriteServices] = useState<TipoServico[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const { toast } = useToast()
  const { user } = useAuth()

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [pedidosData, favoritesData] = await Promise.all([
          listPedidosDoUsuarioLogado(),
          listServicosFavoritos()
        ])
        
        console.log('Pedidos Data from API:', pedidosData); // Adicionado para depuração
        setDashboardItems(pedidosData.content)
        console.log('Dashboard Items after set:', pedidosData.content); // Adicionado para depuração
        setFavoriteServices(favoritesData)
      } catch (error: any) {
        toast({
          title: "Erro ao carregar dados",
          description: error.message || "Não foi possível carregar os dados do dashboard.",
          variant: "destructive",
        })
      } finally {
        setIsLoading(false)
      }
    }

    fetchData()
  }, [toast])

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
      return format(new Date(dateString), "dd/MM/yyyy", { locale: ptBR })
    } catch (error) {
      return "Data inválida"
    }
  }

  return (
    <div className="container mx-auto p-8 space-y-8">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold tracking-tight">Dashboard</h1>
        <div className="flex gap-3">
          <Link href="/dashboard/admin/services">
            <Button variant="outline" className="shadow-sm">Gerenciar Serviços</Button>
          </Link>
          <Link href="/dashboard/new-request">
            <Button className="shadow-sm">Novo Pedido</Button>
          </Link>
        </div>
      </div>

      <Tabs defaultValue="pedidos" className="space-y-6">
        <TabsList className="w-full sm:w-auto border-b">
          <TabsTrigger value="pedidos" className="text-base">Meus Pedidos</TabsTrigger>
          <TabsTrigger value="favoritos" className="text-base">Serviços Favoritos</TabsTrigger>
        </TabsList>
        
        <TabsContent value="pedidos" className="space-y-4">
          <Card className="shadow-md">
            <CardHeader className="space-y-1">
              <CardTitle className="text-2xl">Pedidos Recentes</CardTitle>
            </CardHeader>
            <CardContent>
              {isLoading ? (
                <div className="space-y-3">
                  {[1, 2, 3].map((i) => (
                    <Skeleton key={i} className="h-14 w-full" />
                  ))}
                </div>
              ) : dashboardItems.length > 0 ? (
                <div className="rounded-md border">
                  <Table>
                    <TableHeader>
                      <TableRow className="bg-muted/50">
                        <TableHead className="font-semibold">Código</TableHead>
                        <TableHead className="font-semibold">Serviço</TableHead>
                        <TableHead className="font-semibold">Data</TableHead>
                        <TableHead className="font-semibold">Status</TableHead>
                        <TableHead className="font-semibold">Ações</TableHead>
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {dashboardItems.map((item) => (
                        <TableRow key={item.id} className="hover:bg-muted/50">
                          <TableCell className="font-medium">{item.codigoAcompanhamento}</TableCell>
                          <TableCell>{item.tipoServico}</TableCell>
                          <TableCell>{formatDate(item.dataInicio)}</TableCell>
                          <TableCell>
                            <Badge className={`${getStatusColor(item.status)} text-white`}>
                              {item.status}
                            </Badge>
                          </TableCell>
                          <TableCell>
                            <Link href={`/dashboard/requests/${item.id}`}>
                              <Button variant="outline" size="sm" className="hover:bg-muted">Detalhes</Button>
                            </Link>
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </div>
              ) : (
                <div className="text-center py-10 space-y-3">
                  <p className="text-muted-foreground text-lg">Nenhum pedido encontrado</p>
                  <Link href="/dashboard/new-request">
                    <Button className="shadow-sm">Criar Novo Pedido</Button>
                  </Link>
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>
        
        <TabsContent value="favoritos" className="space-y-4">
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {isLoading ? (
              [1, 2, 3].map((i) => (
                <Card key={i} className="shadow-md">
                  <CardHeader>
                    <Skeleton className="h-7 w-3/4 mb-2" />
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <Skeleton className="h-4 w-full" />
                    <Skeleton className="h-10 w-full" />
                  </CardContent>
                </Card>
              ))
            ) : favoriteServices.length > 0 ? (
              favoriteServices.map((service) => (
                <Card key={service.id} className="shadow-md hover:shadow-lg transition-shadow">
                  <CardHeader>
                    <CardTitle className="flex items-center text-xl">
                      {service.nome}
                      <StarIcon className="ml-2 h-5 w-5 fill-yellow-400 text-yellow-400" />
                    </CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <p className="text-muted-foreground">
                      {service.descricao || "Sem descrição disponível"}
                    </p>
                    <Link href={`/dashboard/new-request?serviceId=${service.id}`}>
                      <Button className="w-full shadow-sm">Solicitar Serviço</Button>
                    </Link>
                  </CardContent>
                </Card>
              ))
            ) : (
              <div className="col-span-full text-center py-10 space-y-3">
                <p className="text-muted-foreground text-lg">Nenhum serviço favorito encontrado</p>
                <Link href="/services">
                  <Button className="shadow-sm">Explorar Serviços</Button>
                </Link>
              </div>
            )}
          </div>
        </TabsContent>
      </Tabs>
    </div>
  )
}
