import Link from 'next/link';
import type { ReactNode } from 'react';
import { ContactRoundIcon } from 'lucide-react';

import { Toaster } from '@/components/ui/sonner';
import { TooltipProvider } from '@/components/ui/tooltip';

export default function AuthLayout({ children }: { children: ReactNode }) {
  return (
    <TooltipProvider>
      <main className="flex min-h-svh flex-col bg-background text-foreground">
        <header className="border-b">
          <div className="mx-auto flex h-16 w-full max-w-6xl items-center px-4 sm:px-6">
            <Link href="/" className="flex items-center gap-2 font-semibold">
              <span className="flex size-8 items-center justify-center rounded-xl bg-primary text-primary-foreground">
                <ContactRoundIcon className="size-4" />
              </span>
              Contacts Manager
            </Link>
          </div>
        </header>

        <section className="flex flex-1 items-center justify-center px-4 py-10 sm:px-6">
          <div className="w-full max-w-sm">{children}</div>
        </section>

        <footer className="border-t px-4 py-5 sm:px-6">
          <div className="mx-auto flex w-full max-w-6xl flex-col gap-2 text-sm text-muted-foreground sm:flex-row sm:items-center sm:justify-between">
            <p>Manage contacts, companies, and follow-ups from one dashboard.</p>
            <Link href="/" className="flex items-center gap-2 font-semibold">
              Back to home
            </Link>
          </div>
        </footer>
      </main>
      <Toaster />
    </TooltipProvider>
  );
}
