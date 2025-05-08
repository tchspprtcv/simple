"use client"

import { Input } from "./ui/input"
import { Button } from "./ui/button"
import { SearchCode } from "lucide-react"

export function OrderTracking() {
  return (
    <div className="flex gap-2">
      <Input
        placeholder="Digite o código de acompanhamento..."
        className="flex-1"
      />
      <Button>
        <SearchCode className="h-4 w-4 mr-2" />
        Consultar
      </Button>
    </div>
  )
}