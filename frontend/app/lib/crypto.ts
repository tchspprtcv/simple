const ENCRYPTION_KEY = process.env.ENCRYPTION_KEY || 'default-key-32-chars-12345678901'

export function encrypt(text: string): string {
  const textBuffer = Buffer.from(text, 'utf8')
  return textBuffer.toString('base64')
}

export function decrypt(encrypted: string): string {
  const buffer = Buffer.from(encrypted, 'base64')
  return buffer.toString('utf8')
}