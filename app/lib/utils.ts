import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"
 
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatDate(date: string) {
  return new Date(date).toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

export function handleError(error: unknown): string {
  if (error instanceof Error) {
    console.error('Application error:', error);
    return error.message;
  }
  console.error('Unknown error:', error);
  return 'Ocorreu um erro inesperado';
}