"use client"

import { Suspense } from "react"
import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { createUtente, createPedido, getUtenteByDocumento, listTiposServicos } from "@/lib/api-services"
import { UtenteRequest, PedidoRequest, TipoServico } from "@/lib/types"
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
  
  // Dados do utente existente
  const [tipoDocumento, setTipoDocumento] = useState("CNI")
  const [numeroDocumento, setNumeroDocumento] = useState("")
  const [cidadaoId, setCidadaoId] = useState<string | null>(null)
  const [cidadaoNome, setCidadaoNome] = useState("")
  const [cidadaoEncontrado, setCidadaoEncontrado] = useState(false)
  const [buscandoCidadao, setBuscandoCidadao] = useState(false)
  
  // Dados do novo utente
  const [novoCidadaoNome, setNovoCidadaoNome] = useState("")
  const [novoCidadaoTipoDocumento, setNovoCidadaoTipoDocumento] = useState("CNI")
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
      const cidadao = await getUtenteByDocumento(tipoDocumento, numeroDocumento)
      setCidadaoId(cidadao.id)
      setCidadaoNome(cidadao.nome)
      setCidadaoEncontrado(true)
      
      toast({
        title: "Utente encontrado",
        description: `${cidadao.nome} foi encontrado com sucesso.`,
      })
    } catch (error: any) {
      setCidadaoEncontrado(false)
      setCidadaoId(null)
      setCidadaoNome("")
      
      toast({
        title: "Utente não encontrado",
        description: "Utente não encontrado. Tente novamente ou cadastre um novo utente.",
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
      // Se for um novo utente, cadastrar primeiro
      if (cidadaoTab === "novo") {
        if (!novoCidadaoNome || !novoCidadaoTipoDocumento || !novoCidadaoNumeroDocumento) {
          throw new Error("Preencha os campos obrigatórios do utente.")
        }
        
        const novoUtenteRequest: UtenteRequest = {
          nome: novoCidadaoNome,
          tipoDocumento: novoCidadaoTipoDocumento,
          numeroDocumento: novoCidadaoNumeroDocumento,
          email: novoCidadaoEmail || undefined,
          telefone: novoCidadaoTelefone || undefined,
          endereco: novoCidadaoEndereco || undefined,
        }
        
        const novoCidadao = await createUtente(novoUtenteRequest)
        cidadaoIdToUse = novoCidadao.id
      } else if (!cidadaoId) {
        throw new Error("Utente não encontrado. Busque um utente existente ou cadastre um novo.")
      }
      
      // Criar o pedido
      const pedidoRequest: PedidoRequest = {
        tipoServicoId: selectedServiceId!.toString(),
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
    <div className="container mx-auto p-8 space-y-8">
      <h1 className="text-3xl font-bold tracking-tight">Novo Pedido</h1>
      
      <form onSubmit={handleSubmit} className="space-y-6">
        <Card className="shadow-sm">
          <CardHeader className="space-y-1">
            <CardTitle className="text-2xl">Tipo de Serviço</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="serviceType" className="text-base">Selecione o tipo de serviço</Label>
                <Select 
                  value={selectedServiceId?.toString() || ""}
                  onValueChange={handleServiceChange}
                  disabled={isLoading}
                >
                  <SelectTrigger className="h-10">
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
                <Alert className="bg-muted/50">
                  <InfoIcon className="h-5 w-5" />
                  <AlertTitle className="font-semibold">{selectedService.nome}</AlertTitle>
                  <AlertDescription className="mt-2 space-y-2">
                    {selectedService.descricao || "Sem descrição disponível"}
                    {selectedService.prazoEstimado && (
                      <p>Prazo estimado: {selectedService.prazoEstimado} dias</p>
                    )}
                    {selectedService.valorBase && (
                      <p>Valor base: {selectedService.valorBase.toFixed(2)} CVE</p>
                    )}
                  </AlertDescription>
                </Alert>
              )}
            </div>
          </CardContent>
        </Card>
        
        <Card className="shadow-sm">
          <CardHeader className="space-y-1">
            <CardTitle className="text-2xl">Utente</CardTitle>
          </CardHeader>
          <CardContent>
            <Tabs value={cidadaoTab} onValueChange={setCidadaoTab} className="space-y-6">
              <TabsList className="w-full sm:w-auto border-b">
                <TabsTrigger value="existente" className="text-base">Utente Existente</TabsTrigger>
                <TabsTrigger value="novo" className="text-base">Novo Utente</TabsTrigger>
              </TabsList>
              
              <TabsContent value="existente" className="space-y-6">
                <div className="space-y-4">
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="tipoDocumento" className="text-base">Tipo de Documento</Label>
                      <Select 
                        value={tipoDocumento} 
                        onValueChange={setTipoDocumento}
                        disabled={buscandoCidadao}
                      >
                        <SelectTrigger className="h-10">
                          <SelectValue placeholder="Tipo de Documento" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="CNI">CNI</SelectItem>
                          <SelectItem value="BI">BI</SelectItem>
                          <SelectItem value="PASSAPORTE">Passaporte</SelectItem>
                          <SelectItem value="CC">Carta de Condução</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                    
                    <div className="space-y-2">
                      <Label htmlFor="numeroDocumento" className="text-base">Número do Documento</Label>
                      <Input
                        id="numeroDocumento"
                        value={numeroDocumento}
                        onChange={(e) => setNumeroDocumento(e.target.value)}
                        disabled={buscandoCidadao}
                        className="h-10"
                        required
                      />
                    </div>
                    
                    <div className="flex items-end">
                      <Button 
                        type="button" 
                        onClick={buscarCidadao} 
                        disabled={buscandoCidadao || !tipoDocumento || !numeroDocumento}
                        className="w-full h-10 shadow-sm"
                      >
                        {buscandoCidadao ? "Buscando..." : "Buscar Utente"}
                      </Button>
                    </div>
                  </div>
                  
                  {cidadaoEncontrado && (
                    <Alert className="bg-green-50 dark:bg-green-950 border-green-200 dark:border-green-900">
                      <InfoIcon className="h-5 w-5 text-green-600 dark:text-green-400" />
                      <AlertTitle className="font-semibold text-green-800 dark:text-green-200">Utente Encontrado</AlertTitle>
                      <AlertDescription className="text-green-700 dark:text-green-300">
                        Nome: {cidadaoNome}
                      </AlertDescription>
                    </Alert>
                  )}
                </div>
              </TabsContent>
              
              <TabsContent value="novo" className="space-y-6">
                <div className="space-y-4">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="novoCidadaoNome" className="text-base">Nome Completo</Label>
                      <Input
                        id="novoCidadaoNome"
                        value={novoCidadaoNome}
                        onChange={(e) => setNovoCidadaoNome(e.target.value)}
                        className="h-10"
                        required
                      />
                    </div>
                    
                    <div className="space-y-2">
                      <Label htmlFor="novoCidadaoEmail" className="text-base">Email</Label>
                      <Input
                        id="novoCidadaoEmail"
                        type="email"
                        value={novoCidadaoEmail}
                        onChange={(e) => setNovoCidadaoEmail(e.target.value)}
                        className="h-10"
                      />
                    </div>
                  </div>
                  
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div className="space-y-2">
                      <Label htmlFor="novoCidadaoTipoDocumento" className="text-base">Tipo de Documento</Label>
                      <Select 
                        value={novoCidadaoTipoDocumento} 
                        onValueChange={setNovoCidadaoTipoDocumento}
                      >
                        <SelectTrigger className="h-10">
                          <SelectValue placeholder="Tipo de Documento" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="CNI">CNI</SelectItem>
                          <SelectItem value="BI">BI</SelectItem>
                          <SelectItem value="PASSAPORTE">Passaporte</SelectItem>
                          <SelectItem value="CC">Carta de Condução</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                    
                    <div className="space-y-2">
                      <Label htmlFor="novoCidadaoNumeroDocumento" className="text-base">Número do Documento</Label>
                      <Input
                        id="novoCidadaoNumeroDocumento"
                        value={novoCidadaoNumeroDocumento}
                        onChange={(e) => setNovoCidadaoNumeroDocumento(e.target.value)}
                        className="h-10"
                        required
                      />
                    </div>
                    
                    <div className="space-y-2">
                      <Label htmlFor="novoCidadaoTelefone" className="text-base">Telefone</Label>
                      <Input
                        id="novoCidadaoTelefone"
                        value={novoCidadaoTelefone}
                        onChange={(e) => setNovoCidadaoTelefone(e.target.value)}
                        className="h-10"
                      />
                    </div>
                  </div>
                  
                  <div className="space-y-2">
                    <Label htmlFor="novoCidadaoEndereco" className="text-base">Endereço</Label>
                    <Textarea
                      id="novoCidadaoEndereco"
                      value={novoCidadaoEndereco}
                      onChange={(e) => setNovoCidadaoEndereco(e.target.value)}
                      className="min-h-[100px]"
                      rows={3}
                    />
                  </div>
                </div>
              </TabsContent>
            </Tabs>
          </CardContent>
        </Card>
        
        <Card className="shadow-sm">
          <CardHeader className="space-y-1">
            <CardTitle className="text-2xl">Detalhes do Pedido</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="observacoes" className="text-base">Observações</Label>
                <Textarea
                  id="observacoes"
                  value={observacoes}
                  onChange={(e) => setObservacoes(e.target.value)}
                  placeholder="Informações adicionais sobre o pedido"
                  className="min-h-[120px]"
                  rows={4}
                />
              </div>
              
              <div className="space-y-2">
                <Label htmlFor="prioridade" className="text-base">Prioridade</Label>
                <Select value={prioridade} onValueChange={setPrioridade}>
                  <SelectTrigger className="h-10">
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
        
        <div className="flex justify-end gap-3">
          <Button 
            type="button" 
            variant="outline" 
            onClick={() => router.push('/dashboard')}
            disabled={isSubmitting}
            className="shadow-sm"
          >
            Cancelar
          </Button>
          <Button 
            type="submit" 
            disabled={isSubmitting || (!cidadaoId && cidadaoTab === "existente") || !selectedServiceId}
            className="shadow-sm"
          >
            {isSubmitting ? "Enviando..." : "Criar Pedido"}
          </Button>
        </div>
      </form>
    </div>
  )
}