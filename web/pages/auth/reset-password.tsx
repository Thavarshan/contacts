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

const ResetPasswordPage: NextPageWithLayout = () => {
  return (
    <>
      <Head>
        <title>Reset Password | Contacts Manager</title>
        <meta name="description" content="Set a new Contacts Manager password." />
      </Head>
      <Card>
        <CardHeader>
          <CardTitle role="heading" aria-level={1}>
            Choose a new password
          </CardTitle>
          <CardDescription>
            Enter and confirm the new password for your account.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form>
            <FieldGroup>
              <Field>
                <FieldLabel htmlFor="password">New password</FieldLabel>
                <Input id="password" type="password" required />
                <FieldDescription>Use at least 8 characters.</FieldDescription>
              </Field>
              <Field>
                <FieldLabel htmlFor="confirm-password">Confirm password</FieldLabel>
                <Input id="confirm-password" type="password" required />
              </Field>
              <Field>
                <Button type="submit">Update password</Button>
                <FieldDescription className="text-center">
                  Back to <Link href="/auth/login">login</Link>
                </FieldDescription>
              </Field>
            </FieldGroup>
          </form>
        </CardContent>
      </Card>
    </>
  );
};

ResetPasswordPage.getLayout = function getLayout(page: ReactElement) {
  return <AuthLayout>{page}</AuthLayout>;
};

export default ResetPasswordPage;
