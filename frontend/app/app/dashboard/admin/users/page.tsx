"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { useToast } from "@/components/ui/use-toast"
import { useState } from "react"

export default function UserManagementPage() {
  const { toast } = useToast()
  const [users, setUsers] = useState([
    { id: 1, name: "João Silva", email: "joao@example.com", role: "CITIZEN" },
    { id: 2, name: "Maria Santos", email: "maria@example.com", role: "ATTENDANT" },
    { id: 3, name: "Pedro Costa", email: "pedro@example.com", role: "TECHNICIAN" },
  ])

  const handleRoleChange = async (userId: number, newRole: string) => {
    try {
      // Update user role logic here
      toast({
        title: "Função atualizada com sucesso!",
        description: "A função do utilizador foi alterada.",
      })
    } catch (error) {
      toast({
        title: "Erro ao atualizar função",
        description: "Tente novamente mais tarde.",
        variant: "destructive",
      })
    }
  }

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Gestão de Utilizadores</h1>
      <div className="mb-6">
        <Input
          placeholder="Buscar utilizadores..."
          className="max-w-md"
        />
      </div>
      <div className="grid gap-4">
        {users.map((user) => (
          <Card key={user.id}>
            <CardContent className="flex items-center justify-between p-4">
              <div>
                <h3 className="font-semibold">{user.name}</h3>
                <p className="text-sm text-gray-500">{user.email}</p>
              </div>
              <div className="flex items-center gap-4">
                <Select
                  defaultValue={user.role}
                  onValueChange={(value) => handleRoleChange(user.id, value)}
                >
                  <SelectTrigger className="w-[180px]">
                    <SelectValue placeholder="Selecione a função" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="ADMIN">Administrador</SelectItem>
                    <SelectItem value="MANAGER">Gerente</SelectItem>
                    <SelectItem value="ATTENDANT">Atendente</SelectItem>
                    <SelectItem value="TECHNICIAN">Técnico</SelectItem>
                    <SelectItem value="CITIZEN">Utente</SelectItem>
                  </SelectContent>
                </Select>
                <Button variant="destructive">Remover</Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
}