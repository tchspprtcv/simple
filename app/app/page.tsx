import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import { ArrowRight, Search, Bell } from 'lucide-react';

export default function HomePage() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-gray-50 to-gray-100 dark:from-gray-900 dark:to-gray-800 p-4 sm:p-6 lg:p-8">
      <div className="max-w-4xl mx-auto space-y-8">
        <div className="text-center space-y-4">
          <h1 className="text-4xl font-bold text-gray-900 dark:text-white">
            Sistema de Serviços Municipais
          </h1>
          <p className="text-xl text-gray-600 dark:text-gray-300">
            Acompanhe seus pedidos e solicitações de forma simples e rápida
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Card className="p-6 hover:shadow-lg transition-shadow">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Search className="h-6 w-6" />
                Acompanhar Pedido
              </CardTitle>
              <CardDescription>
                Verifique o status do seu pedido usando o código de acompanhamento
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Link href="/tracking">
                <Button className="w-full">
                  Consultar Status
                  <ArrowRight className="ml-2 h-4 w-4" />
                </Button>
              </Link>
            </CardContent>
          </Card>

          <Card className="p-6 hover:shadow-lg transition-shadow">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Bell className="h-6 w-6" />
                Notificações
              </CardTitle>
              <CardDescription>
                Configure notificações para acompanhar as atualizações do seu pedido
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Link href="/tracking">
                <Button variant="outline" className="w-full">
                  Configurar Notificações
                  <ArrowRight className="ml-2 h-4 w-4" />
                </Button>
              </Link>
            </CardContent>
          </Card>
        </div>

        <div className="mt-12 text-center">
          <p className="text-gray-600 dark:text-gray-300 mb-4">
            Precisa fazer uma nova solicitação?
          </p>
          <Link href="/dashboard">
            <Button size="lg">
              Acessar Dashboard
              <ArrowRight className="ml-2 h-4 w-4" />
            </Button>
          </Link>
        </div>
      </div>
    </div>
  );
}