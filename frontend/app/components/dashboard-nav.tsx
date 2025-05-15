"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { cn } from "@/lib/utils"

import {
  Users,
  Settings,
  Star,
  FileText,
  LayoutDashboard,
  Wrench,
} from "lucide-react"

const roleRoutes = {
  ADMINISTRADOR: [
    { href: "/dashboard", label: "Dashboard", icon: LayoutDashboard },
    { href: "/dashboard/admin/users", label: "Gestão de Usuários", icon: Users },
    { href: "/dashboard/admin/services", label: "Gestão de Tipos de Serviços", icon: Wrench },
    { href: "/dashboard/admin/settings", label: "Configurações", icon: Settings },
  ],
  ATENDENTE: [
    { href: "/dashboard", label: "Dashboard", icon: LayoutDashboard },
    { href: "/dashboard/attendant/favorites", label: "Favoritos", icon: Star },
    { href: "/dashboard/attendant/services", label: "Gerenciar Serviços", icon: Wrench },
    { href: "/services", label: "Solicitar Serviços", icon: FileText },
  ],
  CITIZEN: [
    { href: "/dashboard", label: "Dashboard", icon: LayoutDashboard },
    { href: "/services", label: "Solicitar Serviços", icon: FileText },
    { href: "/tracking", label: "Acompanhar Pedidos", icon: Star },
  ],
}


export function DashboardNav({ role = "CITIZEN" }: { role: string }) {
  console.log(role)
  const roleUpperCase = role.toUpperCase()
  role = roleUpperCase.charAt(0) + roleUpperCase.slice(1)
  const pathname = usePathname()
  const routes = roleRoutes[role as keyof typeof roleRoutes] || roleRoutes.CITIZEN
  
  return (
    <nav className="grid items-start gap-2">
      {routes.map((route) => {
        const Icon = route.icon
        return (
          <Link
            key={route.href}
            href={route.href}
            className={cn(
              "group flex items-center rounded-lg px-3 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground",
              pathname === route.href ? "bg-accent" : "transparent"
            )}
          >
            <Icon className="mr-2 h-4 w-4" />
            <span>{route.label}</span>
          </Link>
        )
      })}
    </nav>
  )
}