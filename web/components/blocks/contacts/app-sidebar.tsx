'use client';

import * as React from 'react';
import Link from 'next/link';
import {
  ContactRoundIcon,
  FolderIcon,
  HomeIcon,
  Settings2Icon,
  UserPlusIcon,
  UsersIcon,
} from 'lucide-react';

import { NavUser } from '@/components/blocks/contacts/nav-user';
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar';

const user = {
  name: 'Jerome Bell',
  email: 'jerome@example.com',
  avatar: '/avatars/jerome.jpg',
};

const navItems = [
  { title: 'Overview', url: '/dashboard', icon: HomeIcon },
  { title: 'Contacts', url: '/dashboard#contacts', icon: UsersIcon },
  { title: 'Groups', url: '/dashboard#groups', icon: FolderIcon },
  { title: 'Settings', url: '/dashboard#settings', icon: Settings2Icon },
];

export const ADD_CONTACT_EVENT = 'contacts:add';

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  return (
    <Sidebar collapsible="offcanvas" {...props}>
      <SidebarHeader>
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton
              className="data-[slot=sidebar-menu-button]:p-1.5!"
              render={<Link href="/dashboard" />}
            >
              <ContactRoundIcon className="size-5!" />
              <span className="text-base font-semibold">Contacts</span>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarHeader>
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupContent className="flex flex-col gap-2">
            <SidebarMenu>
              <SidebarMenuItem>
                <SidebarMenuButton
                  tooltip="Add contact"
                  className="min-w-8 bg-primary text-primary-foreground hover:bg-primary/90 hover:text-primary-foreground"
                  onClick={() => {
                    document.dispatchEvent(new Event(ADD_CONTACT_EVENT));
                  }}
                >
                  <UserPlusIcon />
                  <span>Add contact</span>
                </SidebarMenuButton>
              </SidebarMenuItem>
            </SidebarMenu>
            <SidebarMenu>
              {navItems.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton
                    tooltip={item.title}
                    render={<Link href={item.url} />}
                  >
                    <item.icon />
                    <span>{item.title}</span>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter>
        <NavUser user={user} />
      </SidebarFooter>
    </Sidebar>
  );
}
