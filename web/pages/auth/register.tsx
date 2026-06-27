import Head from 'next/head';
import type { ReactElement } from 'react';

import AuthLayout from '@/components/layouts/auth';
import { SignupForm } from '@/components/blocks/auth/signup-form';
import type { NextPageWithLayout } from '@/pages/_app';

const RegisterPage: NextPageWithLayout = () => {
  return (
    <>
      <Head>
        <title>Register | Contacts Manager</title>
        <meta
          name="description"
          content="Create a Contacts Manager account to manage your contacts."
        />
      </Head>
      <SignupForm />
    </>
  );
};

RegisterPage.getLayout = function getLayout(page: ReactElement) {
  return <AuthLayout>{page}</AuthLayout>;
};

export default RegisterPage;
