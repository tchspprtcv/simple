import { PrismaClient } from '@prisma/client'
import { NextResponse } from 'next/server'

const prisma = new PrismaClient()

export const dynamic = "force-dynamic"

export async function GET(request: Request) {
  try {
    const { searchParams } = new URL(request.url)
    const userId = searchParams.get('userId')
    
    const requests = await prisma.request.findMany({
      where: userId ? { userId } : undefined,
      include: {
        service: true,
        user: true
      }
    })
    
    return NextResponse.json(requests)
  } catch (error) {
    return NextResponse.json({ error: 'Erro ao buscar solicitações' }, { status: 500 })
  }
}

export async function POST(request: Request) {
  try {
    const data = await request.json()
    const newRequest = await prisma.request.create({
      data,
      include: {
        service: true
      }
    })
    return NextResponse.json(newRequest)
  } catch (error) {
    return NextResponse.json({ error: 'Erro ao criar solicitação' }, { status: 500 })
  }
}