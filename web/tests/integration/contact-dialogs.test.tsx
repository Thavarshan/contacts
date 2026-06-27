import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';

import { ContactDeleteDialog } from '@/components/blocks/contacts/contact-delete-dialog';
import {
  ContactFormDialog,
  type ContactFormValues,
} from '@/components/blocks/contacts/contact-form-dialog';
import type { Contact } from '@/components/blocks/contacts/data-table';

const contact: Contact = {
  id: '7',
  firstName: 'Priya',
  lastName: 'Shah',
  company: 'Brightline Health',
  jobTitle: 'Program Manager',
  phone: '+1 (617) 555-0104',
  email: 'priya.shah@example.com',
  street: '12 Boylston Street',
  city: 'Boston',
  state: 'MA',
  postalCode: '02116',
  country: 'United States',
  birthday: '1986-12-01',
  notes: 'Send calendar invites in Eastern time.',
};

describe('ContactFormDialog', () => {
  it('prefills edit values and submits the existing contact id', async () => {
    const user = userEvent.setup();
    const onSave = vi.fn();

    render(
      <ContactFormDialog contact={contact} open onOpenChange={vi.fn()} onSave={onSave} />,
    );

    expect(screen.getByRole('heading', { name: /edit contact/i })).toBeInTheDocument();
    expect(screen.getByLabelText(/first name/i)).toHaveValue('Priya');

    await user.clear(screen.getByLabelText(/notes/i));
    await user.type(screen.getByLabelText(/notes/i), 'Prefers morning calls.');
    await user.click(screen.getByRole('button', { name: /save changes/i }));

    expect(onSave).toHaveBeenCalledWith(
      expect.objectContaining<ContactFormValues>({
        firstName: 'Priya',
        lastName: 'Shah',
        company: 'Brightline Health',
        jobTitle: 'Program Manager',
        phone: '+1 (617) 555-0104',
        email: 'priya.shah@example.com',
        street: '12 Boylston Street',
        city: 'Boston',
        state: 'MA',
        postalCode: '02116',
        country: 'United States',
        birthday: '1986-12-01',
        notes: 'Prefers morning calls.',
      }),
      '7',
    );
  });

  it('closes through the cancel action', async () => {
    const user = userEvent.setup();
    const onOpenChange = vi.fn();

    render(
      <ContactFormDialog
        contact={null}
        open
        onOpenChange={onOpenChange}
        onSave={vi.fn()}
      />,
    );

    await user.click(screen.getByRole('button', { name: /cancel/i }));

    expect(onOpenChange).toHaveBeenCalledWith(false);
  });
});

describe('ContactDeleteDialog', () => {
  it('confirms deletion for the selected contact', async () => {
    const user = userEvent.setup();
    const onConfirm = vi.fn();

    render(
      <ContactDeleteDialog
        contact={contact}
        open
        onOpenChange={vi.fn()}
        onConfirm={onConfirm}
      />,
    );

    expect(screen.getByText(/priya shah/i)).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: /delete contact/i }));

    expect(onConfirm).toHaveBeenCalledWith(contact);
  });

  it('does not confirm when no contact is available', async () => {
    const user = userEvent.setup();
    const onConfirm = vi.fn();

    render(
      <ContactDeleteDialog
        contact={null}
        open
        onOpenChange={vi.fn()}
        onConfirm={onConfirm}
      />,
    );

    await user.click(screen.getByRole('button', { name: /delete contact/i }));

    expect(onConfirm).not.toHaveBeenCalled();
  });
});
