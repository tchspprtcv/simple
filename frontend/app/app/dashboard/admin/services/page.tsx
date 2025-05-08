"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { useToast } from "@/components/ui/use-toast"
import { createTipoServico } from "@/lib/api-services" // Importar a nova função
import { useState } from "react"

export default function ServiceManagementPage() {
  const { toast } = useToast()
  // Definir um tipo para os serviços no estado local, compatível com TipoServico
  // Para simplificar, vamos usar uma interface que espelhe os campos relevantes de TipoServico
  // e os campos que já estavam sendo usados no mock.
  interface ServiceDisplayItem {
    id: number;
    nome: string; // Alterado de name
    categoria: string; // Alterado de category
    description?: string;
    codigo?: string;
    // Adicione outros campos de TipoServico que você deseja exibir ou usar no estado
    requerVistoria?: boolean;
    ativo?: boolean;
  }

  const [services, setServices] = useState<ServiceDisplayItem[]>([
    { id: 1, nome: "Manutenção de Vias", categoria: "Infraestrutura", description: "Serviço de manutenção e reparo de vias públicas", codigo: "INF001", ativo: true, requerVistoria: true },
    { id: 2, nome: "Coleta de Lixo", categoria: "Limpeza Urbana", description: "Serviço de coleta de resíduos domésticos", codigo: "LIM002", ativo: true, requerVistoria: false },
    { id: 3, nome: "Poda de Árvores", categoria: "Meio Ambiente", description: "Serviço de poda e manutenção de árvores", codigo: "AMB003", ativo: true, requerVistoria: false },
  ])

  const [newService, setNewService] = useState({
    nome: "", // Alterado de name para nome
    codigo: "",
    categoria: "", // Alterado de category para categoria
    description: "",
    requerVistoria: false, // Adicionado
    requerAnaliseTecnica: false, // Adicionado
    requerAprovacao: false, // Adicionado
    disponivelPortal: false, // Adicionado
    ativo: true, // Adicionado (geralmente um novo serviço começa ativo)
    // Campos opcionais podem ser adicionados aqui se necessário:
    // prazoEstimado: 0,
    // valorBase: 0,
  })

  // Certifique-se de que o tipo do estado corresponde ao que createTipoServico espera
  // O tipo TipoServico em types.ts usa 'nome' e 'categoria' (minúsculas)
  // O erro indica que Omit<TipoServico, "id"> espera 'nome' e 'categoria'

  const handleAddService = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const createdService = await createTipoServico(newService); // Chamar a API
      // Atualizar o estado local para refletir a adição
      // createdService é do tipo TipoServico. O estado services agora é ServiceDisplayItem[]
      // Certifique-se de que o objeto adicionado corresponda a ServiceDisplayItem
      const newServiceEntry: ServiceDisplayItem = {
        ...createdService, // Espalha todos os campos de TipoServico
        id: services.length > 0 ? Math.max(...services.map(s => s.id)) + 1 : 1, // Simula ID
        // description já vem de createdService.descricao se existir
      };
      setServices((prevServices) => [...prevServices, newServiceEntry]); 
      toast({
        title: "Serviço adicionado com sucesso!",
        description: `O serviço "${createdService.nome}" foi cadastrado no sistema.`, // Alterado de createdService.name para createdService.nome
      })
      setNewService({
        nome: "", // Alterado de name para nome
        codigo: "",
        categoria: "", // Alterado de category para categoria
        description: "",
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

  return (
    <div className="container mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Gestão de Serviços</h1>
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
                <Label htmlFor="categoria">Categoria</Label>
                <Input
                  id="categoria"
                  value={newService.categoria} // Alterado de newService.category
                  onChange={(e) => setNewService({ ...newService, categoria: e.target.value })} // Alterado de category para categoria
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="description">Descrição</Label>
                <Textarea
                  id="description"
                  value={newService.description}
                  onChange={(e) => setNewService({ ...newService, description: e.target.value })}
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
      </div>

      <div className="grid gap-4">
        {services.map((service) => (
          <Card key={service.id}>
            <CardHeader>
              <CardTitle>{service.nome}</CardTitle> {/* Alterado para service.nome */}
            </CardHeader>
            <CardContent>
              <p className="text-sm text-gray-500 mb-2">{service.categoria}</p> {/* Alterado para service.categoria */}
              <p>{service.description}</p>
              <div className="flex gap-2 mt-4">
                <Button variant="outline">Editar</Button>
                <Button variant="destructive">Remover</Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
}