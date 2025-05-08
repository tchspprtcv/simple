import { DashboardNav } from "@/components/dashboard-nav"

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <div className="flex min-h-screen">
      <div className="hidden w-64 flex-col border-r bg-gray-50/50 dark:bg-gray-900/50 md:flex">
        <div className="flex h-14 items-center border-b px-4">
          <span className="font-semibold">Simple</span>
        </div>
        <div className="flex-1 overflow-auto py-2">
          <DashboardNav />
        </div>
      </div>
      <div className="flex-1">
        <div className="h-14 border-b" />
        <div className="flex-1">{children}</div>
      </div>
    </div>
  )
}