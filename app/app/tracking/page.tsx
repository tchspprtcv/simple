'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { Search, Mail, Clock, CheckCircle } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card } from '@/components/ui/card';
import { toast } from 'sonner';
import StatusTimeline from '@/components/tracking/status-timeline';
import NotificationButton from '@/components/tracking/notification-button';
import { getTrackingInfo } from '@/lib/tracking-service';
import type { RequestStatus } from '@/lib/types';

export default function TrackingPage() {
  const [trackingCode, setTrackingCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [status, setStatus] = useState<RequestStatus | null>(null);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!trackingCode.trim()) {
      toast.error('Por favor, insira um código de acompanhamento');
      return;
    }

    setLoading(true);
    try {
      const response = await getTrackingInfo(trackingCode);
      if (response.success && response.data) {
        setStatus(response.data);
        toast.success('Informações encontradas');
      } else {
        toast.error(response.error || 'Código não encontrado');
        setStatus(null);
      }
    } catch (error) {
      toast.error('Erro ao buscar informações');
      setStatus(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-gray-50 to-gray-100 dark:from-gray-900 dark:to-gray-800 p-4 sm:p-6 lg:p-8">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="max-w-4xl mx-auto space-y-8"
      >
        <div className="text-center space-y-4">
          <h1 className="text-4xl font-bold text-gray-900 dark:text-white">
            Acompanhamento de Pedido
          </h1>
          <p className="text-gray-600 dark:text-gray-300">
            Insira seu código de acompanhamento para verificar o status do seu pedido
          </p>
        </div>

        <Card className="p-6 shadow-lg">
          <form onSubmit={handleSearch} className="flex gap-4">
            <Input
              type="text"
              placeholder="Digite o código de acompanhamento"
              value={trackingCode}
              onChange={(e) => setTrackingCode(e.target.value)}
              className="flex-1"
              disabled={loading}
            />
            <Button type="submit" disabled={loading}>
              {loading ? (
                <span className="flex items-center">
                  <Clock className="animate-spin mr-2 h-4 w-4" />
                  Buscando...
                </span>
              ) : (
                <>
                  <Search className="mr-2 h-4 w-4" />
                  Buscar
                </>
              )}
            </Button>
          </form>
        </Card>

        {status && (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="space-y-6"
          >
            <Card className="p-6 shadow-lg">
              <div className="flex items-center justify-between mb-6">
                <div>
                  <h2 className="text-2xl font-semibold text-gray-900 dark:text-white">
                    Status Atual: {status.currentStatus}
                  </h2>
                  <p className="text-gray-600 dark:text-gray-300">
                    Código: {status.trackingCode}
                  </p>
                </div>
                <NotificationButton trackingCode={status.trackingCode} />
              </div>
              <StatusTimeline history={status.history} />
            </Card>
          </motion.div>
        )}
      </motion.div>
    </div>
  );
}