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
    categoriaId: number;
    categoriaNome?: string;
    formKey?: string;
    readEndpoint?: string;
    writeEndpoint?: string;
  }

  // Estados
  const [searchQuery, setSearchQuery] = useState("")
  const [services, setServices] = useState<ServiceDisplayItem[]>([])
  const [editingService, setEditingService] = useState<ServiceDisplayItem | null>(null);
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);
  // Filtra os serviços baseado na busca
  const filteredServices = services.filter(service =>
    service.nome?.toLowerCase().includes(searchQuery.toLowerCase()) ||
    service.codigo?.toLowerCase().includes(searchQuery.toLowerCase())
  )

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
    categoriaNome: "",
    formKey: "",
    readEndpoint: "",
    writeEndpoint: "",
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
        formKey: "",
        readEndpoint: "",
        writeEndpoint: "",
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
              <form onSubmit={handleAddService} className="space-y-6">
                <div className="grid w-full gap-4">
                  <div className="flex flex-col space-y-1.5">
                    <Label htmlFor="nome">Nome do Serviço</Label>
                    <Input
                      id="nome"
                      value={newService.nome}
                      onChange={(e) => setNewService({ ...newService, nome: e.target.value })}
                      required
                    />
                  </div>
                  <div className="flex flex-col space-y-1.5">
                    <Label htmlFor="codigo">Código do Serviço</Label>
                    <Input
                      id="codigo"
                      value={newService.codigo}
                      onChange={(e) => setNewService({ ...newService, codigo: e.target.value })}
                      required
                    />
                  </div>
                  <div className="flex flex-col space-y-1.5">
                    <Label htmlFor="categoriaId">Categoria</Label>
                    <select
                      id="categoriaId"
                      value={newService.categoriaId}
                      onChange={(e) => setNewService({ ...newService, categoriaId: parseInt(e.target.value, 10) })}
                      className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
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
                  <div className="flex flex-col space-y-1.5">
                    <Label htmlFor="descricao">Descrição</Label>
                    <Textarea
                      id="descricao"
                      value={newService.descricao}
                      onChange={(e) => setNewService({ ...newService, descricao: e.target.value })}
                      required
                    />
                  </div>
                  <div className="flex flex-col space-y-1.5">
                    <Label htmlFor="formKey">FormKey</Label>
                    <Input
                      id="formKey"
                      value={newService.formKey}
                      onChange={(e) => setNewService({ ...newService, formKey: e.target.value })}
                    />
                  </div>
                  <div className="flex flex-col space-y-1.5">
                    <Label htmlFor="readEndpoint">Read Endpoint</Label>
                    <Input
                      id="readEndpoint"
                      value={newService.readEndpoint}
                      onChange={(e) => setNewService({ ...newService, readEndpoint: e.target.value })}
                    />
                  </div>
                  <div className="flex flex-col space-y-1.5">
                    <Label htmlFor="writeEndpoint">Write Endpoint</Label>
                    <Input
                      id="writeEndpoint"
                      value={newService.writeEndpoint}
                      onChange={(e) => setNewService({ ...newService, writeEndpoint: e.target.value })}
                    />
                  </div>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div className="flex items-center space-x-2">
                    <Input type="checkbox" id="requerVistoria" className="h-4 w-4" checked={newService.requerVistoria} onChange={(e) => setNewService({ ...newService, requerVistoria: e.target.checked })} />
                    <Label htmlFor="requerVistoria">Requer Vistoria</Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Input type="checkbox" id="requerAnaliseTecnica" className="h-4 w-4" checked={newService.requerAnaliseTecnica} onChange={(e) => setNewService({ ...newService, requerAnaliseTecnica: e.target.checked })} />
                    <Label htmlFor="requerAnaliseTecnica">Requer Análise Técnica</Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Input type="checkbox" id="requerAprovacao" className="h-4 w-4" checked={newService.requerAprovacao} onChange={(e) => setNewService({ ...newService, requerAprovacao: e.target.checked })} />
                    <Label htmlFor="requerAprovacao">Requer Aprovação</Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Input type="checkbox" id="disponivelPortal" className="h-4 w-4" checked={newService.disponivelPortal} onChange={(e) => setNewService({ ...newService, disponivelPortal: e.target.checked })} />
                    <Label htmlFor="disponivelPortal">Disponível no Portal</Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Input type="checkbox" id="ativo" className="h-4 w-4" checked={newService.ativo} onChange={(e) => setNewService({ ...newService, ativo: e.target.checked })} />
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

      <div className="space-y-4">
          <div className="flex w-full items-center justify-between space-x-2 pb-4">
            <Input
              placeholder="Buscar serviços..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="max-w-sm"
            />
          </div>

          <ul className="divide-y divide-gray-100 rounded-md border">
            {filteredServices.map((service) => (
              <li key={service.id} className="flex items-center justify-between p-4 hover:bg-gray-50">
                <div className="min-w-0 flex-auto">
                  <div className="flex items-center gap-x-3">
                    <h2 className="min-w-0 text-sm font-semibold leading-6">
                      {service.nome}
                    </h2>
                    <p className="rounded-md bg-gray-50 px-2 py-1 text-xs font-medium text-gray-600">
                      {service.codigo}
                    </p>
                  </div>
                  <div className="mt-3 flex items-center gap-x-2.5 text-xs leading-5 text-gray-500">
                    <p className="truncate">{service.categoriaNome}</p>
                    {service.descricao && (
                      <>
                        <svg className="h-0.5 w-0.5 fill-gray-300" viewBox="0 0 2 2">
                          <circle cx={1} cy={1} r={1} />
                        </svg>
                        <p className="truncate">{service.descricao}</p>
                      </>
                    )}
                  </div>
                </div>
                <div className="flex items-center gap-x-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleEditService(service)}
                  >
                    Editar
                  </Button>
                  <Button
                    variant="destructive"
                    size="sm"
                  >
                    Remover
                  </Button>
                </div>
              </li>
            ))}
          </ul>

      {/* Edit Service Dialog */}
      {editingService && (
        <Dialog open={isEditDialogOpen} onOpenChange={setIsEditDialogOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Editar Serviço</DialogTitle>
            </DialogHeader>
            <form onSubmit={handleUpdateService} className="space-y-6">
              <div className="grid w-full gap-4">
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="edit-nome">Nome do Serviço</Label>
                  <Input
                    id="edit-nome"
                    value={editingService.nome || ''}
                    onChange={(e) => setEditingService({ ...editingService, nome: e.target.value })}
                    required
                  />
                </div>
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="edit-codigo">Código do Serviço</Label>
                  <Input
                    id="edit-codigo"
                    value={editingService.codigo || ''}
                    onChange={(e) => setEditingService({ ...editingService, codigo: e.target.value })}
                    required
                  />
                </div>
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="edit-categoriaId">Categoria</Label>
                  <select
                    id="edit-categoriaId"
                    value={editingService.categoriaId || 0}
                    onChange={(e) => setEditingService({ ...editingService, categoriaId: parseInt(e.target.value, 10) })}
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
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
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="edit-descricao">Descrição</Label>
                  <Textarea
                    id="edit-descricao"
                    value={editingService.descricao || ''}
                    onChange={(e) => setEditingService({ ...editingService, descricao: e.target.value })}
                    required
                  />
                </div>
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="edit-formKey">FormKey</Label>
                  <Input
                    id="edit-formKey"
                    value={editingService.formKey || ''}
                    onChange={(e) => setEditingService({ ...editingService, formKey: e.target.value })}
                  />
                </div>
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="edit-readEndpoint">Read Endpoint</Label>
                  <Input
                    id="edit-readEndpoint"
                    value={editingService.readEndpoint || ''}
                    onChange={(e) => setEditingService({ ...editingService, readEndpoint: e.target.value })}
                  />
                </div>
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="edit-writeEndpoint">Write Endpoint</Label>
                  <Input
                    id="edit-writeEndpoint"
                    value={editingService.writeEndpoint || ''}
                    onChange={(e) => setEditingService({ ...editingService, writeEndpoint: e.target.value })}
                  />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="flex items-center space-x-2">
                  <Input type="checkbox" id="edit-requerVistoria" className="h-4 w-4" checked={editingService.requerVistoria || false} onChange={(e) => setEditingService({ ...editingService, requerVistoria: e.target.checked })} />
                  <Label htmlFor="edit-requerVistoria">Requer Vistoria</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <Input type="checkbox" id="edit-requerAnaliseTecnica" className="h-4 w-4" checked={editingService.requerAnaliseTecnica || false} onChange={(e) => setEditingService({ ...editingService, requerAnaliseTecnica: e.target.checked })} />
                  <Label htmlFor="edit-requerAnaliseTecnica">Requer Análise Técnica</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <Input type="checkbox" id="edit-requerAprovacao" className="h-4 w-4" checked={editingService.requerAprovacao || false} onChange={(e) => setEditingService({ ...editingService, requerAprovacao: e.target.checked })} />
                  <Label htmlFor="edit-requerAprovacao">Requer Aprovação</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <Input type="checkbox" id="edit-disponivelPortal" className="h-4 w-4" checked={editingService.disponivelPortal || false} onChange={(e) => setEditingService({ ...editingService, disponivelPortal: e.target.checked })} />
                  <Label htmlFor="edit-disponivelPortal">Disponível no Portal</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <Input type="checkbox" id="edit-ativo" className="h-4 w-4" checked={editingService.ativo || false} onChange={(e) => setEditingService({ ...editingService, ativo: e.target.checked })} />
                  <Label htmlFor="edit-ativo">Ativo</Label>
                </div>
              </div>
              <Button type="submit" className="w-full">Salvar Alterações</Button>
            </form>
          </DialogContent>
        </Dialog>
      )}
    </div>
  </div>
  )
}