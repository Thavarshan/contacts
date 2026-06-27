import { schema, type Contact } from '@/lib/contact-record';

type ContactValues = Omit<Contact, 'id'>;

const DEFAULT_API_URL = 'http://127.0.0.1:7070';

export async function listContacts() {
  const response = await request<{ contacts: unknown[] }>('/api/contacts');

  return response.contacts.map((contact) => schema.parse(contact));
}

export async function createContact(values: ContactValues) {
  const response = await request<{ contact: unknown }>('/api/contacts', {
    body: JSON.stringify(values),
    method: 'POST',
  });

  return schema.parse(response.contact);
}

export async function updateContact(id: string, values: ContactValues) {
  const response = await fetch(apiUrl(`/api/contacts/${encodeURIComponent(id)}`), {
    body: JSON.stringify({ ...values, id }),
    headers: { 'Content-Type': 'application/json' },
    method: 'PUT',
  });

  if (response.status === 404) {
    return null;
  }

  const body = await parseResponse<{ contact: unknown }>(response);

  return schema.parse(body.contact);
}

export async function deleteContact(id: string) {
  const response = await fetch(apiUrl(`/api/contacts/${encodeURIComponent(id)}`), {
    method: 'DELETE',
  });

  if (response.status === 404) {
    return false;
  }

  await parseResponse<{ deleted: number }>(response);

  return true;
}

export async function deleteContacts(ids: string[]) {
  const response = await request<{ deleted: number }>('/api/contacts', {
    body: JSON.stringify({ ids }),
    method: 'DELETE',
  });

  return response.deleted;
}

async function request<TResponse>(path: string, init: RequestInit = {}) {
  const headers = new Headers(init.headers);
  headers.set('Content-Type', 'application/json');

  return parseResponse<TResponse>(
    await fetch(apiUrl(path), {
      ...init,
      headers,
    }),
  );
}

async function parseResponse<TResponse>(response: Response) {
  if (!response.ok) {
    throw new Error(await errorMessage(response));
  }

  return (await response.json()) as TResponse;
}

async function errorMessage(response: Response) {
  try {
    const body = (await response.json()) as { message?: unknown };

    if (typeof body.message === 'string' && body.message) {
      return body.message;
    }
  } catch {
    // Fall through to the status-based message when the API does not return JSON.
  }

  return `Contacts API request failed with status ${response.status.toString()}`;
}

function apiUrl(path: string) {
  return `${apiBaseUrl()}${path}`;
}

function apiBaseUrl() {
  return (process.env.CONTACTS_API_URL ?? DEFAULT_API_URL).replace(/\/+$/, '');
}
