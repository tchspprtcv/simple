import { NextResponse } from 'next/server';
import { PrismaClient } from '@prisma/client';

export const dynamic = "force-dynamic";

const prisma = new PrismaClient();

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const trackingCode = searchParams.get('code');

  if (!trackingCode) {
    return NextResponse.json(
      { error: 'Tracking code is required' },
      { status: 400 }
    );
  }

  try {
    const request = await prisma.request.findUnique({
      where: { trackingCode },
      include: {
        statusHistory: {
          orderBy: { date: 'desc' }
        }
      }
    });

    if (!request) {
      return NextResponse.json(
        { error: 'Request not found' },
        { status: 404 }
      );
    }

    return NextResponse.json({
      success: true,
      data: {
        id: request.id,
        trackingCode: request.trackingCode,
        currentStatus: request.currentStatus,
        history: request.statusHistory.map((history) => ({
          date: history.date.toISOString(),
          status: history.status,
          description: history.description
        }))
      }
    });
  } catch (error) {
    console.error('Tracking API Error:', error);
    return NextResponse.json(
      { error: 'Internal Server Error' },
      { status: 500 }
    );
  }
}