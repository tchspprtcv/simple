'use client';

import { motion } from 'framer-motion';
import { CheckCircle, Clock } from 'lucide-react';

interface TimelineProps {
  history: Array<{
    date: string;
    status: string;
    description: string;
  }>;
}

export default function StatusTimeline({ history }: TimelineProps) {
  return (
    <div className="space-y-6">
      {history.map((item, index) => (
        <motion.div
          key={index}
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ delay: index * 0.1 }}
          className="flex gap-4"
        >
          <div className="flex flex-col items-center">
            <div className="rounded-full bg-primary p-2">
              {index === history.length - 1 ? (
                <Clock className="h-5 w-5 text-white" />
              ) : (
                <CheckCircle className="h-5 w-5 text-white" />
              )}
            </div>
            {index < history.length - 1 && (
              <div className="h-full w-0.5 bg-gray-200 dark:bg-gray-700" />
            )}
          </div>
          <div className="flex-1 pb-6">
            <p className="text-sm text-gray-500 dark:text-gray-400">
              {new Date(item.date).toLocaleDateString('pt-BR')}
            </p>
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
              {item.status}
            </h3>
            <p className="text-gray-600 dark:text-gray-300">{item.description}</p>
          </div>
        </motion.div>
      ))}
    </div>
  );
}