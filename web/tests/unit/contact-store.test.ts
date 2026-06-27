import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';

import {
  createContact,
  deleteContact,
  deleteContacts,
  listContacts,
  updateContact,
} from '@/lib/contact-store';

const contact = {
  id: 'contact-1',
  firstName: 'Jane',
  lastName: 'Doe',
  company: 'Acme',
  jobTitle: 'Engineer',
  phone: '+1 555 0100',
  email: 'jane@example.com',
  street: '1 Main St',
  city: 'New York',
  state: 'NY',
  postalCode: '10001',
  country: 'United States',
  birthday: '1990-01-02',
  notes: 'Created through API',
};

const values = {
  firstName: contact.firstName,
  lastName: contact.lastName,
  company: contact.company,
  jobTitle: contact.jobTitle,
  phone: contact.phone,
  email: contact.email,
  street: contact.street,
  city: contact.city,
  state: contact.state,
  postalCode: contact.postalCode,
  country: contact.country,
  birthday: contact.birthday,
  notes: contact.notes,
};

const fetchMock = vi.fn<typeof fetch>();

beforeEach(() => {
  fetchMock.mockReset();
  vi.stubGlobal('fetch', fetchMock);
  process.env.CONTACTS_API_URL = 'http://contacts-api.test';
});

afterEach(() => {
  vi.unstubAllGlobals();
  delete process.env.CONTACTS_API_URL;
});

describe('contact store', () => {
  it('lists contacts through the Java API', async () => {
    fetchMock.mockResolvedValueOnce(jsonResponse({ contacts: [contact] }));

    await expect(listContacts()).resolves.toEqual([contact]);
    expect(fetchMock).toHaveBeenCalledWith('http://contacts-api.test/api/contacts', {
      headers: expect.any(Headers) as Headers,
    });
    expect(requestHeaders().get('Content-Type')).toBe('application/json');
  });

  it('creates contacts through the Java API', async () => {
    fetchMock.mockResolvedValueOnce(jsonResponse({ contact }));

    await expect(createContact(values)).resolves.toEqual(contact);
    expect(fetchMock).toHaveBeenCalledWith(
      'http://contacts-api.test/api/contacts',
      expect.objectContaining({
        body: JSON.stringify(values),
        method: 'POST',
      }),
    );
  });

  it('updates contacts and returns null for missing contacts', async () => {
    fetchMock
      .mockResolvedValueOnce(
        jsonResponse({ contact: { ...contact, company: 'Acme Labs' } }),
      )
      .mockResolvedValueOnce(jsonResponse({ message: 'Contact not found' }, 404));

    await expect(
      updateContact(contact.id, { ...values, company: 'Acme Labs' }),
    ).resolves.toEqual(expect.objectContaining({ company: 'Acme Labs' }));
    await expect(updateContact('missing', values)).resolves.toBeNull();
  });

  it('deletes contacts and reports missing contacts', async () => {
    fetchMock
      .mockResolvedValueOnce(jsonResponse({ deleted: 1 }))
      .mockResolvedValueOnce(jsonResponse({ message: 'Contact not found' }, 404));

    await expect(deleteContact(contact.id)).resolves.toBe(true);
    await expect(deleteContact('missing')).resolves.toBe(false);
  });

  it('bulk deletes contacts by stable id', async () => {
    fetchMock.mockResolvedValueOnce(jsonResponse({ deleted: 2 }));

    await expect(deleteContacts(['first', 'second'])).resolves.toBe(2);
    expect(fetchMock).toHaveBeenCalledWith(
      'http://contacts-api.test/api/contacts',
      expect.objectContaining({
        body: JSON.stringify({ ids: ['first', 'second'] }),
        method: 'DELETE',
      }),
    );
  });

  it('raises API error messages', async () => {
    fetchMock.mockResolvedValueOnce(jsonResponse({ message: 'API unavailable' }, 503));

    await expect(listContacts()).rejects.toThrow('API unavailable');
  });
});

function jsonResponse(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    headers: { 'Content-Type': 'application/json' },
    status,
  });
}

function requestHeaders() {
  const init = fetchMock.mock.calls.at(-1)?.[1];

  if (!init || !(init.headers instanceof Headers)) {
    throw new Error('Expected request headers');
  }

  return init.headers;
}
