import { PrismaClient } from '@prisma/client'
import { NextResponse } from 'next/server'
import { cookies } from 'next/headers'
import { encrypt } from '@/lib/crypto'
import { ResponseCookies } from 'next/dist/compiled/@edge-runtime/cookies'

const prisma = new PrismaClient()

export const dynamic = "force-dynamic"

export async function POST(request: Request) {
  try {
    const { email, password } = await request.json()
    
    const user = await prisma.user.findUnique({
      where: { email }
    })

    if (!user) {
      return NextResponse.json({ error: 'Utilizador não encontrado' }, { status: 404 })
    }

    // In production, use proper password hashing comparison
    if (user.password !== password) {
      return NextResponse.json({ error: 'Senha incorreta' }, { status: 401 })
    }

    // Create a simple session token
    const session = encrypt(JSON.stringify({
      userId: user.id,
      role: user.role,
      exp: Date.now() + (24 * 60 * 60 * 1000) // 24 hours
    }))

    const cookieStore = cookies() as unknown as ResponseCookies
    cookieStore.set('session', session, {
      httpOnly: true,
      secure: process.env.NODE_ENV === 'production',
      sameSite: 'strict',
      maxAge: 60 * 60 * 24 // 24 hours
    })

    return NextResponse.json({ 
      success: true,
      role: user.role 
    })
  } catch (error) {
    console.error('Auth error:', error)
    return NextResponse.json({ error: 'Erro interno do servidor' }, { status: 500 })
  }
}