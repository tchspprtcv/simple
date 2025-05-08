"use client"

import { Button } from "@/components/ui/button"
import Link from "next/link"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/components/ui/use-toast"
import { Star, StarOff, Loader2 } from "lucide-react"
import { listServicosFavoritos, toggleFavorito } from "@/lib/api-services" // Supondo que toggleFavorito exista ou será criada
import { TipoServico } from "@/lib/types" // Supondo que TipoServico seja o tipo correto para favoritos
import { useEffect } from "react"
import { useState } from "react"

export default function FavoritesPage() {
  const { toast } = useToast()
  const [favorites, setFavorites] = useState<TipoServico[]>([])
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const fetchFavorites = async () => {
      try {
        const data = await listServicosFavoritos()
        setFavorites(data)
      } catch (error) {
        toast({
          title: "Erro ao carregar favoritos",
          description: "Tente novamente mais tarde.",
          variant: "destructive",
        })
      }
      setIsLoading(false)
    }
    fetchFavorites()
  }, [toast])

  const toggleFavorite = async (serviceId: number) => {
    const originalFavorites = [...favorites]
    try {
      // Atualiza o estado local imediatamente para feedback rápido
      setFavorites(favorites.map(fav => 
        fav.id === serviceId 
          ? { ...fav, isFavorito: !fav.isFavorito } // Assumindo que a propriedade é isFavorito no backend
          : fav
      ))

      await toggleFavorito(serviceId) // Chama a API para persistir a mudança
      
      toast({
        title: "Favoritos atualizados",
        description: "Suas preferências foram salvas com sucesso.",
      })
    } catch (error) {
      // Reverte para o estado original em caso de erro
      setFavorites(originalFavorites)
      toast({
        title: "Erro ao atualizar favoritos",
        description: "Tente novamente mais tarde.",
        variant: "destructive",
      })
    }
  }

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Serviços Favoritos</h1>
      {isLoading && (
        <div className="flex justify-center items-center h-64">
          <Loader2 className="h-8 w-8 animate-spin text-primary" />
        </div>
      )}
      {!isLoading && favorites.length === 0 && (
        <div className="text-center py-8">
          <p className="text-muted-foreground">Nenhum serviço favorito encontrado.</p>
          <Link href="/services"> {/* Adapte o link para a página de explorar serviços se necessário */}
            <Button className="mt-4">Explorar Serviços</Button>
          </Link>
        </div>
      )}
      {!isLoading && favorites.length > 0 && (
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {favorites.map((service) => (
          <Card key={service.id} className="hover:shadow-lg transition-shadow">
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="text-xl">{service.nome}</CardTitle>
              <Button
                variant="ghost"
                size="icon"
                onClick={() => toggleFavorite(service.id)}
              >
                {service.isFavorito ? (
                  <Star className="h-5 w-5 text-yellow-500" />
                ) : (
                  <StarOff className="h-5 w-5" />
                )}
              </Button>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-gray-500">{service.categoriaNome || service.categoria}</p>
              <div className="mt-4">
                <Link href={`/dashboard/new-request?serviceId=${service.id}`}>
                <Button className="w-full">Iniciar Atendimento</Button>
              </Link>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
      )}
    </div>
  )
}