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

export default function ServicesPage() {
  const [services, setServices] = useState<TipoServico[]>([])
  const [isLoading, setIsLoading] = useState(true)
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
    if (!servicesByCategory[service.categoria]) {
      servicesByCategory[service.categoria] = []
    }
    servicesByCategory[service.categoria].push(service)
  })

  return (
    <div className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-8">Serviços Disponíveis</h1>
      
      {isLoading ? (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[1, 2, 3, 4, 5, 6].map((i) => (
            <Card key={i} className="hover:shadow-lg transition-shadow">
              <CardHeader>
                <Skeleton className="h-6 w-3/4 mb-2" />
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
        <>
          {Object.entries(servicesByCategory).map(([category, categoryServices]) => (
            <div key={category} className="mb-10">
              <h2 className="text-2xl font-semibold mb-4">{category}</h2>
              <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                {categoryServices.map((service) => (
                  <Card key={service.id} className="hover:shadow-lg transition-shadow">
                    <CardHeader>
                      <CardTitle>{service.nome}</CardTitle>
                      <CardDescription>Código: {service.codigo}</CardDescription>
                    </CardHeader>
                    <CardContent>
                      <p className="text-muted-foreground mb-4">{service.descricao || "Sem descrição disponível"}</p>
                      {service.prazoEstimado && (
                        <p className="text-sm mb-2">Prazo estimado: {service.prazoEstimado} dias</p>
                      )}
                      {service.valorBase && (
                        <p className="text-sm mb-4">Valor base: R$ {service.valorBase.toFixed(2)}</p>
                      )}
                      {isAuthenticated ? (
                        <Link href={`/dashboard/new-request?serviceId=${service.id}`}>
                          <Button className="w-full">Solicitar Serviço</Button>
                        </Link>
                      ) : (
                        <Link href="/login">
                          <Button className="w-full">Entrar para Solicitar</Button>
                        </Link>
                      )}
                    </CardContent>
                  </Card>
                ))}
              </div>
            </div>
          ))}
        </>
      )}
    </div>
  )
}
