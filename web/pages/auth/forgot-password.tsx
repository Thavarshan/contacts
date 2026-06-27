import Head from 'next/head';
import Link from 'next/link';
import type { ReactElement } from 'react';

import AuthLayout from '@/components/layouts/auth';
import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Field, FieldDescription, FieldGroup, FieldLabel } from '@/components/ui/field';
import { Input } from '@/components/ui/input';
import type { NextPageWithLayout } from '@/pages/_app';

const ForgotPasswordPage: NextPageWithLayout = () => {
  return (
    <>
      <Head>
        <title>Forgot Password | Contacts Manager</title>
        <meta
          name="description"
          content="Request a password reset link for Contacts Manager."
        />
      </Head>
      <Card>
        <CardHeader>
          <CardTitle role="heading" aria-level={1}>
            Reset your password
          </CardTitle>
          <CardDescription>
            Enter your email and we&apos;ll send a password reset link.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form>
            <FieldGroup>
              <Field>
                <FieldLabel htmlFor="email">Email</FieldLabel>
                <Input id="email" type="email" placeholder="you@example.com" required />
              </Field>
              <Field>
                <Button type="submit">Send reset link</Button>
                <FieldDescription className="text-center">
                  Remembered your password? <Link href="/auth/login">Login</Link>
                </FieldDescription>
              </Field>
            </FieldGroup>
          </form>
        </CardContent>
      </Card>
    </>
  );
};

ForgotPasswordPage.getLayout = function getLayout(page: ReactElement) {
  return <AuthLayout>{page}</AuthLayout>;
};

export default ForgotPasswordPage;
