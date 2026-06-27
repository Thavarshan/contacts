import Head from 'next/head';
import type { ReactElement } from 'react';

import { LoginForm } from '@/components/blocks/auth/login-form';
import AuthLayout from '@/components/layouts/auth';
import type { NextPageWithLayout } from '@/pages/_app';

const LoginPage: NextPageWithLayout = () => {
  return (
    <>
      <Head>
        <title>Login | Contacts Manager</title>
        <meta name="description" content="Login to your Contacts Manager account." />
      </Head>
      <LoginForm />
    </>
  );
};

LoginPage.getLayout = function getLayout(page: ReactElement) {
  return <AuthLayout>{page}</AuthLayout>;
};

export default LoginPage;
