"use client"

import { Input } from "./ui/input"
import { Button } from "./ui/button"
import { Search } from "lucide-react"

export function ServiceSearch() {
  return (
    <div className="flex gap-2">
      <Input
        placeholder="Buscar serviços..."
        className="flex-1"
      />
      <Button>
        <Search className="h-4 w-4 mr-2" />
        Buscar
      </Button>
    </div>
  )
}