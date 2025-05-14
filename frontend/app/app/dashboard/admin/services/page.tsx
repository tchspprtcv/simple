"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { useToast } from "@/components/ui/use-toast"
import { useConfiguracoes } from "@/hooks/use-configuracoes"
import {
  createTipoServico,
  listTiposServicos,
  TipoServico,
  updateTipoServico
} from "@/lib/api-services"
import { useEffect, useState } from "react"

export default function ServiceManagementPage() {
  const { toast } = useToast()
  const { categoriasServico } = useConfiguracoes()

  interface ServiceDisplayItem extends Partial<Omit<TipoServico, 'categoriaId'>> {
    categoriaId: number
    categoriaNome?: string;
  }

  const [services, setServices] = useState<ServiceDisplayItem[]>([])
  const [editingService, setEditingService] = useState<ServiceDisplayItem | null>(null);
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);

  const [newService, setNewService] = useState<Omit<TipoServico, 'id'>>({
    nome: "",
    codigo: "",
    categoriaId: 0,
    requerVistoria: false,
    requerAnaliseTecnica: false,
    requerAprovacao: false,
    disponivelPortal: false,
    ativo: true,
    isFavorito: false,
    categoriaNome: ""
  })

  // Buscar tipos de serviço ao carregar a página
  useEffect(() => {
    const fetchData = async () => {
      try {
        const tiposServicosData = await listTiposServicos();
        // Mapear tipos de serviço para ServiceDisplayItem, incluindo nome da categoria
        const servicesComCategoriaNome = tiposServicosData.map(ts => {
          const categoria = categoriasServico.find(cat => cat.id === ts.categoriaId);
          return {
            ...ts,
            categoriaNome: categoria ? categoria.nome : "Sem categoria",
          };
        });
        setServices(servicesComCategoriaNome);
      } catch (error) {
        toast({
          title: "Erro ao carregar dados",
          description: "Não foi possível buscar os tipos de serviço.",
          variant: "destructive",
        });
      }
    };
    fetchData();
  }, [toast, categoriasServico]);

  const handleAddService = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const createdService = await createTipoServico(newService);
      // Atualizar o estado local para refletir a adição
      const categoriaDoNovoServico = categoriasServico.find(cat => cat.id === createdService.categoriaId);
      const newServiceEntry: ServiceDisplayItem = {
        ...createdService,
        categoriaNome: categoriaDoNovoServico ? categoriaDoNovoServico.nome : "Sem categoria",
      };
      setServices((prevServices) => [...prevServices, newServiceEntry]);
      toast({
        title: "Serviço adicionado com sucesso!",
        description: `O serviço "${createdService.nome}" foi cadastrado no sistema.`, // Alterado de createdService.name para createdService.nome
      })
      setNewService({
        nome: "",
        isFavorito: false,
        categoriaNome: "",
        codigo: "",
        categoriaId: 0, // Resetar para um valor numérico
        descricao: "",
        requerVistoria: false,
        requerAnaliseTecnica: false,
        requerAprovacao: false,
        disponivelPortal: false,
        ativo: true,
      }) // Limpar o formulário
    } catch (error) {
      toast({
        title: "Erro ao adicionar serviço",
        description: "Tente novamente mais tarde.",
        variant: "destructive",
      })
    }
  }

  const handleEditService = (service: ServiceDisplayItem) => {
    setEditingService(service);
    setIsEditDialogOpen(true);
  };

  const handleUpdateService = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingService || editingService.id === undefined) return;

    try {
      // Certifique-se de que editingService.id não é undefined antes de chamar a API
      const serviceToUpdate = {
        ...editingService,
        id: editingService.id, // Garante que o id está presente
        // @ts-ignore
        categoriaId: editingService.categoriaId || 0, // Garante que categoriaId é um número
      };
      const updatedServiceData = await updateTipoServico(editingService.id, serviceToUpdate);
      const categoriaDoServicoAtualizado = categoriasServico.find(cat => cat.id === updatedServiceData.categoriaId);
      const updatedServiceEntry: ServiceDisplayItem = {
        ...updatedServiceData,
        categoriaNome: categoriaDoServicoAtualizado ? categoriaDoServicoAtualizado.nome : "Sem categoria",
      };

      setServices((prevServices) =>
        prevServices.map((s) => (s.id === updatedServiceEntry.id ? updatedServiceEntry : s))
      );
      toast({
        title: "Serviço atualizado com sucesso!",
        description: `O serviço "${updatedServiceEntry.nome}" foi atualizado.`,
      });
      setIsEditDialogOpen(false);
      setEditingService(null);
    } catch (error) {
      toast({
        title: "Erro ao atualizar serviço",
        description: "Tente novamente mais tarde.",
        variant: "destructive",
      });
    }
  };

  return (
    <div className="container mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Gestão de Serviços</h1>
        <div className="flex gap-4">
          <Dialog>
            <DialogTrigger asChild>
              <Button>Adicionar Serviço</Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Novo Serviço</DialogTitle>
              </DialogHeader>
              <form onSubmit={handleAddService} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="nome">Nome do Serviço</Label>
                  <Input
                    id="nome"
                    value={newService.nome} // Alterado de newService.name
                    onChange={(e) => setNewService({ ...newService, nome: e.target.value })} // Alterado de name para nome
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="codigo">Código do Serviço</Label>
                  <Input
                    id="codigo"
                    value={newService.codigo}
                    onChange={(e) => setNewService({ ...newService, codigo: e.target.value })}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="categoriaId">Categoria</Label>
                  <select
                    id="categoriaId"
                    value={newService.categoriaId}
                    onChange={(e) => setNewService({ ...newService, categoriaId: parseInt(e.target.value, 10) })}
                    className="w-full p-2 border rounded-md"
                    required
                  >
                    <option value="0" disabled>Selecione uma categoria</option>
                    {categoriasServico.map((cat) => (
                      <option key={cat.id} value={cat.id}>
                        {cat.nome}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="descricao">Descrição</Label>
                  <Textarea
                    id="descricao"
                    value={newService.descricao} // Alterado para newService.descricao
                    onChange={(e) => setNewService({ ...newService, descricao: e.target.value })}
                    required
                  />
                </div>
                {/* Campos Booleanos */}
                <div className="grid grid-cols-2 gap-4">
                  <div className="flex items-center space-x-2">
                    <Input type="checkbox" id="requerVistoria" checked={newService.requerVistoria} onChange={(e) => setNewService({ ...newService, requerVistoria: e.target.checked })} />
                    <Label htmlFor="requerVistoria">Requer Vistoria</Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Input type="checkbox" id="requerAnaliseTecnica" checked={newService.requerAnaliseTecnica} onChange={(e) => setNewService({ ...newService, requerAnaliseTecnica: e.target.checked })} />
                    <Label htmlFor="requerAnaliseTecnica">Requer Análise Técnica</Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Input type="checkbox" id="requerAprovacao" checked={newService.requerAprovacao} onChange={(e) => setNewService({ ...newService, requerAprovacao: e.target.checked })} />
                    <Label htmlFor="requerAprovacao">Requer Aprovação</Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Input type="checkbox" id="disponivelPortal" checked={newService.disponivelPortal} onChange={(e) => setNewService({ ...newService, disponivelPortal: e.target.checked })} />
                    <Label htmlFor="disponivelPortal">Disponível no Portal</Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Input type="checkbox" id="ativo" checked={newService.ativo} onChange={(e) => setNewService({ ...newService, ativo: e.target.checked })} />
                    <Label htmlFor="ativo">Ativo</Label>
                  </div>
                </div>
                <Button type="submit" className="w-full">Salvar</Button>
              </form>
            </DialogContent>
          </Dialog>
          <Button
            variant="outline"
            onClick={async () => {
              try {
                const tiposServicosData = await listTiposServicos();
                const servicesComCategoria = tiposServicosData.map(ts => ({
                  ...ts,
                  requerVistoria: true,
                  requerAnaliseTecnica: true,
                  requerAprovacao: true,
                  disponivelPortal: true,
                  ativo: true,
                  isFavorito: false
                }));
                setServices(servicesComCategoria);
                toast({
                  title: "Serviços carregados",
                  description: "Lista de serviços foi carregada com sucesso."
                });
              } catch (error) {
                toast({
                  title: "Erro ao carregar serviços",
                  description: "Não foi possível carregar a lista de serviços.",
                  variant: "destructive"
                });
              }
            }}
          >
            Carregar Serviços Padrão
          </Button>
        </div>
      </div>

      <div className="grid gap-4">
        {services.map((service) => (
          <Card key={service.id}>
            <CardHeader>
              <CardTitle>{service.nome}</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-gray-500 mb-2">{service.categoriaNome}</p>
              <p>{service.descricao}</p>
              <div className="flex gap-2 mt-4">
                <Button variant="outline" onClick={() => handleEditService(service)}>Editar</Button>
                <Button variant="destructive">Remover</Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Edit Service Dialog */}
      {editingService && (
        <Dialog open={isEditDialogOpen} onOpenChange={setIsEditDialogOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Editar Serviço</DialogTitle>
            </DialogHeader>
            <form onSubmit={handleUpdateService} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="edit-nome">Nome do Serviço</Label>
                <Input
                  id="edit-nome"
                  value={editingService.nome || ''}
                  onChange={(e) => setEditingService({ ...editingService, nome: e.target.value })}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="edit-codigo">Código do Serviço</Label>
                <Input
                  id="edit-codigo"
                  value={editingService.codigo || ''}
                  onChange={(e) => setEditingService({ ...editingService, codigo: e.target.value })}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="edit-categoriaId">Categoria</Label>
                <select
                  id="edit-categoriaId"
                  value={editingService.categoriaId || 0}
                  onChange={(e) => setEditingService({ ...editingService, categoriaId: parseInt(e.target.value, 10) })}
                  className="w-full p-2 border rounded-md"
                  required
                >
                  <option value="0" disabled>Selecione uma categoria</option>
                  {categoriasServico.map((cat) => (
                    <option key={cat.id} value={cat.id}>
                      {cat.nome}
                    </option>
                  ))}
                </select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="edit-descricao">Descrição</Label>
                <Textarea
                  id="edit-descricao"
                  value={editingService.descricao || ''}
                  onChange={(e) => setEditingService({ ...editingService, descricao: e.target.value })}
                  required
                />
              </div>
              {/* Campos Booleanos */}
              <div className="grid grid-cols-2 gap-4">
                 <div className="flex items-center space-x-2">
                  <Input type="checkbox" id="edit-requerVistoria" checked={editingService.requerVistoria || false} onChange={(e) => setEditingService({ ...editingService, requerVistoria: e.target.checked })} />
                  <Label htmlFor="edit-requerVistoria">Requer Vistoria</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <Input type="checkbox" id="edit-requerAnaliseTecnica" checked={editingService.requerAnaliseTecnica || false} onChange={(e) => setEditingService({ ...editingService, requerAnaliseTecnica: e.target.checked })} />
                  <Label htmlFor="edit-requerAnaliseTecnica">Requer Análise Técnica</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <Input type="checkbox" id="edit-requerAprovacao" checked={editingService.requerAprovacao || false} onChange={(e) => setEditingService({ ...editingService, requerAprovacao: e.target.checked })} />
                  <Label htmlFor="edit-requerAprovacao">Requer Aprovação</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <Input type="checkbox" id="edit-disponivelPortal" checked={editingService.disponivelPortal || false} onChange={(e) => setEditingService({ ...editingService, disponivelPortal: e.target.checked })} />
                  <Label htmlFor="edit-disponivelPortal">Disponível no Portal</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <Input type="checkbox" id="edit-ativo" checked={editingService.ativo || false} onChange={(e) => setEditingService({ ...editingService, ativo: e.target.checked })} />
                  <Label htmlFor="edit-ativo">Ativo</Label>
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