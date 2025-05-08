import { PrismaClient } from '@prisma/client'
import { NextResponse } from 'next/server'

const prisma = new PrismaClient()

export const dynamic = "force-dynamic"

export async function GET() {
  try {
    const services = await prisma.service.findMany()
    return NextResponse.json(services)
  } catch (error) {
    return NextResponse.json({ error: 'Erro ao buscar serviços' }, { status: 500 })
  }
}

export async function POST(request: Request) {
  try {
    const data = await request.json()
    const service = await prisma.service.create({ data })
    return NextResponse.json(service)
  } catch (error) {
    return NextResponse.json({ error: 'Erro ao criar serviço' }, { status: 500 })
  }
}