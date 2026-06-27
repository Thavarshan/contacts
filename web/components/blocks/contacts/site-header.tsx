'use client';

import { Separator } from '@/components/ui/separator';
import { SidebarTrigger } from '@/components/ui/sidebar';
import { Button } from '@/components/ui/button';
import { PlusIcon } from 'lucide-react';
import { ADD_CONTACT_EVENT } from '@/components/blocks/contacts/app-sidebar';

export function SiteHeader() {
  return (
    <header className="flex h-(--header-height) shrink-0 items-center gap-2 border-b transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-(--header-height)">
      <div className="flex w-full items-center gap-1 px-4 lg:gap-2 lg:px-6">
        <SidebarTrigger className="-ml-1" />
        <Separator orientation="vertical" className="mx-2 h-4 data-vertical:self-auto" />
        <h1 className="text-base font-medium">Contacts</h1>
        <div className="ml-auto">
          <Button
            size="sm"
            onClick={() => {
              document.dispatchEvent(new Event(ADD_CONTACT_EVENT));
            }}
          >
            <PlusIcon data-icon="inline-start" />
            Add contact
          </Button>
        </div>
      </div>
    </header>
  );
}
