import { NextResponse } from 'next/server';
import { PrismaClient } from '@prisma/client';

export const dynamic = "force-dynamic";

const prisma = new PrismaClient();

export async function GET() {
  try {
    const favorites = await prisma.favorite.findMany({
      include: {
        service: true,
      },
    });
    return NextResponse.json(favorites);
  } catch (error) {
    console.error('Favorites API Error:', error);
    return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 });
  }
}

export async function POST(request: Request) {
  try {
    const body = await request.json();
    const { userId, serviceId } = body;

    const favorite = await prisma.favorite.create({
      data: {
        userId,
        serviceId,
      },
      include: {
        service: true,
      },
    });

    return NextResponse.json(favorite);
  } catch (error) {
    console.error('Create Favorite API Error:', error);
    return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 });
  }
}

export async function DELETE(request: Request) {
  try {
    const { searchParams } = new URL(request.url);
    const id = searchParams.get('id');

    if (!id) {
      return NextResponse.json({ error: 'Favorite ID is required' }, { status: 400 });
    }

    await prisma.favorite.delete({
      where: {
        id,
      },
    });

    return NextResponse.json({ success: true });
  } catch (error) {
    console.error('Delete Favorite API Error:', error);
    return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 });
  }
}