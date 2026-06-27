import Head from 'next/head';
import Link from 'next/link';
import type { NextPage } from 'next';
import {
  ArrowRightIcon,
  ContactRoundIcon,
  LockKeyholeIcon,
  UsersRoundIcon,
} from 'lucide-react';

import { buttonVariants } from '@/components/ui/button';
import { cn } from '@/lib/utils';

const Home: NextPage = () => {
  return (
    <>
      <Head>
        <title>Contacts Manager | Welcome</title>
        <meta
          name="description"
          content="A focused contacts list manager for organizing people, companies, and follow-ups."
        />
      </Head>

      <main className="min-h-svh bg-background text-foreground">
        <header className="border-b">
          <div className="mx-auto flex h-16 w-full max-w-6xl items-center justify-between px-4 sm:px-6">
            <Link href="/" className="flex items-center gap-2 font-semibold">
              <span className="flex size-8 items-center justify-center rounded-xl bg-primary text-primary-foreground">
                <ContactRoundIcon className="size-4" />
              </span>
              Contacts Manager
            </Link>
            <nav className="flex items-center gap-2">
              <Link
                href="/auth/login"
                className={cn(buttonVariants({ variant: 'ghost' }))}
              >
                Sign in
              </Link>
              <Link href="/dashboard" className={cn(buttonVariants())}>
                Dashboard
                <ArrowRightIcon data-icon="inline-end" />
              </Link>
            </nav>
          </div>
        </header>

        <section className="mx-auto grid min-h-[calc(100svh-4rem)] w-full max-w-6xl items-center gap-10 px-4 py-12 sm:px-6 lg:grid-cols-[1fr_420px] lg:py-16">
          <div className="max-w-2xl">
            <div className="mb-5 inline-flex items-center gap-2 rounded-full border px-3 py-1 text-sm text-muted-foreground">
              <LockKeyholeIcon className="size-3.5" />
              Built for authenticated contact workflows
            </div>
            <h1 className="text-4xl font-semibold tracking-normal text-balance sm:text-5xl">
              Manage every contact, conversation, and next step in one focused dashboard.
            </h1>
            <p className="mt-5 max-w-xl text-base leading-7 text-muted-foreground sm:text-lg">
              Use the public landing page for auth entry points, then send signed-in users
              to the contacts dashboard where lists, activity, and account tools live.
            </p>
            <div className="mt-8 flex flex-col gap-3 sm:flex-row">
              <Link href="/auth/login" className={cn(buttonVariants({ size: 'lg' }))}>
                Sign in
                <ArrowRightIcon data-icon="inline-end" />
              </Link>
              <Link
                href="/dashboard"
                className={cn(buttonVariants({ size: 'lg', variant: 'outline' }))}
              >
                Preview dashboard
              </Link>
            </div>
          </div>

          <div className="rounded-lg border bg-card p-5 shadow-xs">
            <div className="flex items-center justify-between border-b pb-4">
              <div>
                <p className="text-sm text-muted-foreground">Contacts</p>
                <p className="text-2xl font-semibold">2,847</p>
              </div>
              <span className="flex size-10 items-center justify-center rounded-xl bg-secondary">
                <UsersRoundIcon className="size-5" />
              </span>
            </div>
            <div className="space-y-4 pt-5">
              {[
                ['Sarah Chen', 'Renewal follow-up due today'],
                ['Marcus Reed', 'New company contact added'],
                ['Nina Patel', 'Meeting notes ready for review'],
              ].map(([name, detail]) => (
                <div key={name} className="flex items-center gap-3">
                  <div className="flex size-9 shrink-0 items-center justify-center rounded-full bg-muted text-sm font-medium">
                    {name
                      .split(' ')
                      .map((part) => part[0])
                      .join('')}
                  </div>
                  <div className="min-w-0">
                    <p className="truncate text-sm font-medium">{name}</p>
                    <p className="truncate text-sm text-muted-foreground">{detail}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </section>
      </main>
    </>
  );
};

export default Home;
