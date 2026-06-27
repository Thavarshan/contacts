import Head from 'next/head';
import type { NextPage } from 'next';

import { DashboardContent } from '@/components/blocks/dashboard-content';

const Dashboard: NextPage = () => {
  return (
    <>
      <Head>
        <title>Contacts Manager | Dashboard</title>
        <meta
          name="description"
          content="A contacts list manager dashboard built with the Next.js Pages Router."
        />
      </Head>

      <DashboardContent />
    </>
  );
};

export default Dashboard;
