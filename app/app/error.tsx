'use client';

import { useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { toast } from 'sonner';

export default function Error({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  useEffect(() => {
    console.error('Application error:', error);
    toast.error('Ocorreu um erro na aplicação');
  }, [error]);

  return (
    <div className="flex flex-col items-center justify-center min-h-[400px] p-4">
      <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">
        Algo deu errado!
      </h2>
      <Button onClick={reset}>Tentar novamente</Button>
    </div>
  );
}