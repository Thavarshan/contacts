import { useEffect, type ReactElement, type ReactNode } from 'react';
import type { NextPage } from 'next';
import type { AppProps } from 'next/app';
import { Inter } from 'next/font/google';

import DefaultLayout from '@/components/layouts/default';
import '../assets/css/globals.css';

export type NextPageWithLayout<P = object, IP = P> = NextPage<P, IP> & {
  getLayout?: (page: ReactElement) => ReactNode;
};

type AppPropsWithLayout = AppProps & {
  Component: NextPageWithLayout;
};

const inter = Inter({
  subsets: ['latin'],
  display: 'swap',
  variable: '--font-inter',
});

export default function MainApp({ Component, pageProps }: AppPropsWithLayout) {
  const getLayout =
    Component.getLayout ?? ((page) => <DefaultLayout>{page}</DefaultLayout>);

  useEffect(() => {
    document.body.classList.add(inter.variable);

    return () => {
      document.body.classList.remove(inter.variable);
    };
  }, []);

  return (
    <div className={`${inter.variable} font-sans`}>
      {getLayout(<Component {...pageProps} />)}
    </div>
  );
}
