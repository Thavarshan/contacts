import type { NextApiRequest, NextApiResponse } from 'next';

import { deleteContact, updateContact } from '@/lib/contact-store';
import { schema } from '@/lib/contact-record';

const updateContactSchema = schema.omit({ id: true });

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  const id = readId(req.query.id);

  try {
    if (req.method === 'PUT') {
      const contact = await updateContact(id, updateContactSchema.parse(req.body));

      if (!contact) {
        res.status(404).json({ message: 'Contact not found' });
        return;
      }

      res.status(200).json({ contact });
      return;
    }

    if (req.method === 'DELETE') {
      const deleted = await deleteContact(id);

      if (!deleted) {
        res.status(404).json({ message: 'Contact not found' });
        return;
      }

      res.status(200).json({ deleted: 1 });
      return;
    }

    res.setHeader('Allow', 'PUT, DELETE');
    res.status(405).json({ message: 'Method not allowed' });
  } catch (error) {
    res.status(400).json({
      message: error instanceof Error ? error.message : 'Unable to process contact',
    });
  }
}

function readId(value: string | string[] | undefined) {
  if (typeof value === 'string' && value) {
    return value;
  }

  throw new Error('Expected contact id');
}
