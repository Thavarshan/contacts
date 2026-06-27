import { render, screen, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it } from 'vitest';

import { DataTable, type Contact } from '@/components/blocks/contacts/data-table';

const contacts: Contact[] = [
  {
    id: '1',
    firstName: 'Sarah',
    lastName: 'Chen',
    company: 'Northstar Labs',
    jobTitle: 'Product Lead',
    phone: '+1 (415) 555-0132',
    email: 'sarah.chen@example.com',
    street: '120 Market Street',
    city: 'San Francisco',
    state: 'CA',
    postalCode: '94105',
    country: 'United States',
    birthday: '1988-04-12',
    notes: 'Prefers email for follow-ups.',
  },
  {
    id: '2',
    firstName: 'Marcus',
    lastName: 'Reed',
    company: 'Reed Advisory',
    jobTitle: 'Principal',
    phone: '+1 (212) 555-0198',
    email: 'marcus.reed@example.com',
    street: '44 West 18th Street',
    city: 'New York',
    state: 'NY',
    postalCode: '10011',
    country: 'United States',
    birthday: '1979-09-03',
    notes: 'Met at the spring networking event.',
  },
];

const paginatedContacts = Array.from({ length: 10 }, (_, index) => ({
  ...contacts[index % contacts.length],
  id: String(index + 1),
  firstName: `Contact${index + 1}`,
  lastName: 'Person',
  email: `contact${index + 1}@example.com`,
}));

function renderTable() {
  return {
    user: userEvent.setup(),
    ...render(<DataTable data={contacts} />),
  };
}

async function createContact(user: ReturnType<typeof userEvent.setup>) {
  await user.click(screen.getByRole('button', { name: /add contact/i }));
  await user.type(screen.getByLabelText(/first name/i), 'Nina');
  await user.type(screen.getByLabelText(/last name/i), 'Patel');
  await user.type(screen.getByLabelText(/company/i), 'Patel Studio');
  await user.type(screen.getByLabelText(/job title/i), 'Creative Director');
  await user.type(screen.getByLabelText(/phone/i), '+1 (646) 555-0147');
  await user.type(screen.getByLabelText(/email/i), 'nina.patel@example.com');
  await user.type(screen.getByLabelText(/street/i), '89 Kent Avenue');
  await user.type(screen.getByLabelText(/city/i), 'Brooklyn');
  await user.type(screen.getByLabelText(/state/i), 'NY');
  await user.type(screen.getByLabelText(/postal code/i), '11249');
  await user.type(screen.getByLabelText(/country/i), 'United States');
  await user.type(screen.getByLabelText(/birthday/i), '1991-01-27');
  await user.type(screen.getByLabelText(/notes/i), 'Interested in import workflow.');
  await user.click(screen.getByRole('button', { name: /create contact/i }));
}

describe('DataTable', () => {
  it('renders contacts with search and pagination status', () => {
    renderTable();

    expect(screen.getByRole('textbox')).toHaveAttribute('placeholder', 'Search contacts');
    expect(screen.getByRole('button', { name: /add contact/i })).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /\+1 \(415\) 555-0132/i })).toHaveAttribute(
      'href',
      'tel:+1 (415) 555-0132',
    );
    expect(screen.getByText(/showing 2 of 2 filtered contact/i)).toBeInTheDocument();
  });

  it('filters contacts by name, company, and notes', async () => {
    const { user } = renderTable();
    const search = screen.getByRole('textbox');

    await user.type(search, 'spring networking');

    expect(screen.getByText(/marcus reed/i)).toBeInTheDocument();
    expect(screen.queryByText(/sarah chen/i)).not.toBeInTheDocument();
    expect(screen.getByText(/showing 1 of 1 filtered contact/i)).toBeInTheDocument();
  });

  it('creates a contact from the reusable form dialog', async () => {
    const { user } = renderTable();

    await createContact(user);

    expect(screen.getByText(/nina patel/i)).toBeInTheDocument();
    expect(screen.getByText(/patel studio - creative director/i)).toBeInTheDocument();
    expect(screen.getByText(/showing 3 of 3 filtered contact/i)).toBeInTheDocument();
  });

  it('opens a contact details drawer from a contact name', async () => {
    const { user } = renderTable();
    const row = screen.getByRole('row', { name: /sarah chen/i });

    await user.click(within(row).getByRole('button', { name: /sarah chen/i }));

    expect(screen.getByRole('heading', { name: /sarah chen/i })).toBeInTheDocument();
    expect(screen.getByText(/product lead at northstar labs/i)).toBeInTheDocument();
    expect(screen.getByText(/prefers email for follow-ups/i)).toBeInTheDocument();
  });

  it('supports selecting all visible contacts and bulk deleting them', async () => {
    const { user } = renderTable();

    await user.click(
      screen.getByRole('checkbox', { name: /select all contacts on this page/i }),
    );

    expect(screen.getByText(/2 contact\(s\) selected/i)).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: /delete selected/i }));

    expect(screen.getByText(/no contacts found/i)).toBeInTheDocument();
  });

  it('paginates longer contact lists', async () => {
    const user = userEvent.setup();
    render(<DataTable data={paginatedContacts} />);

    expect(screen.getByText(/page 1 of 2/i)).toBeInTheDocument();
    expect(screen.getByText(/contact1 person/i)).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: /next/i }));

    expect(screen.getByText(/page 2 of 2/i)).toBeInTheDocument();
    expect(screen.getByText(/contact9 person/i)).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: /previous/i }));

    expect(screen.getByText(/page 1 of 2/i)).toBeInTheDocument();
  });

  it('reveals bulk email and clear actions after selecting a contact', async () => {
    const { user } = renderTable();

    await user.click(screen.getAllByRole('checkbox', { name: /select contact/i })[0]);

    expect(screen.getByText(/1 contact\(s\) selected/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /email selected/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /clear/i })).toBeInTheDocument();
  });
});
