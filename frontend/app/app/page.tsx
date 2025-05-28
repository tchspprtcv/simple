import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import Link from "next/link"
import Image from "next/image"

export default function Home() {
  return (
    <div className="min-h-screen">
      <section className="py-12 space-y-6">
        <div className="text-center space-y-4">
          <h1 className="text-4xl font-bold tracking-tighter">
            Bem-vindo ao Sistema de Serviços Municipais
          </h1>
          <p className="text-xl text-muted-foreground max-w-[800px] mx-auto">
            Gerencie seus pedidos de serviços municipais de forma simples e eficiente
          </p>
        </div>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
          <Card className="hover:shadow-lg transition-shadow">
            <CardHeader>
              <CardTitle>Serviços Disponíveis</CardTitle>
              <CardDescription>Explore todos os serviços municipais</CardDescription>
            </CardHeader>
            <CardContent>
              <Link href="/services">
                <Button className="w-full">Ver Serviços</Button>
              </Link>
            </CardContent>
          </Card>

          <Card className="hover:shadow-lg transition-shadow">
            <CardHeader>
              <CardTitle>Acompanhar Pedido</CardTitle>
              <CardDescription>Verifique o status do seu pedido</CardDescription>
            </CardHeader>
            <CardContent>
              <Link href="/track">
                <Button className="w-full" variant="outline">Acompanhar</Button>
              </Link>
            </CardContent>
          </Card>

          <Card className="hover:shadow-lg transition-shadow">
            <CardHeader>
              <CardTitle>Área do Utente</CardTitle>
              <CardDescription>Acesse sua conta para mais funcionalidades</CardDescription>
            </CardHeader>
            <CardContent>
              <Link href="/dashboard">
                <Button className="w-full" variant="secondary">Entrar</Button>
              </Link>
            </CardContent>
          </Card>
        </div>

        <div className="mt-12">
          <Card className="p-6">
            <div className="grid md:grid-cols-2 gap-8 items-center">
              <div>
                <h2 className="text-2xl font-bold mb-4">Estrutura do Sistema</h2>
                <p className="text-muted-foreground mb-4">
                  Conheça como nosso sistema está organizado para melhor atender suas necessidades
                </p>
                <Image
                  src="../app/er_diagram.png"
                  alt="Diagrama ER do Sistema"
                  width={500}
                  height={300}
                  className="rounded-lg shadow-md"
                />
              </div>
              <div className="space-y-4">
                <h3 className="text-xl font-semibold">Principais Funcionalidades</h3>
                <ul className="space-y-2">
                  <li>✓ Gestão de utilizadores e perfis</li>
                  <li>✓ Gestão de pedidos de serviços</li>
                  <li>✓ Acompanhamento de pedidos</li>
                  <li>✓ Gestão de documentos</li>
                  <li>✓ Gestão de pagamentos</li>
                  <li>✓ Gestão de vistorias</li>
                </ul>
              </div>
            </div>
          </Card>
        </div>
      </section>
    </div>
  )
}