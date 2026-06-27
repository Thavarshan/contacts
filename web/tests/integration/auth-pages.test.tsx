import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';

import ForgotPasswordPage from '@/pages/auth/forgot-password';
import LoginPage from '@/pages/auth/login';
import RegisterPage from '@/pages/auth/register';
import ResetPasswordPage from '@/pages/auth/reset-password';

describe('auth pages', () => {
  it('renders login with account and recovery links', () => {
    render(<LoginPage />);

    expect(
      screen.getByRole('heading', { name: /login to your account/i }),
    ).toBeInTheDocument();
    expect(screen.getByLabelText(/email/i)).toHaveAttribute('type', 'email');
    expect(screen.getByLabelText(/^password$/i)).toHaveAttribute('type', 'password');
    expect(screen.getByRole('link', { name: /forgot your password/i })).toHaveAttribute(
      'href',
      '/auth/forgot-password',
    );
    expect(screen.getByRole('link', { name: /register/i })).toHaveAttribute(
      'href',
      '/auth/register',
    );
  });

  it('renders registration fields and login link', () => {
    render(<RegisterPage />);

    expect(
      screen.getByRole('heading', { name: /create an account/i }),
    ).toBeInTheDocument();
    expect(screen.getByLabelText(/full name/i)).toBeRequired();
    expect(screen.getByLabelText(/^email$/i)).toBeRequired();
    expect(screen.getByLabelText(/^password$/i)).toBeRequired();
    expect(screen.getByLabelText(/confirm password/i)).toBeRequired();
    expect(screen.getByRole('link', { name: /login/i })).toHaveAttribute(
      'href',
      '/auth/login',
    );
  });

  it('renders forgot password email capture', () => {
    render(<ForgotPasswordPage />);

    expect(
      screen.getByRole('heading', { name: /reset your password/i }),
    ).toBeInTheDocument();
    expect(screen.getByLabelText(/email/i)).toHaveAttribute('type', 'email');
    expect(screen.getByRole('button', { name: /send reset link/i })).toBeInTheDocument();
  });

  it('renders reset password confirmation fields', () => {
    render(<ResetPasswordPage />);

    expect(
      screen.getByRole('heading', { name: /choose a new password/i }),
    ).toBeInTheDocument();
    expect(screen.getByLabelText(/new password/i)).toHaveAttribute('type', 'password');
    expect(screen.getByLabelText(/confirm password/i)).toHaveAttribute(
      'type',
      'password',
    );
    expect(screen.getByRole('button', { name: /update password/i })).toBeInTheDocument();
  });
});
