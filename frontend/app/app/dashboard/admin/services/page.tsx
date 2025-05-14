"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { useToast } from "@/components/ui/use-toast"
import {
  createTipoServico,
  listCategoriasServicos,
  listTiposServicos,
  CategoriaServico, // Importar o tipo CategoriaServico
  TipoServico,      // Importar o tipo TipoServico
} from "@/lib/api-services"
import { useEffect, useState } from "react" // Adicionar useEffect

export default function ServiceManagementPage() {
  const { toast } = useToast()
  // Definir um tipo para os serviços no estado local, compatível com TipoServico
  // Para simplificar, vamos usar uma interface que espelhe os campos relevantes de TipoServico
  // e os campos que já estavam sendo usados no mock.
  interface ServiceDisplayItem extends Partial<Omit<TipoServico, 'categoriaId'>> {
    categoriaNome?: string; // Para exibir o nome da categoria
  }

  // Estado para armazenar as categorias de serviço
  const [categorias, setCategorias] = useState<CategoriaServico[]>([])

  const [services, setServices] = useState<ServiceDisplayItem[]>([])

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

  // Buscar categorias e tipos de serviço ao carregar a página
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [categoriasData, tiposServicosData] = await Promise.all([
          listCategoriasServicos(),
          listTiposServicos(),
        ]);
        setCategorias(categoriasData);

        // Mapear tipos de serviço para ServiceDisplayItem, incluindo nome da categoria
        const servicesComCategoriaNome = tiposServicosData.map(ts => {
          const categoria = categoriasData.find(cat => cat.id === ts.categoriaId);
          return {
            ...ts,
            categoriaNome: categoria ? categoria.nome : "Sem categoria",
          };
        });
        setServices(servicesComCategoriaNome);

      } catch (error) {
        toast({
          title: "Erro ao carregar dados",
          description: "Não foi possível buscar categorias ou tipos de serviço.",
          variant: "destructive",
        });
      }
    };
    fetchData();
  }, [toast]);

  const handleAddService = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const createdService = await createTipoServico(newService); // Chamar a API
      // Atualizar o estado local para refletir a adição
      const categoriaDoNovoServico = categorias.find(cat => cat.id === createdService.categoriaId);
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
                <Label htmlFor="categoriaId">Categoria</Label>
                <select
                  id="categoriaId"
                  value={newService.categoriaId}
                  onChange={(e) => setNewService({ ...newService, categoriaId: parseInt(e.target.value, 10) })}
                  className="w-full p-2 border rounded-md"
                  required
                >
                  <option value="0" disabled>Selecione uma categoria</option>
                  {categorias.map((cat) => (
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