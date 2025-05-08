"use client";

import { useAuth } from "@/lib/auth-context";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useEffect, useState } from "react";
import { UsuarioResponse } from "@/lib/types";
import { updateUserProfile } from "@/lib/api-services"; // Assuming you'll create this API service
import { useToast } from "@/components/ui/use-toast";

export default function ProfilePage() {
  const { user, isLoading, checkAuth } = useAuth();
  const { toast } = useToast();
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState<Partial<UsuarioResponse>>({
    nome: "",
    email: "",
  });

  useEffect(() => {
    if (user) {
      setFormData({
        nome: user.nome,
        email: user.email,
      });
    }
  }, [user]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user || !user.id) return;

    try {
      // const updatedUser = await updateUserProfile(user.id, {
      //   nome: formData.nome,
      //   // email: formData.email, // Usually email is not updatable or requires verification
      // });
      // await checkAuth(); // Refresh user data
      toast({
        title: "Perfil atualizado",
        description: "Suas informações foram salvas com sucesso.",
      });
      setIsEditing(false);
      // Simulate API call for now
      console.log("Profile update submitted:", formData);
    } catch (error) {
      toast({
        title: "Erro ao atualizar perfil",
        description: "Não foi possível salvar suas informações. Tente novamente.",
        variant: "destructive",
      });
      console.error("Failed to update profile:", error);
    }
  };

  if (isLoading) {
    return <div className="container mx-auto p-6"><p>Carregando perfil...</p></div>;
  }

  if (!user) {
    return <div className="container mx-auto p-6"><p>Você precisa estar logado para ver esta página.</p></div>;
  }

  return (
    <div className="container mx-auto p-6">
      <Card className="max-w-2xl mx-auto">
        <CardHeader>
          <CardTitle className="text-2xl">Meu Perfil</CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          <div className="flex items-center space-x-4">
            <Avatar className="h-20 w-20">
              <AvatarImage src={`https://avatar.vercel.sh/${user.email}.png`} alt={user.nome} />
              <AvatarFallback>{user.nome?.charAt(0).toUpperCase()}</AvatarFallback>
            </Avatar>
            <div>
              <h2 className="text-xl font-semibold">{user.nome}</h2>
              <p className="text-muted-foreground">{user.email}</p>
            </div>
          </div>

          {isEditing ? (
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <Label htmlFor="nome">Nome Completo</Label>
                <Input id="nome" name="nome" value={formData.nome || ''} onChange={handleInputChange} />
              </div>
              <div>
                <Label htmlFor="email">Email</Label>
                <Input id="email" name="email" type="email" value={formData.email || ''} onChange={handleInputChange} disabled />
                <p className="text-sm text-muted-foreground mt-1">O email não pode ser alterado.</p>
              </div>
              {/* Adicionar mais campos editáveis aqui, como telefone, etc. */}
              <div className="flex space-x-2">
                <Button type="submit">Salvar Alterações</Button>
                <Button variant="outline" onClick={() => setIsEditing(false)}>Cancelar</Button>
              </div>
            </form>
          ) : (
            <div className="space-y-2">
              <p><strong>Nome:</strong> {user.nome}</p>
              <p><strong>Email:</strong> {user.email}</p>
              <p><strong>Perfil:</strong> {user.perfil}</p>
              <p><strong>Último Acesso:</strong> {user.ultimoAcesso ? new Date(user.ultimoAcesso).toLocaleString() : 'N/A'}</p>
              <Button onClick={() => setIsEditing(true)}>Editar Perfil</Button>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}