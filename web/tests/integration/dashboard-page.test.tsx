import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';

import Dashboard from '@/pages/dashboard';

describe('Dashboard page', () => {
  it('renders the contacts workspace shell and table', () => {
    render(<Dashboard />);

    expect(screen.getByRole('banner')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /toggle sidebar/i })).toBeInTheDocument();
    expect(screen.getByRole('table')).toBeInTheDocument();
    expect(screen.getByText(/sarah chen/i)).toBeInTheDocument();
    expect(screen.getAllByRole('button', { name: /add contact/i })).toHaveLength(3);
  });
});
