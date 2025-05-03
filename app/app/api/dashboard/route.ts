import { NextResponse } from 'next/server';
import { PrismaClient } from '@prisma/client';

export const dynamic = "force-dynamic";

const prisma = new PrismaClient();

export async function GET() {
  try {
    const [requests, favorites, statistics] = await Promise.all([
      prisma.request.findMany({
        include: {
          service: true,
          user: true,
        },
        take: 10,
        orderBy: {
          createdAt: 'desc',
        },
      }),
      prisma.favorite.findMany({
        include: {
          service: true,
        },
      }),
      prisma.request.groupBy({
        by: ['status'],
        _count: {
          id: true,
        },
      }),
    ]);

    const totalRequests = statistics.reduce((acc, curr) => acc + curr._count.id, 0);
    const pendingRequests = statistics.find(s => s.status === 'pending')?._count.id || 0;
    const completedRequests = statistics.find(s => s.status === 'completed')?._count.id || 0;

    return NextResponse.json({
      requests,
      favorites,
      statistics: {
        totalRequests,
        pendingRequests,
        completedRequests,
      },
    });
  } catch (error) {
    console.error('Dashboard API Error:', error);
    return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 });
  }
}