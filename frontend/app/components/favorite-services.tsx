"use client"

import { Card, CardHeader, CardTitle, CardContent } from "./ui/card"

export function FavoriteServices() {
  const favorites = [
    { id: 1, name: "Legalização", description: "Processo de legalização de documentos" },
    { id: 2, name: "Compra de Lote", description: "Processo de compra de lotes municipais" },
    { id: 3, name: "Licenciamento Comercial", description: "Licenciamento para atividades comerciais" }
  ]

  return (
    <div className="grid gap-4 md:grid-cols-2">
      {favorites.map((service) => (
        <Card key={service.id} className="hover:shadow-lg transition-shadow">
          <CardHeader>
            <CardTitle className="text-lg">{service.name}</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-sm text-muted-foreground">{service.description}</p>
          </CardContent>
        </Card>
      ))}
    </div>
  )
}