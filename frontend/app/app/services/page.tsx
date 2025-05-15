"use client"

import { useEffect, useState } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { listTiposServicos } from "@/lib/api-services"
import { TipoServico } from "@/lib/types"
import { useToast } from "@/components/ui/use-toast"
import { Skeleton } from "@/components/ui/skeleton"
import Link from "next/link"
import { useAuth } from "@/lib/auth-context"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

export default function ServicesPage() {
  const [services, setServices] = useState<TipoServico[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [selectedCategory, setSelectedCategory] = useState<string>('all')
  const { toast } = useToast()
  const { isAuthenticated } = useAuth()

  useEffect(() => {
    const fetchServices = async () => {
      try {
        const data = await listTiposServicos()
        setServices(data)
      } catch (error: any) {
        toast({
          title: "Erro ao carregar serviços",
          description: error.message || "Não foi possível carregar a lista de serviços.",
          variant: "destructive",
        })
      } finally {
        setIsLoading(false)
      }
    }

    fetchServices()
  }, [toast])

  // Agrupar serviços por categoria
  const servicesByCategory: Record<string, TipoServico[]> = {}
  services.forEach(service => {
    if (!servicesByCategory[service.categoriaNome]) {
      servicesByCategory[service.categoriaNome] = []
    }
    servicesByCategory[service.categoriaNome].push(service)
  })

  // Obter lista única de categorias
  const categories = Object.keys(servicesByCategory)

  // Filtrar serviços pela categoria selecionada
  const filteredServices = selectedCategory === 'all' 
    ? servicesByCategory 
    : { [selectedCategory]: servicesByCategory[selectedCategory] }

  return (
    <div className="container mx-auto p-8 space-y-8">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <h1 className="text-3xl font-bold tracking-tight">Serviços Disponíveis</h1>
        
        <div className="w-full sm:w-[280px]">
          <Select value={selectedCategory} onValueChange={setSelectedCategory}>
            <SelectTrigger className="h-10">
              <SelectValue placeholder="Filtrar por categoria" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">Todas as categorias</SelectItem>
              {categories.map((categoryId) => (
                <SelectItem key={categoryId} value={categoryId}>
                  {categoryId}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>
      
      {isLoading ? (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[1, 2, 3, 4, 5, 6].map((i) => (
            <Card key={i} className="shadow-sm hover:shadow-lg transition-shadow">
              <CardHeader>
                <Skeleton className="h-7 w-3/4 mb-2" />
                <Skeleton className="h-4 w-1/2" />
              </CardHeader>
              <CardContent>
                <Skeleton className="h-4 w-full mb-4" />
                <Skeleton className="h-10 w-full" />
              </CardContent>
            </Card>
          ))}
        </div>
      ) : (
        <div className="space-y-10">
          {Object.entries(filteredServices).map(([category, categoryServices]) => (
            <div key={category}>
              <h2 className="text-2xl font-semibold mb-6">{category}</h2>
              <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                {categoryServices.map((service) => (
                  <Card key={service.id} className="shadow-sm hover:shadow-lg transition-shadow">
                    <CardHeader>
                      <CardTitle className="text-xl">{service.nome}</CardTitle>
                      <CardDescription>Código: {service.codigo}</CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                      <p className="text-muted-foreground">{service.descricao || "Sem descrição disponível"}</p>
                      {service.prazoEstimado && (
                        <p className="text-sm">Prazo estimado: {service.prazoEstimado} dias</p>
                      )}
                      {service.valorBase && (
                        <p className="text-sm">Valor base: {service.valorBase.toFixed(2)} CVE </p>
                      )}
                      {isAuthenticated ? (
                        <Link href={`/dashboard/new-request?serviceId=${service.id}`}>
                          <Button className="w-full shadow-sm">Solicitar Serviço</Button>
                        </Link>
                      ) : (
                        <Link href="/login">
                          <Button className="w-full shadow-sm">Entrar para Solicitar</Button>
                        </Link>
                      )}
                    </CardContent>
                  </Card>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
