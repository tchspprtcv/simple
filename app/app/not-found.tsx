import Link from 'next/link';
import { Button } from '@/components/ui/button';

export default function NotFound() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen p-4">
      <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">
        Página não encontrada
      </h2>
      <p className="text-gray-600 dark:text-gray-300 mb-6">
        A página que você está procurando não existe.
      </p>
      <Link href="/">
        <Button>Voltar para a página inicial</Button>
      </Link>
    </div>
  );
}