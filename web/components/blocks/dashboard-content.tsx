import { AppSidebar } from '@/components/blocks/contacts/app-sidebar';
import { DataTable } from '@/components/blocks/contacts/data-table';
import { SiteHeader } from '@/components/blocks/contacts/site-header';
import { SidebarInset, SidebarProvider } from '@/components/ui/sidebar';
import data from '@/data/dashboard.json';

export function DashboardContent() {
  const apiEnabled = process.env.NODE_ENV !== 'test';

  return (
    <SidebarProvider
      style={
        {
          '--sidebar-width': 'calc(var(--spacing) * 72)',
          '--header-height': 'calc(var(--spacing) * 12)',
        } as React.CSSProperties
      }
    >
      <AppSidebar variant="inset" />
      <SidebarInset>
        <SiteHeader />
        <div className="@container/main flex flex-1 flex-col px-4 py-4 lg:px-6 lg:py-6">
          <DataTable
            data={data.map((contact) => ({ ...contact, id: String(contact.id) }))}
            apiEnabled={apiEnabled}
          />
        </div>
      </SidebarInset>
    </SidebarProvider>
  );
}
