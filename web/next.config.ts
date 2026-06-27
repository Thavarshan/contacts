import type { NextConfig } from 'next';
import path from 'node:path';

const nextConfig: NextConfig = {
  allowedDevOrigins: ['127.0.0.1'],
  output: 'standalone',
  turbopack: {
    root: path.resolve(__dirname),
  },
};

export default nextConfig;
