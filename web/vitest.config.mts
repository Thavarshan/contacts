import react from '@vitejs/plugin-react';
import { defineConfig } from 'vitest/config';

export default defineConfig({
  plugins: [react()],
  resolve: {
    tsconfigPaths: true,
  },
  test: {
    coverage: {
      exclude: [
        'components/ui/**',
        'components/layouts/**',
        'components/blocks/contacts/nav-user.tsx',
        'pages/_app.tsx',
        'pages/_document.tsx',
        'next-env.d.ts',
      ],
      include: [
        'components/blocks/**/*.{ts,tsx}',
        'hooks/**/*.{ts,tsx}',
        'lib/**/*.{ts,tsx}',
        'pages/**/*.{ts,tsx}',
      ],
      provider: 'v8',
      reporter: ['text', 'lcov'],
      thresholds: {
        branches: 70,
        functions: 70,
        lines: 70,
        statements: 70,
      },
    },
    environment: 'jsdom',
    globals: true,
    include: ['tests/**/*.{test,spec}.{ts,tsx}'],
    setupFiles: ['./tests/vitest.setup.ts'],
  },
});
