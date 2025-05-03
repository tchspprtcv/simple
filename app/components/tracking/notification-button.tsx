'use client';

import { Button } from '@/components/ui/button';
import { Mail } from 'lucide-react';

interface NotificationButtonProps {
  trackingCode: string;
}

export default function NotificationButton({ trackingCode }: NotificationButtonProps) {
  const handleNotificationSetup = () => {
    const subject = encodeURIComponent(`Notificações do Pedido ${trackingCode}`);
    const body = encodeURIComponent(
      `Olá,\n\nGostaria de receber notificações sobre o status do meu pedido ${trackingCode}.\n\nAtenciosamente.`
    );
    window.location.href = `mailto:atendimento@municipio.gov.br?subject=${subject}&body=${body}`;
  };

  return (
    <Button onClick={handleNotificationSetup} variant="outline">
      <Mail className="mr-2 h-4 w-4" />
      Receber Notificações
    </Button>
  );
}