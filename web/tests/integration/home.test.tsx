import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import Home from '@/pages';

describe('Home page', () => {
  it('renders the landing page with auth and dashboard entry points', () => {
    render(<Home />);

    expect(
      screen.getByRole('heading', {
        level: 1,
        name: /manage every contact, conversation, and next step/i,
      }),
    ).toBeInTheDocument();
    expect(screen.getAllByRole('link', { name: /sign in/i })).toHaveLength(2);
    expect(screen.getAllByRole('link', { name: /dashboard/i })).toHaveLength(2);
    screen
      .getAllByRole('link', { name: /dashboard/i })
      .forEach((link) => expect(link).toHaveAttribute('href', '/dashboard'));
    expect(screen.getByText(/sarah chen/i)).toBeInTheDocument();
  });
});
