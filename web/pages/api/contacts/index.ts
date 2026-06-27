import type { NextApiRequest, NextApiResponse } from 'next';

import { createContact, deleteContacts, listContacts } from '@/lib/contact-store';
import { schema } from '@/lib/contact-record';

const createContactSchema = schema.omit({ id: true });

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  try {
    if (req.method === 'GET') {
      res.status(200).json({ contacts: await listContacts() });
      return;
    }

    if (req.method === 'POST') {
      const contact = await createContact(createContactSchema.parse(req.body));
      res.status(201).json({ contact });
      return;
    }

    if (req.method === 'DELETE') {
      const ids = readIds(req.body);
      const deleted = await deleteContacts(ids);
      res.status(200).json({ deleted });
      return;
    }

    res.setHeader('Allow', 'GET, POST, DELETE');
    res.status(405).json({ message: 'Method not allowed' });
  } catch (error) {
    res.status(400).json({
      message: error instanceof Error ? error.message : 'Unable to process contacts',
    });
  }
}

function readIds(body: unknown) {
  if (
    typeof body === 'object' &&
    body !== null &&
    'ids' in body &&
    Array.isArray(body.ids) &&
    body.ids.every((id) => typeof id === 'string')
  ) {
    return body.ids;
  }

  throw new Error('Expected ids to be a string array');
}
