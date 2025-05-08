"use client"

import { Suspense } from "react"
import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { createCidadao, createPedido, getCidadaoByDocumento, listTiposServicos } from "@/lib/api-services"
import { CidadaoRequest, PedidoRequest, TipoServico } from "@/lib/types"
import { useToast } from "@/components/ui/use-toast"
import { useAuth } from "@/lib/auth-context"
import { useRouter, useSearchParams } from "next/navigation"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
import { InfoIcon } from "lucide-react"

export default function NewRequestPage() {
  return (
    <Suspense>
      <NewRequestContent />
    </Suspense>
  )
}

function NewRequestContent() {
  const [tiposServicos, setTiposServicos] = useState<TipoServico[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [selectedServiceId, setSelectedServiceId] = useState<number | null>(null)
  const [selectedService, setSelectedService] = useState<TipoServico | null>(null)
  const [cidadaoTab, setCidadaoTab] = useState("existente")
  
  // Dados do cidadão existente
  const [tipoDocumento, setTipoDocumento] = useState("CPF")
  const [numeroDocumento, setNumeroDocumento] = useState("")
  const [cidadaoId, setCidadaoId] = useState<string | null>(null)
  const [cidadaoNome, setCidadaoNome] = useState("")
  const [cidadaoEncontrado, setCidadaoEncontrado] = useState(false)
  const [buscandoCidadao, setBuscandoCidadao] = useState(false)
  
  // Dados do novo cidadão
  const [novoCidadaoNome, setNovoCidadaoNome] = useState("")
  const [novoCidadaoTipoDocumento, setNovoCidadaoTipoDocumento] = useState("CPF")
  const [novoCidadaoNumeroDocumento, setNovoCidadaoNumeroDocumento] = useState("")
  const [novoCidadaoEmail, setNovoCidadaoEmail] = useState("")
  const [novoCidadaoTelefone, setNovoCidadaoTelefone] = useState("")
  const [novoCidadaoEndereco, setNovoCidadaoEndereco] = useState("")
  
  // Dados do pedido
  const [observacoes, setObservacoes] = useState("")
  const [prioridade, setPrioridade] = useState("0")
  
  const { toast } = useToast()
  const { user } = useAuth()
  const router = useRouter()
  const searchParams = useSearchParams()
  
  useEffect(() => {
    const fetchServices = async () => {
      try {
        const data = await listTiposServicos()
        setTiposServicos(data)
        
        // Verificar se há um serviço pré-selecionado na URL
        const serviceIdParam = searchParams.get('serviceId')
        if (serviceIdParam) {
          const serviceId = parseInt(serviceIdParam)
          setSelectedServiceId(serviceId)
          
          const service = data.find(s => s.id === serviceId)
          if (service) {
            setSelectedService(service)
          }
        }
      } catch (error: any) {
        toast({
          title: "Erro ao carregar serviços",
          description: error.message || "Não foi possível carregar a lista de serviços.",
          variant: "destructive",
        })
      } finally {
        setIsLoading(false)
      }
    }

    fetchServices()
  }, [toast, searchParams])
  
  const buscarCidadao = async () => {
    if (!tipoDocumento || !numeroDocumento) {
      toast({
        title: "Campos obrigatórios",
        description: "Preencha o tipo e número do documento.",
        variant: "destructive",
      })
      return
    }
    
    setBuscandoCidadao(true)
    try {
      const cidadao = await getCidadaoByDocumento(tipoDocumento, numeroDocumento)
      setCidadaoId(cidadao.id)
      setCidadaoNome(cidadao.nome)
      setCidadaoEncontrado(true)
      
      toast({
        title: "Cidadão encontrado",
        description: `${cidadao.nome} foi encontrado com sucesso.`,
      })
    } catch (error: any) {
      setCidadaoEncontrado(false)
      setCidadaoId(null)
      setCidadaoNome("")
      
      toast({
        title: "Cidadão não encontrado",
        description: "Cidadão não encontrado. Tente novamente ou cadastre um novo cidadão.",
        variant: "destructive",
      })
    } finally {
      setBuscandoCidadao(false)
    }
  }
  
  const handleServiceChange = (serviceId: string) => {
    const id = parseInt(serviceId)
    setSelectedServiceId(id)
    
    const service = tiposServicos.find(s => s.id === id)
    if (service) {
      setSelectedService(service)
    } else {
      setSelectedService(null)
    }
  }
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!selectedServiceId) {
      toast({
        title: "Serviço não selecionado",
        description: "Selecione um tipo de serviço para continuar.",
        variant: "destructive",
      })
      return
    }
    
    let cidadaoIdToUse = cidadaoId
    
    setIsSubmitting(true)
    try {
      // Se for um novo cidadão, cadastrar primeiro
      if (cidadaoTab === "novo") {
        if (!novoCidadaoNome || !novoCidadaoTipoDocumento || !novoCidadaoNumeroDocumento) {
          throw new Error("Preencha os campos obrigatórios do cidadão.")
        }
        
        const novoCidadaoRequest: CidadaoRequest = {
          nome: novoCidadaoNome,
          tipoDocumento: novoCidadaoTipoDocumento,
          numeroDocumento: novoCidadaoNumeroDocumento,
          email: novoCidadaoEmail || undefined,
          telefone: novoCidadaoTelefone || undefined,
          endereco: novoCidadaoEndereco || undefined,
        }
        
        const novoCidadao = await createCidadao(novoCidadaoRequest)
        cidadaoIdToUse = novoCidadao.id
      } else if (!cidadaoId) {
        throw new Error("Cidadão não encontrado. Busque um cidadão existente ou cadastre um novo.")
      }
      
      // Criar o pedido
      const pedidoRequest: PedidoRequest = {
        tipoServicoId: selectedServiceId,
        cidadaoId: cidadaoIdToUse!,
        origem: "PORTAL",
        prioridade: parseInt(prioridade),
        observacoes: observacoes || undefined,
      }
      
      const pedido = await createPedido(pedidoRequest)
      
      toast({
        title: "Pedido criado com sucesso!",
        description: `Código de acompanhamento: ${pedido.codigoAcompanhamento}`,
      })
      
      // Redirecionar para a página de detalhes do pedido
      router.push(`/dashboard/requests/${pedido.id}`)
    } catch (error: any) {
      toast({
        title: "Erro ao criar pedido",
        description: error.message || "Não foi possível criar o pedido. Tente novamente.",
        variant: "destructive",
      })
    } finally {
      setIsSubmitting(false)
    }
  }
  
  return (
    <div className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-8">Novo Pedido</h1>
      
      <form onSubmit={handleSubmit} className="space-y-8">
        <Card>
          <CardHeader>
            <CardTitle>Tipo de Serviço</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="serviceType">Selecione o tipo de serviço</Label>
                <Select 
                  value={selectedServiceId?.toString() || ""}
                  onValueChange={handleServiceChange}
                  disabled={isLoading}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Selecione um serviço" />
                  </SelectTrigger>
                  <SelectContent>
                    {tiposServicos.map((service) => (
                      <SelectItem key={service.id} value={service.id.toString()}>
                        {service.nome}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              
              {selectedService && (
                <Alert>
                  <InfoIcon className="h-4 w-4" />
                  <AlertTitle>{selectedService.nome}</AlertTitle>
                  <AlertDescription>
                    {selectedService.descricao || "Sem descrição disponível"}
                    {selectedService.prazoEstimado && (
                      <p className="mt-2">Prazo estimado: {selectedService.prazoEstimado} dias</p>
                    )}
                    {selectedService.valorBase && (
                      <p>Valor base: R$ {selectedService.valorBase.toFixed(2)}</p>
                    )}
                  </AlertDescription>
                </Alert>
              )}
            </div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader>
            <CardTitle>Cidadão</CardTitle>
          </CardHeader>
          <CardContent>
            <Tabs value={cidadaoTab} onValueChange={setCidadaoTab}>
              <TabsList className="mb-6">
                <TabsTrigger value="existente">Cidadão Existente</TabsTrigger>
                <TabsTrigger value="novo">Novo Cidadão</TabsTrigger>
              </TabsList>
              
              <TabsContent value="existente">
                <div className="space-y-4">
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                      <Label htmlFor="tipoDocumento">Tipo de Documento</Label>
                      <Select 
                        value={tipoDocumento} 
                        onValueChange={setTipoDocumento}
                        disabled={buscandoCidadao}
                      >
                        <SelectTrigger>
                          <SelectValue placeholder="Tipo de Documento" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="CPF">CPF</SelectItem>
                          <SelectItem value="RG">RG</SelectItem>
                          <SelectItem value="PASSAPORTE">Passaporte</SelectItem>
                          <SelectItem value="CNH">CNH</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                    
                    <div>
                      <Label htmlFor="numeroDocumento">Número do Documento</Label>
                      <Input
                        id="numeroDocumento"
                        value={numeroDocumento}
                        onChange={(e) => setNumeroDocumento(e.target.value)}
                        disabled={buscandoCidadao}
                        required
                      />
                    </div>
                    
                    <div className="flex items-end">
                      <Button 
                        type="button" 
                        onClick={buscarCidadao} 
                        disabled={buscandoCidadao || !tipoDocumento || !numeroDocumento}
                        className="w-full"
                      >
                        {buscandoCidadao ? "Buscando..." : "Buscar Cidadão"}
                      </Button>
                    </div>
                  </div>
                  
                  {cidadaoEncontrado && (
                    <Alert className="bg-green-50 dark:bg-green-950">
                      <InfoIcon className="h-4 w-4" />
                      <AlertTitle>Cidadão Encontrado</AlertTitle>
                      <AlertDescription>
                        Nome: {cidadaoNome}
                      </AlertDescription>
                    </Alert>
                  )}
                </div>
              </TabsContent>
              
              <TabsContent value="novo">
                <div className="space-y-4">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <Label htmlFor="novoCidadaoNome">Nome Completo</Label>
                      <Input
                        id="novoCidadaoNome"
                        value={novoCidadaoNome}
                        onChange={(e) => setNovoCidadaoNome(e.target.value)}
                        required
                      />
                    </div>
                    
                    <div>
                      <Label htmlFor="novoCidadaoEmail">Email</Label>
                      <Input
                        id="novoCidadaoEmail"
                        type="email"
                        value={novoCidadaoEmail}
                        onChange={(e) => setNovoCidadaoEmail(e.target.value)}
                      />
                    </div>
                  </div>
                  
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                      <Label htmlFor="novoCidadaoTipoDocumento">Tipo de Documento</Label>
                      <Select 
                        value={novoCidadaoTipoDocumento} 
                        onValueChange={setNovoCidadaoTipoDocumento}
                      >
                        <SelectTrigger>
                          <SelectValue placeholder="Tipo de Documento" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="CPF">CPF</SelectItem>
                          <SelectItem value="RG">RG</SelectItem>
                          <SelectItem value="PASSAPORTE">Passaporte</SelectItem>
                          <SelectItem value="CNH">CNH</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                    
                    <div>
                      <Label htmlFor="novoCidadaoNumeroDocumento">Número do Documento</Label>
                      <Input
                        id="novoCidadaoNumeroDocumento"
                        value={novoCidadaoNumeroDocumento}
                        onChange={(e) => setNovoCidadaoNumeroDocumento(e.target.value)}
                        required
                      />
                    </div>
                    
                    <div>
                      <Label htmlFor="novoCidadaoTelefone">Telefone</Label>
                      <Input
                        id="novoCidadaoTelefone"
                        value={novoCidadaoTelefone}
                        onChange={(e) => setNovoCidadaoTelefone(e.target.value)}
                      />
                    </div>
                  </div>
                  
                  <div>
                    <Label htmlFor="novoCidadaoEndereco">Endereço</Label>
                    <Textarea
                      id="novoCidadaoEndereco"
                      value={novoCidadaoEndereco}
                      onChange={(e) => setNovoCidadaoEndereco(e.target.value)}
                      rows={3}
                    />
                  </div>
                </div>
              </TabsContent>
            </Tabs>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader>
            <CardTitle>Detalhes do Pedido</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div>
                <Label htmlFor="observacoes">Observações</Label>
                <Textarea
                  id="observacoes"
                  value={observacoes}
                  onChange={(e) => setObservacoes(e.target.value)}
                  placeholder="Informações adicionais sobre o pedido"
                  rows={4}
                />
              </div>
              
              <div>
                <Label htmlFor="prioridade">Prioridade</Label>
                <Select value={prioridade} onValueChange={setPrioridade}>
                  <SelectTrigger>
                    <SelectValue placeholder="Selecione a prioridade" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="0">Normal</SelectItem>
                    <SelectItem value="1">Alta</SelectItem>
                    <SelectItem value="2">Urgente</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>
          </CardContent>
        </Card>
        
        <div className="flex justify-end gap-4">
          <Button 
            type="button" 
            variant="outline" 
            onClick={() => router.push('/dashboard')}
            disabled={isSubmitting}
          >
            Cancelar
          </Button>
          <Button 
            type="submit" 
            disabled={isSubmitting || (!cidadaoId && cidadaoTab === "existente") || !selectedServiceId}
          >
            {isSubmitting ? "Enviando..." : "Criar Pedido"}
          </Button>
        </div>
      </form>
    </div>
  )
}