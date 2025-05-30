"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { useToast } from "@/components/ui/use-toast"
import { PerfilResponseDTO, createPerfil, listPerfis, updatePerfil } from "@/lib/api-config" // Assuming API functions for profiles
import { useEffect, useState } from "react"

export default function ProfileManagementPage() {
  const { toast } = useToast()

  const [searchQuery, setSearchQuery] = useState("")
  const [profiles, setProfiles] = useState<PerfilResponseDTO[]>([])
  const [editingProfile, setEditingProfile] = useState<PerfilResponseDTO | null>(null)
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false)

  const filteredProfiles = profiles.filter(profile =>
    profile.nome?.toLowerCase().includes(searchQuery.toLowerCase())
  )

  const [newProfile, setNewProfile] = useState<Omit<PerfilResponseDTO, 'id'>>({
    nome: "",
    descricao: "",
  })

  useEffect(() => {
    const fetchData = async () => {
      try {
        const profilesData = await listPerfis()
        setProfiles(profilesData)
      } catch (error) {
        toast({
          title: "Erro ao carregar perfis",
          description: "Não foi possível buscar os perfis.",
          variant: "destructive",
        })
      }
    }
    fetchData()
  }, [toast])

  const handleAddProfile = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const createdProfile = await createPerfil(newProfile)
      setProfiles(prevProfiles => [...prevProfiles, createdProfile])
      toast({
        title: "Perfil adicionado com sucesso!",
        description: `O perfil "${createdProfile.nome}" foi cadastrado.`,
      })
      setNewProfile({ nome: "", descricao: "" })
    } catch (error) {
      toast({
        title: "Erro ao adicionar perfil",
        description: "Tente novamente mais tarde.",
        variant: "destructive",
      })
    }
  }

  const handleEditProfile = (profile: PerfilResponseDTO) => {
    setEditingProfile(profile)
    setIsEditDialogOpen(true)
  }

  const handleUpdateProfile = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!editingProfile || editingProfile.id === undefined) return

    try {
      const updatedProfileData = await updatePerfil(editingProfile.id, editingProfile)
      setProfiles(prevProfiles =>
        prevProfiles.map(p => (p.id === updatedProfileData.id ? updatedProfileData : p))
      )
      toast({
        title: "Perfil atualizado com sucesso!",
        description: `O perfil "${updatedProfileData.nome}" foi atualizado.`,
      })
      setIsEditDialogOpen(false)
      setEditingProfile(null)
    } catch (error) {
      toast({
        title: "Erro ao atualizar perfil",
        description: "Tente novamente mais tarde.",
        variant: "destructive",
      })
    }
  }

  return (
    <div className="container mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Gestão de Perfis</h1>
        <Dialog>
          <DialogTrigger asChild>
            <Button>Adicionar Perfil</Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Novo Perfil</DialogTitle>
            </DialogHeader>
            <form onSubmit={handleAddProfile} className="space-y-6">
              <div className="grid w-full gap-4">
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="nome">Nome do Perfil</Label>
                  <Input
                    id="nome"
                    value={newProfile.nome}
                    onChange={e => setNewProfile({ ...newProfile, nome: e.target.value })}
                    required
                  />
                </div>
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="descricao">Descrição</Label>
                  <Textarea
                    id="descricao"
                    value={newProfile.descricao}
                    onChange={e => setNewProfile({ ...newProfile, descricao: e.target.value })}
                  />
                </div>
              </div>
              <Button type="submit" className="w-full">Salvar</Button>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      <div className="space-y-4">
        <div className="flex w-full items-center justify-between space-x-2 pb-4">
          <Input
            placeholder="Buscar perfis..."
            value={searchQuery}
            onChange={e => setSearchQuery(e.target.value)}
            className="max-w-sm"
          />
        </div>

        <ul className="divide-y divide-gray-100 rounded-md border">
          {filteredProfiles.map(profile => (
            <li key={profile.id} className="flex items-center justify-between p-4 hover:bg-gray-50">
              <div className="min-w-0 flex-auto">
                <h2 className="min-w-0 text-sm font-semibold leading-6">
                  {profile.nome}
                </h2>
                {profile.descricao && (
                  <p className="mt-1 truncate text-xs leading-5 text-gray-500">
                    {profile.descricao}
                  </p>
                )}
              </div>
              <div className="flex items-center gap-x-2">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => handleEditProfile(profile)}
                >
                  Editar
                </Button>
                <Button variant="destructive" size="sm">
                  Remover
                </Button>
              </div>
            </li>
          ))}
        </ul>
      </div>

      {editingProfile && (
        <Dialog open={isEditDialogOpen} onOpenChange={setIsEditDialogOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Editar Perfil</DialogTitle>
            </DialogHeader>
            <form onSubmit={handleUpdateProfile} className="space-y-6">
              <div className="grid w-full gap-4">
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="edit-nome">Nome do Perfil</Label>
                  <Input
                    id="edit-nome"
                    value={editingProfile.nome || ""}
                    onChange={e => setEditingProfile({ ...editingProfile, nome: e.target.value })}
                    required
                  />
                </div>
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="edit-descricao">Descrição</Label>
                  <Textarea
                    id="edit-descricao"
                    value={editingProfile.descricao || ""}
                    onChange={e => setEditingProfile({ ...editingProfile, descricao: e.target.value })}
                  />
                </div>
              </div>
              <Button type="submit" className="w-full">Salvar Alterações</Button>
            </form>
          </DialogContent>
        </Dialog>
      )}
    </div>
  )
}
