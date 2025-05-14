"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { useToast } from "@/components/ui/use-toast"
import {
  createCategoriaServico,
  listCategoriasServicos,
  updateCategoriaServico,
  deleteCategoriaServico,
  CategoriaServico,
} from "@/lib/api-services"
import { useEffect, useState } from "react"

export default function CategoryManagementPage() {
  const { toast } = useToast()
  const [categories, setCategories] = useState<CategoriaServico[]>([])
  const [newCategory, setNewCategory] = useState<Omit<CategoriaServico, 'id'>>({
    nome: "",
    descricao: "",
    ativo: true,
  })
  const [editingCategory, setEditingCategory] = useState<CategoriaServico | null>(null)
  const [isDialogOpen, setIsDialogOpen] = useState(false)

  // Buscar categorias ao carregar a página
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const data = await listCategoriasServicos()
        setCategories(data)
      } catch (error) {
        toast({
          title: "Erro ao buscar categorias",
          description: "Não foi possível carregar a lista de categorias.",
          variant: "destructive",
        })
      }
    }
    fetchCategories()
  }, [toast])

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    if (editingCategory) {
      setEditingCategory({ ...editingCategory, [name]: name === 'ativo' ? (e.target as HTMLInputElement).checked : value });
    } else {
      setNewCategory({ ...newCategory, [name]: name === 'ativo' ? (e.target as HTMLInputElement).checked : value });
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      let savedCategory: CategoriaServico;
      if (editingCategory) {
        savedCategory = await updateCategoriaServico(editingCategory.id, editingCategory);
        setCategories(categories.map(cat => cat.id === savedCategory.id ? savedCategory : cat));
        toast({
          title: "Categoria atualizada!",
          description: `A categoria "${savedCategory.nome}" foi atualizada com sucesso.`,
        });
      } else {
        savedCategory = await createCategoriaServico(newCategory);
        setCategories([...categories, savedCategory]);
        toast({
          title: "Categoria criada!",
          description: `A categoria "${savedCategory.nome}" foi criada com sucesso.`,
        });
      }
      setNewCategory({ nome: "", descricao: "", ativo: true });
      setEditingCategory(null);
      setIsDialogOpen(false);
    } catch (error) {
      toast({
        title: "Erro ao salvar categoria",
        description: "Ocorreu um erro ao tentar salvar a categoria.",
        variant: "destructive",
      })
    }
  }

  const handleEdit = (category: CategoriaServico) => {
    setEditingCategory(category);
    setIsDialogOpen(true);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm("Tem certeza que deseja remover esta categoria?")) {
      try {
        await deleteCategoriaServico(id);
        setCategories(categories.filter(cat => cat.id !== id));
        toast({
          title: "Categoria removida!",
          description: "A categoria foi removida com sucesso.",
        });
      } catch (error) {
        toast({
          title: "Erro ao remover categoria",
          description: "Não foi possível remover a categoria. Verifique se ela não está associada a nenhum serviço.",
          variant: "destructive",
        });
      }
    }
  };

  const openNewCategoryDialog = () => {
    setEditingCategory(null);
    setNewCategory({ nome: "", descricao: "", ativo: true });
    setIsDialogOpen(true);
  }

  return (
    <div className="container mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Gestão de Categorias de Serviço</h1>
        <Button onClick={openNewCategoryDialog}>Adicionar Categoria</Button>
      </div>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{editingCategory ? "Editar Categoria" : "Nova Categoria"}</DialogTitle>
          </DialogHeader>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="nome">Nome da Categoria</Label>
              <Input
                id="nome"
                name="nome"
                value={editingCategory ? editingCategory.nome : newCategory.nome}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="descricao">Descrição</Label>
              <Textarea
                id="descricao"
                name="descricao"
                value={editingCategory ? editingCategory.descricao || '' : newCategory.descricao || ''}
                onChange={handleInputChange}
              />
            </div>
            <div className="flex items-center space-x-2">
                <Input 
                    type="checkbox" 
                    id="ativo"
                    name="ativo"
                    checked={editingCategory ? editingCategory.ativo : newCategory.ativo}
                    onChange={handleInputChange} 
                    className="h-4 w-4"
                />
                <Label htmlFor="ativo">Ativo</Label>
            </div>
            <Button type="submit" className="w-full">{editingCategory ? "Salvar Alterações" : "Criar Categoria"}</Button>
          </form>
        </DialogContent>
      </Dialog>

      <div className="grid gap-4">
        {categories.map((category) => (
          <Card key={category.id}>
            <CardHeader>
              <CardTitle className="flex justify-between items-center">
                {category.nome}
                <span className={`text-sm px-2 py-1 rounded-full ${category.ativo ? 'bg-green-200 text-green-800' : 'bg-red-200 text-red-800'}`}>
                  {category.ativo ? 'Ativa' : 'Inativa'}
                </span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-gray-500 mb-2">{category.descricao}</p>
              <div className="flex gap-2 mt-4">
                <Button variant="outline" onClick={() => handleEdit(category)}>Editar</Button>
                <Button variant="destructive" onClick={() => handleDelete(category.id)}>Remover</Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
}