# Frontend - Simple

## 📋 Visão Geral

O **Frontend** da aplicação Simple é uma interface web moderna e responsiva desenvolvida em Next.js, oferecendo uma experiência intuitiva para cidadãos e funcionários públicos acessarem e gerenciarem serviços municipais.

## 🏗️ Arquitetura

O frontend implementa:
- **App Router** do Next.js 15 para roteamento moderno
- **Server-Side Rendering (SSR)** para performance otimizada
- **Component-based architecture** com React 18
- **State management** com React Query e Jotai
- **Design system** baseado em Radix UI e Tailwind CSS
- **Authentication** integrada com NextAuth.js
- **Real-time updates** com SWR

## 🚀 Tecnologias

### Core
- **Next.js 15.3.2** - Framework React com SSR/SSG
- **React 18** - Biblioteca de interface de usuário
- **TypeScript 5.2.2** - Tipagem estática
- **Tailwind CSS 3.4.1** - Framework CSS utilitário

### UI Components
- **Radix UI** - Componentes acessíveis e customizáveis
- **Lucide React** - Ícones modernos
- **Framer Motion** - Animações fluidas
- **Embla Carousel** - Carrosséis responsivos

### State Management
- **TanStack React Query 5.76.1** - Gerenciamento de estado servidor
- **Jotai 2.5.1** - State management atômico
- **SWR 2.2.4** - Data fetching com cache

### Forms & Validation
- **React Hook Form 7.48.2** - Gerenciamento de formulários
- **Formik 2.4.5** - Formulários complexos
- **Zod 3.22.4** - Validação de esquemas
- **Yup 1.3.2** - Validação de dados

### Authentication & Security
- **NextAuth.js 4.24.7** - Autenticação completa
- **JSON Web Tokens** - Tokens seguros
- **Crypto utilities** - Criptografia client-side

### Data Visualization
- **Chart.js 4.4.9** - Gráficos interativos
- **React Chart.js 2** - Integração React

### BPMN & Workflow
- **BPMN.js 13.2.2** - Visualização de processos
- **Camunda BPMN** - Modelagem de workflows

### Database
- **Prisma 6.7.0** - ORM moderno
- **PostgreSQL** - Banco de dados relacional

### Development Tools
- **ESLint** - Linting de código
- **PostCSS** - Processamento CSS
- **Autoprefixer** - Compatibilidade CSS

## 📁 Estrutura do Projeto

```
frontend/
├── app/                    # Next.js App Router
│   ├── api/               # API Routes
│   ├── dashboard/         # Dashboard pages
│   ├── login/            # Autenticação
│   ├── register/         # Cadastro
│   ├── services/         # Catálogo de serviços
│   ├── profile/          # Perfil do usuário
│   ├── track/            # Rastreamento
│   ├── tracking/         # Acompanhamento
│   ├── layout.tsx        # Layout principal
│   ├── page.tsx          # Página inicial
│   └── globals.css       # Estilos globais
├── components/            # Componentes reutilizáveis
│   ├── ui/               # Componentes base
│   ├── dashboard-nav.tsx # Navegação dashboard
│   ├── favorite-services.tsx # Serviços favoritos
│   ├── navbar.tsx        # Barra de navegação
│   ├── order-tracking.tsx # Rastreamento pedidos
│   ├── service-search.tsx # Busca de serviços
│   └── tracking/         # Componentes de tracking
├── lib/                   # Utilitários e configurações
│   ├── api-config.ts     # Configuração da API
│   ├── api-services.ts   # Serviços da API
│   ├── auth-context.tsx  # Contexto de autenticação
│   ├── crypto.ts         # Utilitários de criptografia
│   ├── db.ts            # Configuração do banco
│   ├── services/        # Serviços específicos
│   ├── tracking-service.ts # Serviço de rastreamento
│   ├── types.ts         # Definições de tipos
│   └── utils.ts         # Utilitários gerais
├── hooks/                # Custom hooks
│   ├── use-configuracoes.ts # Hook de configurações
│   └── use-toast.ts     # Hook de notificações
├── assets/               # Recursos estáticos
│   └── images/          # Imagens
├── prisma/               # Schema do banco
│   ├── migrations/      # Migrações
│   └── schema.prisma    # Schema principal
├── data/                 # Dados estáticos
├── middleware.ts         # Middleware Next.js
├── next.config.js       # Configuração Next.js
├── tailwind.config.ts   # Configuração Tailwind
├── tsconfig.json        # Configuração TypeScript
└── package.json         # Dependências
```

## 🔧 Configuração

### Variáveis de Ambiente

```bash
# API Configuration
NEXT_PUBLIC_API_URL=http://localhost:9080
NEXT_PUBLIC_API_GATEWAY_URL=http://localhost:9080

# Authentication
NEXTAUTH_URL=http://localhost:3000
NEXTAUTH_SECRET=your-nextauth-secret
JWT_SECRET=your-jwt-secret

# Database
DATABASE_URL=postgresql://postgres:postgres@localhost:9433/simple_frontend

# Services URLs
NEXT_PUBLIC_AUTH_SERVICE_URL=http://localhost:9081
NEXT_PUBLIC_CITIZEN_SERVICE_URL=http://localhost:9082
NEXT_PUBLIC_ORDER_SERVICE_URL=http://localhost:9083
NEXT_PUBLIC_CONFIG_SERVICE_URL=http://localhost:9084
NEXT_PUBLIC_FAVORITES_SERVICE_URL=http://localhost:9085

# Features
NEXT_PUBLIC_ENABLE_ANALYTICS=true
NEXT_PUBLIC_ENABLE_NOTIFICATIONS=true
NEXT_PUBLIC_ENABLE_DARK_MODE=true

# Upload
NEXT_PUBLIC_MAX_FILE_SIZE=10485760
NEXT_PUBLIC_ALLOWED_FILE_TYPES=pdf,jpg,jpeg,png,doc,docx
```

### Configuração do Next.js

```javascript
// next.config.js
/** @type {import('next').NextConfig} */
const nextConfig = {
  experimental: {
    appDir: true,
  },
  images: {
    domains: ['localhost'],
  },
  env: {
    CUSTOM_KEY: process.env.CUSTOM_KEY,
  },
}

module.exports = nextConfig
```

## 🚀 Execução

### Desenvolvimento Local

```bash
# Instalar dependências
yarn install
# ou
npm install

# Executar em modo desenvolvimento
yarn dev
# ou
npm run dev

# Acessar aplicação
open http://localhost:3000
```

### Build de Produção

```bash
# Gerar build otimizado
yarn build
# ou
npm run build

# Executar build de produção
yarn start
# ou
npm start
```

### Docker

```bash
# Construir imagem
docker build -t simple-frontend .

# Executar container
docker run -p 3000:3000 \
  -e NEXT_PUBLIC_API_URL=http://host.docker.internal:9080 \
  -e NEXTAUTH_SECRET=your-secret \
  simple-frontend
```

### Docker Compose

```bash
# Executar frontend
docker-compose up frontend

# Executar com dependências
docker-compose up frontend api-gateway
```

## 🎨 Design System

### Componentes Base

```typescript
// Exemplo de componente base
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card } from '@/components/ui/card'

function ServiceCard({ service }) {
  return (
    <Card className="p-6">
      <h3 className="text-lg font-semibold">{service.name}</h3>
      <p className="text-gray-600">{service.description}</p>
      <Button className="mt-4">Solicitar Serviço</Button>
    </Card>
  )
}
```

### Tema e Cores

```css
/* globals.css */
:root {
  --primary: 220 90% 56%;
  --primary-foreground: 220 90% 98%;
  --secondary: 220 14.3% 95.9%;
  --secondary-foreground: 220.9 39.3% 11%;
  --muted: 220 14.3% 95.9%;
  --muted-foreground: 220 8.9% 46.1%;
  --accent: 220 14.3% 95.9%;
  --accent-foreground: 220.9 39.3% 11%;
  --destructive: 0 84.2% 60.2%;
  --destructive-foreground: 210 20% 98%;
  --border: 220 13% 91%;
  --input: 220 13% 91%;
  --ring: 220 90% 56%;
  --radius: 0.5rem;
}
```

### Responsividade

```typescript
// Breakpoints Tailwind
const breakpoints = {
  sm: '640px',   // Mobile landscape
  md: '768px',   // Tablet
  lg: '1024px',  // Desktop
  xl: '1280px',  // Large desktop
  '2xl': '1536px' // Extra large
}
```

## 🔐 Autenticação

### NextAuth.js Configuration

```typescript
// lib/auth-config.ts
import NextAuth from 'next-auth'
import CredentialsProvider from 'next-auth/providers/credentials'

export const authOptions = {
  providers: [
    CredentialsProvider({
      name: 'credentials',
      credentials: {
        email: { label: 'Email', type: 'email' },
        password: { label: 'Password', type: 'password' }
      },
      async authorize(credentials) {
        const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/login`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(credentials)
        })
        
        if (response.ok) {
          const user = await response.json()
          return user
        }
        return null
      }
    })
  ],
  pages: {
    signIn: '/login',
    signUp: '/register'
  },
  callbacks: {
    async jwt({ token, user }) {
      if (user) {
        token.accessToken = user.accessToken
        token.role = user.role
      }
      return token
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken
      session.user.role = token.role
      return session
    }
  }
}
```

### Proteção de Rotas

```typescript
// middleware.ts
import { withAuth } from 'next-auth/middleware'

export default withAuth(
  function middleware(req) {
    // Middleware logic
  },
  {
    callbacks: {
      authorized: ({ token, req }) => {
        const { pathname } = req.nextUrl
        
        // Rotas públicas
        if (pathname.startsWith('/login') || pathname.startsWith('/register')) {
          return true
        }
        
        // Rotas protegidas
        if (pathname.startsWith('/dashboard')) {
          return !!token
        }
        
        // Rotas administrativas
        if (pathname.startsWith('/admin')) {
          return token?.role === 'ADMIN'
        }
        
        return !!token
      }
    }
  }
)

export const config = {
  matcher: ['/dashboard/:path*', '/admin/:path*', '/profile/:path*']
}
```

## 📊 State Management

### React Query

```typescript
// hooks/use-services.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { apiServices } from '@/lib/api-services'

export function useServices() {
  return useQuery({
    queryKey: ['services'],
    queryFn: apiServices.getServices,
    staleTime: 5 * 60 * 1000, // 5 minutos
  })
}

export function useCreateOrder() {
  const queryClient = useQueryClient()
  
  return useMutation({
    mutationFn: apiServices.createOrder,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] })
    }
  })
}
```

### Jotai (State Atômico)

```typescript
// lib/atoms.ts
import { atom } from 'jotai'

export const userAtom = atom(null)
export const themeAtom = atom('light')
export const notificationsAtom = atom([])

// Atom derivado
export const unreadNotificationsAtom = atom(
  (get) => get(notificationsAtom).filter(n => !n.read).length
)
```

## 🎯 Funcionalidades Principais

### Dashboard do Cidadão

```typescript
// app/dashboard/page.tsx
export default function Dashboard() {
  const { data: user } = useSession()
  const { data: orders } = useOrders()
  const { data: favorites } = useFavorites()
  
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <ServiceSearch />
      <FavoriteServices services={favorites} />
      <OrderTracking orders={orders} />
      <RecentActivity />
      <QuickActions />
      <Notifications />
    </div>
  )
}
```

### Catálogo de Serviços

```typescript
// app/services/page.tsx
export default function ServicesPage() {
  const [search, setSearch] = useState('')
  const [category, setCategory] = useState('all')
  const { data: services, isLoading } = useServices({ search, category })
  
  return (
    <div className="space-y-6">
      <ServiceFilters 
        search={search} 
        onSearchChange={setSearch}
        category={category}
        onCategoryChange={setCategory}
      />
      
      {isLoading ? (
        <ServicesSkeleton />
      ) : (
        <ServicesGrid services={services} />
      )}
    </div>
  )
}
```

### Rastreamento de Pedidos

```typescript
// components/order-tracking.tsx
export function OrderTracking({ orderId }: { orderId: string }) {
  const { data: order } = useOrder(orderId)
  const { data: timeline } = useOrderTimeline(orderId)
  
  return (
    <Card>
      <CardHeader>
        <CardTitle>Acompanhar Pedido #{orderId}</CardTitle>
      </CardHeader>
      <CardContent>
        <OrderStatus status={order?.status} />
        <OrderTimeline events={timeline} />
        <OrderDocuments documents={order?.documents} />
      </CardContent>
    </Card>
  )
}
```

## 📱 Responsividade

### Mobile First

```typescript
// Componente responsivo
function ResponsiveLayout({ children }) {
  return (
    <div className="
      grid 
      grid-cols-1 
      sm:grid-cols-2 
      lg:grid-cols-3 
      xl:grid-cols-4 
      gap-4 
      p-4
    ">
      {children}
    </div>
  )
}
```

### Navigation Adaptativa

```typescript
// components/navbar.tsx
export function Navbar() {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false)
  
  return (
    <nav className="bg-white shadow-sm border-b">
      {/* Desktop Navigation */}
      <div className="hidden md:flex items-center justify-between px-6 py-4">
        <Logo />
        <NavigationMenu />
        <UserMenu />
      </div>
      
      {/* Mobile Navigation */}
      <div className="md:hidden">
        <MobileHeader onMenuToggle={setIsMobileMenuOpen} />
        <MobileMenu isOpen={isMobileMenuOpen} />
      </div>
    </nav>
  )
}
```

## 🧪 Testes

### Configuração de Testes

```bash
# Instalar dependências de teste
yarn add -D @testing-library/react @testing-library/jest-dom jest jest-environment-jsdom

# Executar testes
yarn test

# Executar com coverage
yarn test --coverage

# Executar em modo watch
yarn test --watch
```

### Exemplo de Teste

```typescript
// __tests__/components/service-card.test.tsx
import { render, screen } from '@testing-library/react'
import { ServiceCard } from '@/components/service-card'

const mockService = {
  id: '1',
  name: 'Licença de Construção',
  description: 'Solicite sua licença de construção',
  category: 'BUILDING'
}

describe('ServiceCard', () => {
  it('renders service information correctly', () => {
    render(<ServiceCard service={mockService} />)
    
    expect(screen.getByText('Licença de Construção')).toBeInTheDocument()
    expect(screen.getByText('Solicite sua licença de construção')).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /solicitar/i })).toBeInTheDocument()
  })
})
```

## 🚀 Performance

### Otimizações

```typescript
// Lazy loading de componentes
const DashboardChart = lazy(() => import('@/components/dashboard-chart'))
const OrderHistory = lazy(() => import('@/components/order-history'))

// Image optimization
import Image from 'next/image'

function ServiceImage({ src, alt }) {
  return (
    <Image
      src={src}
      alt={alt}
      width={300}
      height={200}
      placeholder="blur"
      blurDataURL="data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQ..."
    />
  )
}

// Code splitting por rota
const AdminPanel = dynamic(() => import('@/components/admin-panel'), {
  loading: () => <AdminSkeleton />,
  ssr: false
})
```

### Bundle Analysis

```bash
# Analisar bundle
yarn add -D @next/bundle-analyzer

# Configurar next.config.js
const withBundleAnalyzer = require('@next/bundle-analyzer')({
  enabled: process.env.ANALYZE === 'true',
})

module.exports = withBundleAnalyzer(nextConfig)

# Executar análise
ANALYZE=true yarn build
```

## 📊 Monitoramento

### Analytics

```typescript
// lib/analytics.ts
export const analytics = {
  track: (event: string, properties?: Record<string, any>) => {
    if (typeof window !== 'undefined' && process.env.NEXT_PUBLIC_ENABLE_ANALYTICS) {
      // Implementar tracking
      console.log('Analytics:', event, properties)
    }
  },
  
  page: (name: string) => {
    analytics.track('page_view', { page: name })
  }
}

// Hook para tracking
export function useAnalytics() {
  const router = useRouter()
  
  useEffect(() => {
    const handleRouteChange = (url: string) => {
      analytics.page(url)
    }
    
    router.events.on('routeChangeComplete', handleRouteChange)
    return () => router.events.off('routeChangeComplete', handleRouteChange)
  }, [router])
}
```

### Error Monitoring

```typescript
// lib/error-monitoring.ts
export function reportError(error: Error, context?: Record<string, any>) {
  console.error('Error reported:', error, context)
  
  // Integrar com serviço de monitoramento
  if (process.env.NODE_ENV === 'production') {
    // Sentry, LogRocket, etc.
  }
}

// Error Boundary
export class ErrorBoundary extends Component {
  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    reportError(error, { errorInfo })
  }
  
  render() {
    if (this.state.hasError) {
      return <ErrorFallback />
    }
    
    return this.props.children
  }
}
```

## 🔍 SEO e Acessibilidade

### Meta Tags

```typescript
// app/layout.tsx
export const metadata: Metadata = {
  title: 'Simple - Serviços Municipais',
  description: 'Plataforma digital para serviços municipais',
  keywords: 'serviços públicos, município, governo digital',
  authors: [{ name: 'Equipe Simple' }],
  openGraph: {
    title: 'Simple - Serviços Municipais',
    description: 'Acesse serviços municipais de forma digital',
    images: ['/og-image.jpg'],
  },
}
```

### Acessibilidade

```typescript
// Componente acessível
function AccessibleButton({ children, ...props }) {
  return (
    <button
      className="
        focus:outline-none 
        focus:ring-2 
        focus:ring-blue-500 
        focus:ring-offset-2
        disabled:opacity-50
        disabled:cursor-not-allowed
      "
      aria-label={props['aria-label']}
      {...props}
    >
      {children}
    </button>
  )
}
```

## 🚨 Troubleshooting

### Problemas Comuns

1. **Hydration Mismatch**:
   ```typescript
   // Usar dynamic import para componentes client-side
   const ClientOnlyComponent = dynamic(() => import('./client-component'), {
     ssr: false
   })
   ```

2. **API Connection Issues**:
   ```bash
   # Verificar variáveis de ambiente
   echo $NEXT_PUBLIC_API_URL
   
   # Testar conectividade
   curl $NEXT_PUBLIC_API_URL/health
   ```

3. **Build Errors**:
   ```bash
   # Limpar cache
   rm -rf .next
   yarn build
   
   # Verificar TypeScript
   yarn tsc --noEmit
   ```

### Debug Mode

```bash
# Habilitar debug
DEBUG=* yarn dev

# Debug específico
DEBUG=next:* yarn dev
```

## 📚 Documentação Adicional

- [Next.js Documentation](https://nextjs.org/docs)
- [React Query Guide](https://tanstack.com/query/latest)
- [Tailwind CSS](https://tailwindcss.com/docs)
- [Radix UI](https://www.radix-ui.com/docs)
- [NextAuth.js](https://next-auth.js.org/)

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Siga os padrões de código estabelecidos
4. Adicione testes para novas funcionalidades
5. Envie um pull request

### Padrões de Código

```bash
# Linting
yarn lint

# Formatação
yarn format

# Type checking
yarn type-check
```

---

**Frontend Simple** - Interface moderna para serviços municipais 🌐